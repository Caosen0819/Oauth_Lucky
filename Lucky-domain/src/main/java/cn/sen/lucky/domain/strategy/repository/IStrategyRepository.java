package cn.sen.lucky.domain.strategy.repository;

import cn.sen.lucky.domain.strategy.model.aggregates.StrategyRich;
import cn.sen.lucky.infrastructure.po.Award;

/**
 * @Author caosen
 * @Date 2023/3/28 13:46
 */
public interface IStrategyRepository {

    StrategyRich queryStrategyRich(Long strategyId);

    Award queryAwardInfo(String awardId);
}
