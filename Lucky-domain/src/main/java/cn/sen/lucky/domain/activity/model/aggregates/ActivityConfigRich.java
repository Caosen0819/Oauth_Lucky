package cn.sen.lucky.domain.activity.model.aggregates;

import cn.sen.lucky.domain.activity.model.vo.ActivityVO;
import cn.sen.lucky.domain.activity.model.vo.AwardVO;
import cn.sen.lucky.domain.activity.model.vo.StrategyVO;


import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/30 22:32
 */
public class ActivityConfigRich {

    /*活动*/
    private ActivityVO activity;

    /*政策*/
    private StrategyVO strategy;

    /**//*奖品*/
    private List<AwardVO> awardList;
    public ActivityConfigRich() {
    }

    public ActivityConfigRich(ActivityVO activity, StrategyVO strategy, List<AwardVO> awardList) {
        this.activity = activity;
        this.strategy = strategy;
        this.awardList = awardList;
    }

    public ActivityVO getActivity() {
        return activity;
    }

    public void setActivity(ActivityVO activity) {
        this.activity = activity;
    }

    public StrategyVO getStrategy() {
        return strategy;
    }

    public void setStrategy(StrategyVO strategy) {
        this.strategy = strategy;
    }

    public List<AwardVO> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<AwardVO> awardList) {
        this.awardList = awardList;
    }
}
