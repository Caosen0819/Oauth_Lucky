package cn.sen.lucky.domain.strategy.repository.impl;

import cn.sen.lucky.domain.strategy.model.aggregates.StrategyRich;
import cn.sen.lucky.domain.strategy.repository.IStrategyRepository;
import cn.sen.lucky.infrastructure.dao.IAwardDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDao;
import cn.sen.lucky.infrastructure.dao.IStrategyDetailDao;
import cn.sen.lucky.infrastructure.po.Award;
import cn.sen.lucky.infrastructure.po.Strategy;
import cn.sen.lucky.infrastructure.po.StrategyDetail;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 13:46
 */
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyDetailDao strategyDetailDao;

    @Resource
    private IAwardDao awardDao;

    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {
        Strategy strategy = strategyDao.queryStrategy(strategyId);
        List<StrategyDetail> strategyDetails = strategyDetailDao.queryStrategyDetailList(strategyId);
        return new StrategyRich(strategyId, strategy, strategyDetails);
    }

    @Override
    public Award queryAwardInfo(String awardId) {
        return awardDao.queryAwardInfo(awardId);
    }
}
