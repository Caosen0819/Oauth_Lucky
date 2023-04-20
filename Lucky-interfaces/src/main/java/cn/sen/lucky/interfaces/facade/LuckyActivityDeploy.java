package cn.sen.lucky.interfaces.facade;

import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.sen.lucky.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.sen.lucky.domain.activity.model.vo.ActivityVO;
import cn.sen.lucky.interfaces.assembler.IMapping;
import cn.sen.lucky.process.deploy.IActivityDeployProcess;
import cn.sen.lucky.rpc.activity.deploy.ILuckyActivityDeploy;
import cn.sen.lucky.rpc.activity.deploy.dto.ActivityDTO;
import cn.sen.lucky.rpc.activity.deploy.req.ActivityPageReq;
import cn.sen.lucky.rpc.activity.deploy.res.ActivityRes;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author caosen
 * @Date 2023/4/18 21:54
 */
@Service
public class LuckyActivityDeploy implements ILuckyActivityDeploy {

    private Logger logger = LoggerFactory.getLogger(LuckyActivityBooth.class);

    @Resource
    private IActivityDeployProcess activityDeploy;

    @Resource
    private IMapping<ActivityVO, ActivityDTO> activityMapping;

    @Override
    public ActivityRes queryActivityListByPageForErp(ActivityPageReq req) {
        try {
            logger.info("活动部署分页数据查询开始 erpID：{}", req.getErpId());

            // 1. 包装入参
            ActivityInfoLimitPageReq activityInfoLimitPageReq = new ActivityInfoLimitPageReq(req.getPage(),req.getRows());
            activityInfoLimitPageReq.setActivityId(req.getActivityId());
            activityInfoLimitPageReq.setActivityName(req.getActivityName());

            // 2. 查询结果
            ActivityInfoLimitPageRich activityInfoLimitPageRich = activityDeploy.queryActivityInfoLimitPage(activityInfoLimitPageReq);
            Long count = activityInfoLimitPageRich.getCount();
            List<ActivityVO> activityVOList = activityInfoLimitPageRich.getActivityVOList();

            // 3. 转换对象
            List<ActivityDTO> activityDTOList = activityMapping.sourceToTarget(activityVOList);

            // 4. 封装数据
            ActivityRes activityRes = new ActivityRes(Result.buildSuccessResult());
            activityRes.setCount(count);
            activityRes.setActivityDTOList(activityDTOList);

            logger.info("活动部署分页数据查询完成 erpID：{} count：{}", req.getErpId(), count);

            // 5. 返回结果
            return activityRes;
        } catch (Exception e) {
            logger.error("活动部署分页数据查询失败 erpID：{} reqStr：{}", req.getErpId(), JSON.toJSON(req), e);
            return new ActivityRes(Result.buildErrorResult());
        }
    }

}
