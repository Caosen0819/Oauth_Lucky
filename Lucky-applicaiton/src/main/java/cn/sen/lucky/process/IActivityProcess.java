package cn.sen.lucky.process;

import cn.sen.lucky.common.Result;
import cn.sen.lucky.domain.activity.model.vo.DrawOrderVO;
import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.process.req.DrawProcessReq;
import cn.sen.lucky.process.res.DrawProcessResult;
import cn.sen.lucky.process.res.RuleQuantificationCrowdResult;

/**
 * @Description 活动抽奖流程编排接口
 * @Author caosen
 * @Date 2023/4/3 21:39
 */
public interface IActivityProcess {

    DrawProcessResult doDrawProcess(DrawProcessReq req);

    RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req);
}
