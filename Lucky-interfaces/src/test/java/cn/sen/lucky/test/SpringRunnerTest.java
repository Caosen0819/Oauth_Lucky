package cn.sen.lucky.test;


import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.award.model.req.GoodsReq;
import cn.sen.lucky.domain.award.model.res.DistributionRes;
import cn.sen.lucky.domain.award.serve.factory.DistributionGoodsFactory;
import cn.sen.lucky.domain.award.serve.factory.GoodsConfig;
import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
import cn.sen.lucky.domain.strategy.model.req.DrawReq;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;
import cn.sen.lucky.domain.strategy.model.vo.DrawAwardInfo;
import cn.sen.lucky.domain.strategy.service.draw.IDrawExec;
import cn.sen.lucky.domain.strategy.service.draw.impl.DrawExecImpl;
import cn.sen.lucky.infrastructure.dao.IActivityDao;
import cn.sen.lucky.infrastructure.po.Activity;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;



@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRunnerTest {

    private Logger logger = LoggerFactory.getLogger(SpringRunnerTest.class);

    @Resource
    private IActivityDao activityDao;

    @Resource
    private IDrawExec drawExec;

    @Resource
    private DrawExecImpl drawExec1;
    @Resource
    private DistributionGoodsFactory distributionGoodsFactory;

    @Test
    public void test_drawExec() {
        drawExec.doDrawExec(new DrawReq("小傅哥", 10001L));
        drawExec.doDrawExec(new DrawReq("小佳佳", 10001L));
        drawExec.doDrawExec(new DrawReq("小蜗牛", 10001L));
        drawExec.doDrawExec(new DrawReq("八杯水", 10001L));
    }

    @Test
    public void test_award() {

        // 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq("小傅哥", 10001L));

        // 判断抽奖结果
        Integer drawState = drawResult.getDrawState();
        if (Constants.DrawState.FAIL.getCode().equals(drawState)) {
            logger.info("未中奖 DrawAwardInfo is null");
            return;
        }

        // 封装发奖参数，orderId：2109313442431 为模拟ID，需要在用户参与领奖活动时生成
        DrawAwardInfo drawAwardInfo = drawResult.getDrawAwardInfo();
        GoodsReq goodsReq = new GoodsReq(drawResult.getuId(), "2109313442431", drawAwardInfo.getAwardId(), drawAwardInfo.getAwardName(), drawAwardInfo.getAwardContent());


        // 根据 awardType 从抽奖工厂中获取对应的发奖服务
        IDistributionGoods distributionGoodsService = distributionGoodsFactory.getDistributionGoodsService(drawAwardInfo.getAwardType());
        DistributionRes distributionRes = distributionGoodsService.doDistribution(goodsReq);

        logger.info("测试结果：{}", JSON.toJSONString(distributionRes));
    }
    @Test
    public void test_insert() {
        Activity activity = new Activity();
        activity.setActivityId(100001L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("仅用于插入数据测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(100);
        activity.setTakeCount(10);
        activity.setState(0);
        activity.setCreator("xiaofuge");
        activityDao.insert(activity);
    }

    @Test
    public void test_select() {
        Activity activity = activityDao.queryActivityById(100001L);
        logger.info("测试结果：{}", JSON.toJSONString(activity));
    }

}
