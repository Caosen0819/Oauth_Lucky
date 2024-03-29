package cn.sen.lucky.domain.strategy.service.draw;

import cn.sen.lucky.domain.strategy.model.aggregates.StrategyRich;
import cn.sen.lucky.domain.strategy.model.req.DrawReq;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;
import cn.sen.lucky.domain.strategy.model.vo.*;
import cn.sen.lucky.domain.strategy.repository.IStrategyRepository;
import cn.sen.lucky.domain.strategy.service.algorithm.IDrawAlgorithm;

import cn.sen.lucky.domain.util.RedisUti2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.sen.lucky.common.Constants;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author caosen
 * @Date 2023/3/28 20:59
 * 定义抽象抽奖过程，模板模式
 * 这里还有策略模式，（枚举类+map）
 */
public abstract class AbstractDrawBase extends DrawStrategySupport implements IDrawExec {

    private Logger logger = LoggerFactory.getLogger(AbstractDrawBase.class);

    @Resource
    private RedisUti2 redisUti2;

    @Override
    public DrawResult doDrawExec(DrawReq req) {
        // 1. 获取抽奖策略
        StrategyRich strategyRich = super.queryStrategyRich(req.getStrategyId());
        StrategyBriefVO strategy = strategyRich.getStrategy();

        // 2. 校验抽奖策略是否已经初始化到内存
        this.checkAndInitRateData(req.getStrategyId(), strategy.getStrategyMode(), strategyRich.getStrategyDetailList());

        // 3. 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等
//        List<String> excludeAwardIds = this.queryExcludeAwardIds(req.getStrategyId());
        /**
         * 3. 此处依旧可以优化，原本第三步是查询无库存策略奖品的ID，那如果我们把库存奖品的数量配合id直接就直接放到redis里面，
         * 那是不是可以提高速度？ 我们奖品的字段在redis的key ： 策略id + 奖品id + stock
         * 优化方式 : redis + hashmap
          */
        List<String> excludeAwardIds = this.queryExcludeAwardIdsByRedis(req.getStrategyId());


        // 4. 执行抽奖算法
        String awardId = this.drawAlgorithm(req.getStrategyId(), drawAlgorithmGroup.get(strategy.getStrategyMode()), excludeAwardIds);

        // 5. 包装中奖结果
        return buildDrawResult(req.getuId(), req.getStrategyId(), awardId, strategy);
    }

    @Override
    public DrawResult doDrawExec2(DrawReq req) {
        // 1. 获取抽奖策略
        StrategyRich strategyRich = super.queryStrategyRich(req.getStrategyId());
        StrategyBriefVO strategy = strategyRich.getStrategy();

        // 2. 校验抽奖策略是否已经初始化到内存
        this.checkAndInitRateData(req.getStrategyId(), strategy.getStrategyMode(), strategyRich.getStrategyDetailList());

        // 3. 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等
        // 优化成了byredis，如果想用以前的数据库，那么去掉方法名字后面的ByRedis
        List<String> excludeAwardIds = this.queryExcludeAwardIdsByRedis(req.getStrategyId());

        // 4. 执行抽奖算法
        String awardId = this.drawAlgorithm(req.getStrategyId(), drawAlgorithmGroup.get(strategy.getStrategyMode()), excludeAwardIds);

        // 5. 包装中奖结果
        return buildDrawResult(req.getuId(), req.getStrategyId(), awardId, strategy);
    }

    protected List<String> queryExcludeAwardIdsByRedis(Long strategyId) {
        String hashkey = strategyId + "_Awards";
        Map<Object, Object> hmget = redisUti2.hmget(hashkey);
        List<String> excludeAwardIds = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : hmget.entrySet()) {
            if ((Integer) entry.getValue() == 0) {
                excludeAwardIds.add((String) entry.getKey());
            }
        }
        return excludeAwardIds;
    }

    /**
     * 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等，这类数据是含有业务逻辑的，所以需要由具体的实现方决定
     *
     * @param strategyId 策略ID
     * @return 排除的奖品ID集合
     */
    protected abstract List<String> queryExcludeAwardIds(Long strategyId);


    /**
     * 执行抽奖算法
     *
     * @param strategyId      策略ID
     * @param drawAlgorithm   抽奖算法模型
     * @param excludeAwardIds 排除的抽奖ID集合
     * @return 中奖奖品ID
     */
    protected abstract String drawAlgorithm(Long strategyId, IDrawAlgorithm drawAlgorithm, List<String> excludeAwardIds);

    /**
     * 校验抽奖策略是否已经初始化到内存
     *
     * @param strategyId         抽奖策略ID
     * @param strategyMode       抽奖策略模式
     * @param strategyDetailList 抽奖策略详情
     */
    private void checkAndInitRateData(Long strategyId, Integer strategyMode, List<StrategyDetailBriefVO> strategyDetailList) {

        // 非单项概率，不必存入缓存
//        if (!Constants.StrategyMode.SINGLE.getCode().equals(strategyMode)) {
//            return;
//        }
// 根据抽奖策略模式，获取对应的抽奖服务
        IDrawAlgorithm drawAlgorithm = drawAlgorithmGroup.get(strategyMode);

        // 已初始化过的数据，不必重复初始化
        if (drawAlgorithm.isExistRateTuple(strategyId)) {
            return;
        }

        // 解析并初始化中奖概率数据到散列表
        List<AwardRateInfo> awardRateInfoList = new ArrayList<>(strategyDetailList.size());
        for (StrategyDetailBriefVO strategyDetail : strategyDetailList) {
            awardRateInfoList.add(new AwardRateInfo(strategyDetail.getAwardId(), strategyDetail.getAwardRate()));
        }

        drawAlgorithm.initRateTuple(strategyId, awardRateInfoList);

    }

    /**
     * 包装抽奖结果
     *
     * @param uId        用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID，null 情况：并发抽奖情况下，库存临界值1 -> 0，会有用户中奖结果为 null
     * @return 中奖结果
     */
    private DrawResult buildDrawResult(String uId, Long strategyId, String awardId, StrategyBriefVO strategy) {
        if (null == awardId) {
            logger.info("执行策略抽奖完成【未中奖】，用户：{} 策略ID：{}", uId, strategyId);
            return new DrawResult(uId, strategyId, Constants.DrawState.FAIL.getCode());
        }

        AwardBriefVO award = super.queryAwardInfoByAwardId(awardId);
        DrawAwardVO drawAwardInfo = new DrawAwardVO(uId, award.getAwardId(), award.getAwardType(), award.getAwardName(), award.getAwardContent());
        drawAwardInfo.setStrategyMode(strategy.getStrategyMode());
        drawAwardInfo.setGrantType(strategy.getGrantType());
        drawAwardInfo.setGrantDate(strategy.getGrantDate());
//        logger.info("执行策略抽奖完成【已中奖】，用户：{} 策略ID：{} 奖品ID：{} 奖品名称：{}", uId, strategyId, awardId, award.getAwardName());

        return new DrawResult(uId, strategyId, Constants.DrawState.SUCCESS.getCode(), drawAwardInfo);
    }

}