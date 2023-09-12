package cn.sen.lucky.application;


import cn.sen.lucky.domain.receive.model.BehaviorMatter;

/**
 * @description: 处理接收信息
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
public interface IWxReceiveService {

    /**
     * 接收信息
     *
     * @param behaviorMatter    入参
     * @return                  出惨
     * @throws Exception        异常
     */
    String doReceive(BehaviorMatter behaviorMatter) throws Exception;

}
