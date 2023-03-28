package cn.sen.lucky.domain.strategy.service.draw;

import cn.sen.lucky.domain.strategy.model.vo.AwardRateInfo;
import cn.sen.lucky.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.sen.lucky.infrastructure.po.Strategy;
import cn.sen.lucky.infrastructure.po.StrategyDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 18:02
 */
public class DrawBase extends DrawConfig{

    public void checkAndInitRateData(Long strategyId, Integer strategyMode, List<StrategyDetail> strategyDetailList) {

//        这里有问题，可以自己修改这段代码觉得了必须得1，但是我们可以加上自己的逻辑
        if (strategyMode != 1) { return; }
        IDrawAlgorithm drawAlgorithm = drawAlgorithmMap.get(strategyMode);

//
        boolean existRateTuple = drawAlgorithm.isExistRateTuple(strategyId);
        if (existRateTuple) { return; }

        List<AwardRateInfo> awardRateInfoList = new ArrayList<>(strategyDetailList.size());

//        一个策略id其实对应多个策略，因为有多个奖品每个奖品都有一个概率，所以下面要从策略id中得到奖品类型和其概率

        for (StrategyDetail strategyDetail : strategyDetailList) {
            awardRateInfoList.add(new AwardRateInfo(strategyDetail.getAwardId(), strategyDetail.getAwardRate()));

        }
//        到这里已经得到了奖品信息
//        下面就要初始化算法了
        drawAlgorithm.initRateTuple(strategyId, awardRateInfoList);

    }
}
