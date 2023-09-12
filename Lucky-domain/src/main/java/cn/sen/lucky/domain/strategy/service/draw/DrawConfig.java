package cn.sen.lucky.domain.strategy.service.draw;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.strategy.service.algorithm.BaseAlgorithm;
import cn.sen.lucky.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.sen.lucky.domain.strategy.service.algorithm.impl.DefaultRateRandomDrawAlgorithm;
import cn.sen.lucky.domain.strategy.service.algorithm.impl.SingleRateRandomDrawAlgorithm;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author caosen
 * @Date 2023/3/28 16:40
 * 配置类，因为项目启动要知道所有的算法，才能在别人需要某个算法的时候给他加载，
 * 所以这个就是把你的算法加载到项目里面，然后用static标识
 */
public class DrawConfig {


    @Resource
    private IDrawAlgorithm entiretyRateRandomDrawAlgorithm;

    @Resource
    private IDrawAlgorithm singleRateRandomDrawAlgorithm;



    public static Map<Integer, IDrawAlgorithm> drawAlgorithmGroup = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        drawAlgorithmGroup.put(Constants.StrategyMode.ENTIRETY.getCode(), entiretyRateRandomDrawAlgorithm);
        drawAlgorithmGroup.put(Constants.StrategyMode.SINGLE.getCode(), singleRateRandomDrawAlgorithm);
    }
}
