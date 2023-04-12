package cn.sen.lucky.domain.rule.service.engine;

import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.res.EngineResult;

/**
 * 引擎接口
 * @Author caosen
 * @Date 2023/4/4 12:50
 */
public interface EngineFilter {

    public EngineResult process(final DecisionMatterReq matterReq);
}
