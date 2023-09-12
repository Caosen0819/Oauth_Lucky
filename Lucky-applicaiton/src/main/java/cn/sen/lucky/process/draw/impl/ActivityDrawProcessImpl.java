package cn.sen.lucky.process.draw.impl;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.activity.model.req.PartakeReq;
import cn.sen.lucky.domain.activity.model.res.PartakeResult;
import cn.sen.lucky.domain.activity.model.vo.ActivityPartakeRecordVO;
import cn.sen.lucky.domain.activity.model.vo.DrawOrderVO;
import cn.sen.lucky.domain.activity.model.vo.InvoiceVO;
import cn.sen.lucky.domain.activity.service.partake.IActivityPartake;
import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.res.EngineResult;
import cn.sen.lucky.domain.rule.service.engine.EngineFilter;
import cn.sen.lucky.domain.strategy.model.req.DrawReq;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;
import cn.sen.lucky.domain.strategy.model.vo.DrawAwardVO;
import cn.sen.lucky.domain.strategy.service.draw.IDrawExec;
import cn.sen.lucky.domain.support.ids.IIdGenerator;
import cn.sen.lucky.mq.producer.KafkaProducer;
import cn.sen.lucky.process.draw.IActivityDrawProcess;
import cn.sen.lucky.process.draw.req.DrawProcessReq;
import cn.sen.lucky.process.draw.res.DrawProcessResult;
import cn.sen.lucky.process.draw.res.RuleQuantificationCrowdResult;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class ActivityDrawProcessImpl implements IActivityDrawProcess {

    private Logger logger = LoggerFactory.getLogger(ActivityDrawProcessImpl.class);

    @Resource
    private IActivityPartake activityPartake;

    @Resource
    private IDrawExec drawExec;

    @Resource(name = "ruleEngineHandle")
    private EngineFilter engineFilter;

    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        // 1. 领取活动
        PartakeResult partakeResult = activityPartake.doPartake(new PartakeReq(req.getuId(), req.getActivityId()));
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode()) && !Constants.ResponseCode.NOT_CONSUMED_TAKE.getCode().equals(partakeResult.getCode())) {
            return new DrawProcessResult(partakeResult.getCode(), partakeResult.getInfo());
        }

        // 2. 首次成功领取活动，发送 MQ 消息
        if (Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())) {
            ActivityPartakeRecordVO activityPartakeRecord = new ActivityPartakeRecordVO();
            activityPartakeRecord.setuId(req.getuId());
            activityPartakeRecord.setActivityId(req.getActivityId());
            activityPartakeRecord.setStockCount(partakeResult.getStockCount());
            activityPartakeRecord.setStockSurplusCount(partakeResult.getStockSurplusCount());
            // 发送 MQ 消息
//            kafkaProducer.sendLuckyActivityPartakeRecord(activityPartakeRecord);
//            fasong1 = "发送MQ消息(领取活动记录) topic：lucky_activity_partake bizId：fuzhengwei message：" + JSON.toJSONString(activityPartakeRecord) + "\n";
//            xiaofei1 = "消费MQ消息，异步扣减活动库存:" + JSON.toJSONString(activityPartakeRecord) + "\n";
        }


        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId() ;

        // 3. 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq(req.getuId(), strategyId));
        String drawResultstring = JSON.toJSONString(drawResult) + "\n";
        if (Constants.DrawState.FAIL.getCode().equals(drawResult.getDrawState())) {
            return new DrawProcessResult(Constants.ResponseCode.LOSING_DRAW.getCode(), Constants.ResponseCode.LOSING_DRAW.getInfo());
        }
        DrawAwardVO drawAwardVO = drawResult.getDrawAwardInfo();

        // 4. 结果落库

        DrawOrderVO drawOrderVO = buildDrawOrderVO(req, strategyId, takeId, drawAwardVO);

        Result recordResult = activityPartake.recordDrawOrder(drawOrderVO);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(recordResult.getCode())) {
            return new DrawProcessResult(recordResult.getCode(), recordResult.getInfo());
        }

        // 5. 发送MQ，触发发奖流程
        InvoiceVO invoiceVO = buildInvoiceVO(drawOrderVO);
        ListenableFuture<SendResult<String, Object>> future = kafkaProducer.sendLuckyInvoice(invoiceVO);
//        String fasong2 = "发送MQ消息(中奖发货单)" + JSON.toJSONString(invoiceVO) + "\n";
//        String xiaofei2 = " 消费MQ消息，完成 topic：lucky_invoice bizId：fuzhengwei 发奖结果：{code: 1, info: 发奖成功 , uId : fuzhengwei }" + "\n";
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // 4.1 MQ 消息发送完成，更新数据库表 user_strategy_export.mq_state = 1
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.COMPLETE.getCode());
            }

            @Override
            public void onFailure(Throwable throwable) {
                // 4.2 MQ 消息发送失败，更新数据库表 user_strategy_export.mq_state = 2 【等待定时任务扫码补偿MQ消息】
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.FAIL.getCode());
            }

        });
        DrawProcessResult drawProcessResult = new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawAwardVO);
//        String log = fasong1 + xiaofei2 + drawResultstring + fasong2 + xiaofei2  + "\n";
//        drawProcessResult.setLog(log);
        // 6. 返回结果
        return drawProcessResult;
    }

    @Override
    public RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req) {
        // 1. 量化决策
        EngineResult engineResult = engineFilter.process(req);

        if (!engineResult.isSuccess()) {
            return new RuleQuantificationCrowdResult(Constants.ResponseCode.RULE_ERR.getCode(), Constants.ResponseCode.RULE_ERR.getInfo());
        }

        // 2. 封装结果
        RuleQuantificationCrowdResult ruleQuantificationCrowdResult = new RuleQuantificationCrowdResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        ruleQuantificationCrowdResult.setActivityId(Long.valueOf(engineResult.getNodeValue()));

        return ruleQuantificationCrowdResult;
    }

    public DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardVO drawAwardVO) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setuId(req.getuId());
        drawOrderVO.setTakeId(orderId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardVO.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardVO.getGrantType());
        drawOrderVO.setGrantDate(drawAwardVO.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardVO.getAwardId());
        drawOrderVO.setAwardType(drawAwardVO.getAwardType());
        drawOrderVO.setAwardName(drawAwardVO.getAwardName());
        drawOrderVO.setAwardContent(drawAwardVO.getAwardContent());
        return drawOrderVO;
    }

    private InvoiceVO buildInvoiceVO(DrawOrderVO drawOrderVO) {
        InvoiceVO invoiceVO = new InvoiceVO();
        invoiceVO.setuId(drawOrderVO.getuId());
        invoiceVO.setOrderId(drawOrderVO.getOrderId());
        invoiceVO.setAwardId(drawOrderVO.getAwardId());
        invoiceVO.setAwardType(drawOrderVO.getAwardType());
        invoiceVO.setAwardName(drawOrderVO.getAwardName());
        invoiceVO.setAwardContent(drawOrderVO.getAwardContent());
        invoiceVO.setShippingAddress(null);
        invoiceVO.setExtInfo(null);
        return invoiceVO;
    }

}
