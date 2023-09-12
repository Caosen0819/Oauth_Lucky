package cn.sen.lucky.domain.receive.service.engine.impl;


import cn.sen.lucky.common.constant.AuthConstant;
import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.model.MessageTextEntity;
import cn.sen.lucky.domain.receive.service.logic.LogicFilter;
import cn.sen.lucky.domain.receive.service.engine.EngineBase;
import cn.sen.lucky.infrastructure.utils.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.service.TokenEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 消息处理
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service("msgEngineHandle")
public class MsgEngineHandle extends EngineBase {

    @Value("${wx.config.originalid:gh_89e9e6e0eb59}")
    private String originalId;


    @Override
    public String process(BehaviorMatter request) throws Exception {
        LogicFilter router = super.router(request);
        if (null == router) {
            return null;
        }
        String resultStr = null;
        resultStr = router.filter(request);
        if (StringUtils.isBlank(resultStr)) {
            return "";
        }

        //反馈信息[文本]
        MessageTextEntity res = new MessageTextEntity();
        res.setToUserName(request.getOpenId());
        res.setFromUserName(originalId);
        res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
        res.setMsgType("text");
        res.setContent(resultStr);
        return XmlUtil.beanToXml(res);
    }

}
