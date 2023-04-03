package cn.sen.lucky.domain.activity.service.partake;

import cn.sen.lucky.domain.activity.model.req.PartakeReq;
import cn.sen.lucky.domain.activity.model.vo.ActivityBillVO;
import cn.sen.lucky.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;

/**
 * @description: 活动领取模操作，一些通用的数据服务
 * @Author caosen
 * @Date 2023/4/3 10:30
 */
public class ActivityPartakeSupport {

    @Resource
    protected IActivityRepository activityRepository;

    protected ActivityBillVO queryActivityBill(PartakeReq req){
        return activityRepository.queryActivityBill(req);
    }

}