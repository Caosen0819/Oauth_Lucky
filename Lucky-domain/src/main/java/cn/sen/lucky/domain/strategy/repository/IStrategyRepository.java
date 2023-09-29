package cn.sen.lucky.domain.strategy.repository;

import cn.sen.lucky.domain.activity.model.vo.StrategyVO;
import cn.sen.lucky.domain.strategy.model.aggregates.StrategyRich;
import cn.sen.lucky.domain.strategy.model.vo.AwardBriefVO;
import cn.sen.lucky.domain.strategy.model.vo.StrategyDetailBriefVO;

import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 13:46
 */
public interface IStrategyRepository {

    StrategyRich queryStrategyRich(Long strategyId);
    StrategyRich queryStrategyRich2(Long strategyId);
    AwardBriefVO queryAwardInfo(String awardId);

    List<String> queryNoStockStrategyAwardList(Long strategyId);

    List<StrategyVO> queryAllStrategy();

    List<StrategyDetailBriefVO> queryStrategyDetailList(Long strategyId);

    /**
     * 扣减库存
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return           扣减结果
     */
    boolean deductStock(Long strategyId, String awardId);

    int deductStockByRedis(Long strategyId, String awardId);

    int updateAwardCount(String awardId, int awardCount);

}
