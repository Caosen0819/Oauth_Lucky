package cn.sen.lucky.domain.rule.service.logic.impl;

import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.service.logic.BaseLogic;
import org.springframework.stereotype.Component;

/**
 * @Author caosen
 * @Date 2023/4/4 12:38
 */
@Component
public class UserGenderFilter extends BaseLogic {

    @Override
    public String matterValue(DecisionMatterReq decisionMatter) {
        return decisionMatter.getValMap().get("gender").toString();
    }

}