package cn.sen.lucky.domain.strategy.service.algorithm.impl;

import cn.sen.lucky.domain.strategy.model.vo.AwardRateInfo;
import cn.sen.lucky.domain.strategy.service.algorithm.BaseAlgorithm;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 15:45
 * 必中奖策略抽奖，排掉已经中奖的概率，重新计算中奖范围
 */
public class DefaultRateRandomDrawAlgorithm extends BaseAlgorithm {

    BigDecimal newDenominator = BigDecimal.ZERO;

    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {

        List<AwardRateInfo> awardRateInfoList = awardRateInfoMap.get(strategyId);

        List<AwardRateInfo> newAwardRateInfos = new ArrayList<AwardRateInfo>();
//         for 找没抽完的
        for (AwardRateInfo info: awardRateInfoList) {
            if (excludeAwardIds.contains(info.getAwardId())) {
                newAwardRateInfos.add(info);
                newDenominator = newDenominator.add(info.getAwardRate());
            } else {
                continue;
            }


        }
        // 前置判断
        if (newAwardRateInfos.size() == 0) {
            return "";
        }
        if (newAwardRateInfos.size() == 1) {
            return newAwardRateInfos.get(0).getAwardId();
        }
        // 获取随机概率值
        SecureRandom secureRandom = new SecureRandom();
        int randomVal = secureRandom.nextInt(100) + 1;

        int cur = 0;
        String awardId = "未中奖";
//        找到后，可以通过一个循环判断在哪个区间内
        for (AwardRateInfo info: newAwardRateInfos
             ) {
            int rateVal = info.getAwardRate().divide(newDenominator, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).intValue();
            //目前的index
            int index = hashIdx(rateVal);
            if ( index > cur && index <= (rateVal + cur)) {
                awardId = info.getAwardId();
                break;
            }
            else {
                cur += rateVal;
            }
        }
        return awardId;
    }
}
