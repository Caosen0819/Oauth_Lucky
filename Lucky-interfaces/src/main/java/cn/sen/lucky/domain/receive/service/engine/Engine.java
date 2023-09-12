package cn.sen.lucky.domain.receive.service.engine;

import cn.sen.lucky.domain.receive.model.BehaviorMatter;
/**
 * @description: 消息引擎接口
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
public interface Engine {

    /**
     * 过滤器
     * @param request       入参
     * @return              出参
     * @throws Exception    异常
     */
    String process(final BehaviorMatter request) throws Exception;

}
