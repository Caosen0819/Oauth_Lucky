package cn.sen.lucky.rpc.activity.deploy;

import cn.sen.lucky.rpc.activity.deploy.req.ActivityPageReq;
import cn.sen.lucky.rpc.activity.deploy.res.ActivityRes;

public interface ILuckyActivityDeploy {

    /**
     * 通过分页查询活动列表信息，给ERP运营使用
     *
     * @param req   查询参数
     * @return      查询结果
     */
    ActivityRes queryActivityListByPageForErp(ActivityPageReq req);

}
