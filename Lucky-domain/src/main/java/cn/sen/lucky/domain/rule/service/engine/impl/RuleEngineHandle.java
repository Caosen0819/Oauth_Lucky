package cn.sen.lucky.domain.rule.service.engine.impl;

import cn.sen.lucky.domain.rule.model.aggragates.TreeRuleRich;
import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.res.EngineResult;
import cn.sen.lucky.domain.rule.model.vo.TreeNodeVO;
import cn.sen.lucky.domain.rule.repository.IRuleRepository;
import cn.sen.lucky.domain.rule.service.engine.EngineBase;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author caosen
 * @Date 2023/4/4 13:19
 */

@Service("ruleEngineHandle")
public class RuleEngineHandle extends EngineBase {

    @Resource
    private IRuleRepository ruleRepository;

    @Override
    public EngineResult process(DecisionMatterReq matter) {
        /*拿到规则树*/
        TreeRuleRich treeRuleRich = ruleRepository.queryTreeRuleRich(matter.getTreeId());

        if (treeRuleRich == null) {
            throw new RuntimeException("数据库中没有这样等库");
        }

        TreeNodeVO treeNodeVO = this.engineDecisionMaker(treeRuleRich, matter);

        return new EngineResult(matter.getUserId(), treeNodeVO.getTreeId(), treeNodeVO.getTreeNodeId(), treeNodeVO.getNodeValue());

    }
}
