package cn.sen.lucky.process.deploy;

import cn.sen.lucky.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.sen.lucky.domain.activity.model.req.ActivityInfoLimitPageReq;


public interface IActivityDeployProcess {

    /**
     * 查询活动分页查询聚合对象
     *
     * @param req 请求参数；分页、活动
     * @return    查询结果
     */
    ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req);

}
