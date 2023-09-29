package cn.sen.lucky.controller;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.activity.model.req.PartakeReq;
import cn.sen.lucky.domain.activity.model.res.PartakeResult;
import cn.sen.lucky.domain.activity.model.vo.ActivityPartakeRecordVO;
import cn.sen.lucky.domain.activity.model.vo.DrawOrderVO;
import cn.sen.lucky.domain.activity.service.partake.IActivityPartake;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;
import cn.sen.lucky.domain.strategy.model.vo.DrawAwardVO;
import cn.sen.lucky.domain.strategy.service.draw.IDrawExec;
import cn.sen.lucky.domain.support.ids.IIdGenerator;
import cn.sen.lucky.mq.producer.KafkaProducer;
import cn.sen.lucky.process.draw.IActivityDrawProcess;
import cn.sen.lucky.process.draw.req.DrawProcessReq;
import cn.sen.lucky.process.draw.res.DrawProcessResult;
import cn.sen.lucky.rpc.activity.booth.ILuckyActivityBooth;
import cn.sen.lucky.rpc.activity.booth.req.DrawReq;
import cn.sen.lucky.rpc.activity.booth.res.DrawRes;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author caosen
 * @Date 2023/4/22 15:26
 */
@RestController
@RequestMapping("/do")
public class controller {
    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;
    @Resource
    private ILuckyActivityBooth luckyActivityBooth;
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private IActivityPartake activityPartake;
    @Resource
    private IDrawExec drawExec;
    @Resource
    private IActivityDrawProcess activityProcess;
    @GetMapping("/hello")
    public String hello(){

        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);
        DrawRes drawRes = luckyActivityBooth.doDraw(req);
        return Constants.ResponseCode.SUCCESS.toString();
    }

    @GetMapping("/yace")
    public String yace() {
        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);

        PartakeResult partakeResult = activityPartake.doPartake(new PartakeReq("fuzhengwei", 100001L));
        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId() ;
        DrawResult drawResult = drawExec.doDrawExec(new cn.sen.lucky.domain.strategy.model.req.DrawReq(req.getuId(), strategyId));

        return Constants.ResponseCode.SUCCESS.toString();
    }

    /**
     *
     * @return
     */
    @GetMapping("/yace2")
    public String yace2() {
        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);

        PartakeResult partakeResult = activityPartake.doPartake2(new PartakeReq("fuzhengwei", 100001L));
        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId() ;
        DrawResult drawResult = drawExec.doDrawExec2(new cn.sen.lucky.domain.strategy.model.req.DrawReq(req.getuId(), strategyId));

        return Constants.ResponseCode.SUCCESS.toString();
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
    @PostMapping("/hello2")
    public String hello2(){

        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);
        DrawRes drawRes = luckyActivityBooth.doDraw(req);
        return drawRes.toString();
    }

}
