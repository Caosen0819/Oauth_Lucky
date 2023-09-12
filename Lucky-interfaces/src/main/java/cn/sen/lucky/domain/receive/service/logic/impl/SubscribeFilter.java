package cn.sen.lucky.domain.receive.service.logic.impl;


import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.service.logic.LogicFilter;
import org.springframework.stereotype.Service;

/**
 * @description: 关注微信公众号
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service("subscribe")
public class SubscribeFilter implements LogicFilter {

    @Override
    public String filter(BehaviorMatter request) {
        return "感谢关注，快来回复抽奖，参与森林有秘密的活动吧！";
    }

}
