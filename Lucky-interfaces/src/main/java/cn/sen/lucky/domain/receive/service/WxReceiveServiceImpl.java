package cn.sen.lucky.domain.receive.service;


import cn.sen.lucky.application.IWxReceiveService;
import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.service.engine.Engine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 处理接收信息
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service
public class WxReceiveServiceImpl implements IWxReceiveService {

    @Resource(name = "msgEngineHandle")
    private Engine msgEngineHandle;

    @Override
    public String doReceive(BehaviorMatter behaviorMatter) throws Exception {
        return msgEngineHandle.process(behaviorMatter);
    }

}
