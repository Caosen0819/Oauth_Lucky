package cn.sen.lucky.domain.rule.service.engine;

import cn.sen.lucky.domain.rule.service.logic.LogicFilter;
import cn.sen.lucky.domain.rule.service.logic.impl.UserAgeFilter;
import cn.sen.lucky.domain.rule.service.logic.impl.UserGenderFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则配置
 * @Author caosen
 * @Date 2023/4/4 12:53
 */
public class EngineConfig {

    public static Map<String, LogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    @Resource
    private UserAgeFilter userAgeFilter;

    @Resource
    private UserGenderFilter userGenderFilter;

    @PostConstruct
    public void init() {
        logicFilterMap.put("userAge", userAgeFilter);
        logicFilterMap.put("userGender", userGenderFilter);

    }
}
