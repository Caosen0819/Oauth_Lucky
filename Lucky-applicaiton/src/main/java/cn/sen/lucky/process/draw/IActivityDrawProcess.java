package cn.sen.lucky.process.draw;


import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.process.draw.req.DrawProcessReq;
import cn.sen.lucky.process.draw.res.DrawProcessResult;
import cn.sen.lucky.process.draw.res.RuleQuantificationCrowdResult;

public interface IActivityDrawProcess {

    /**
     * 执行抽奖流程
     * @param req 抽奖请求
     * @return    抽奖结果
     */
    DrawProcessResult doDrawProcess(DrawProcessReq req);

    /**
     * 规则量化人群，返回可参与的活动ID
     * @param req   规则请求
     * @return      量化结果，用户可以参与的活动ID
     */
    RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req);

}
