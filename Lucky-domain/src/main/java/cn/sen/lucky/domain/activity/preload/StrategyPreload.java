package cn.sen.lucky.domain.activity.preload;

import cn.sen.lucky.domain.activity.model.vo.ActivityVO;
import cn.sen.lucky.domain.activity.model.vo.StrategyDetailVO;
import cn.sen.lucky.domain.activity.model.vo.StrategyVO;
import cn.sen.lucky.domain.activity.repository.IActivityRepository;
import cn.sen.lucky.domain.strategy.model.vo.StrategyBriefVO;
import cn.sen.lucky.domain.strategy.model.vo.StrategyDetailBriefVO;
import cn.sen.lucky.domain.strategy.repository.IStrategyRepository;
import cn.sen.lucky.domain.util.RedisUti2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/9/11 11:06
 */
@Component
public class StrategyPreload {
    @Resource
    private IStrategyRepository strategyRepository;

    @Resource
    public RedisUti2 redisUtil2;
    @PostConstruct
    private void init(){
        // 得到活动存入redis
        List<StrategyVO> strategyList = strategyRepository.queryAllStrategy();
        for (StrategyVO strategy : strategyList) {
            String Strategykey = "Strategy_" + strategy.getStrategyId();
            redisUtil2.set(Strategykey,strategy);
            List<StrategyDetailBriefVO> strategyDetailList = strategyRepository.queryStrategyDetailList(strategy.getStrategyId());
            HashMap<String, Object> hashMap = new HashMap<>();
            for (StrategyDetailBriefVO vo : strategyDetailList) {
                String awardId = vo.getAwardId();
                Integer awardSurplusCount = vo.getAwardSurplusCount();
                hashMap.put(awardId, awardSurplusCount);
            }
            String strateyAward = strategy.getStrategyId() + "_Awards";
            redisUtil2.hmset(strateyAward, hashMap, -1);
            String strategyDetailKey = "StrategyDetail_" + strategy.getStrategyId();
            redisUtil2.lSet3(strategyDetailKey, strategyDetailList,-1);

        }
    }
}
