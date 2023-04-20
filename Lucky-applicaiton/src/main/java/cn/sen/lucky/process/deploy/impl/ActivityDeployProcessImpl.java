package cn.sen.lucky.process.deploy.impl;

import cn.sen.lucky.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.sen.lucky.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.sen.lucky.domain.activity.service.deploy.IActivityDeploy;
import cn.sen.lucky.process.deploy.IActivityDeployProcess;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 活动部署实现
 */
@Service
public class ActivityDeployProcessImpl implements IActivityDeployProcess {

    @Resource
    private IActivityDeploy activityDeploy;

    @Override
    public ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req) {
        return activityDeploy.queryActivityInfoLimitPage(req);
    }
}
