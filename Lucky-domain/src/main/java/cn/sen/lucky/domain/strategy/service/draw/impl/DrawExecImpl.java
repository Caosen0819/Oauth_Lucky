package cn.sen.lucky.domain.strategy.service.draw.impl;

import cn.sen.lucky.domain.strategy.model.req.DrawReq;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;
import cn.sen.lucky.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.sen.lucky.domain.strategy.service.draw.DrawBase;
import cn.sen.lucky.domain.strategy.service.draw.IDrawExec;
import cn.sen.lucky.infrastructure.dao.IAwardDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDetailDao;
import cn.sen.lucky.infrastructure.po.Award;
import cn.sen.lucky.infrastructure.po.Strategy;
import cn.sen.lucky.infrastructure.po.StrategyDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 17:45
 */
@Service("DrawExec")
public class DrawExecImpl extends DrawBase implements IDrawExec {

    private Logger logger = LoggerFactory.getLogger(DrawExecImpl.class);

    @Resource
    private IStrategyDao iStrategyDao;

    @Resource
    private IStrategyDetailDao iStrategyDetailDao;

    @Resource
    private IAwardDao iAwardDao;
    @Override
    public DrawResult doDrawExec(DrawReq req) {


        Long strategyId = req.getStrategyId();
        List<StrategyDetail> strategyDetails = iStrategyDetailDao.queryStrategyDetailList(strategyId);
        logger.info("执行开始，策略id是：", req.getStrategyId());
//      初始化  本身就是一个抽奖实体，但是是空的，那么就根据策略信息(策略id，抽奖算法mode，策略明细)初始化实体
        Strategy strategy = iStrategyDao.queryStrategy(strategyId);
        checkAndInitRateData(strategyId, strategy.getStrategyMode(), strategyDetails);

        IDrawAlgorithm drawAlgorithm = drawAlgorithmMap.get(strategy.getStrategyMode());
        String awardId = drawAlgorithm.randomDraw(strategyId, new ArrayList<>());

        Award award = iAwardDao.queryAwardInfo(awardId);
        logger.info("执行策略抽奖完成，中奖用户：{} 奖品ID：{} 奖品名称：{}", req.getuId(), awardId, award.getAwardName());

        return  new DrawResult(req.getuId(), strategyId, awardId, award.getAwardName());


    }
}
