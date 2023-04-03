package cn.sen.lucky.domain.activity.service.deploy;

import cn.sen.lucky.domain.activity.model.req.ActivityConfigReq;

/**
 * @Author caosen
 * @Date 2023/3/30 22:44
 */
public interface IActivityDeploy {
    /**
     * 创建活动信息
     *
     * @param req 活动配置信息
     */
    void createActivity(ActivityConfigReq req);

    /**
     * 修改活动信息
     *
     * @param req 活动配置信息
     */
    void updateActivity(ActivityConfigReq req);
}
