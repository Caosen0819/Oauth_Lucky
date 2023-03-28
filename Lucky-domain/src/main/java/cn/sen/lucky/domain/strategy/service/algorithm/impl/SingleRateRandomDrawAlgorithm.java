package cn.sen.lucky.domain.strategy.service.algorithm.impl;

import cn.sen.lucky.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 15:25
 * 【推荐】单项随机概率抽奖，抽到一个已经排掉的奖品则未中奖
 */
@Component("singleRateRandomDrawAlgorithm")
public class SingleRateRandomDrawAlgorithm extends BaseAlgorithm {



    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        String[] rateTuple = super.rateTupleMap.get(strategyId);
        assert rateTuple != null;
        int randomVal = new SecureRandom().nextInt(100) + 1;
        int idx = super.hashIdx(randomVal);
        // 返回结果
        String awardId = rateTuple[idx];
        if (excludeAwardIds.contains(awardId)) {
            return "未中奖";
        }
        return awardId;
    }
}
