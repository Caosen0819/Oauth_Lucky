package cn.sen.lucky.domain.receive.service.logic.impl;

import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.service.logic.LogicFilter;
import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description: 取消关注微信公众号
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service("unsubscribe")
public class UnsubscribeFilter implements LogicFilter {

    private Logger logger = LoggerFactory.getLogger(UnsubscribeFilter.class);

    @Override
    public String filter(BehaviorMatter request) {
        logger.info("用户{}已取消关注", request.getOpenId());
        return null;
    }

}
