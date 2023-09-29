package cn.sen.lucky.domain.rule.service.engine;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.rule.model.aggragates.TreeRuleRich;
import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.res.EngineResult;
import cn.sen.lucky.domain.rule.model.vo.TreeNodeVO;
import cn.sen.lucky.domain.rule.model.vo.TreeRootVO;
import cn.sen.lucky.domain.rule.service.logic.LogicFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static cn.sen.lucky.domain.rule.service.engine.EngineConfig.logicFilterMap;

/**
 * @Author caosen
 * @Date 2023/4/4 12:52
 */
public abstract class EngineBase extends EngineConfig implements EngineFilter{


    private Logger logger = LoggerFactory.getLogger(EngineBase.class);
    /**
     * 这里是base所以为未实现，等子类实现
     * @param matter
     * @return
     */
    @Override
    public EngineResult process(DecisionMatterReq matter) {
        throw new RuntimeException("未实现规则引擎服务");
    }

    protected TreeNodeVO engineDecisionMaker(TreeRuleRich treeRuleRich, DecisionMatterReq matter) {

        TreeRootVO treeRoot = treeRuleRich.getTreeRoot();
        Map<Long, TreeNodeVO> treeNodeMap = treeRuleRich.getTreeNodeMap();

        Long treeRootNodeId = treeRoot.getTreeRootNodeId();
        TreeNodeVO treeNodeVO = treeNodeMap.get(treeRootNodeId);


        /**
         * 循环，找到这个用户应该做的活动
         */
        while (Constants.NodeType.STEM.equals(treeNodeVO.getNodeType())) {
            /*通过决策值拿到规则过滤器*/
            String ruleKey = treeNodeVO.getRuleKey();
            LogicFilter logicFilter = logicFilterMap.get(ruleKey);
            String matterValue = logicFilter.matterValue(matter);
            Long nextNode = logicFilter.filter(matterValue, treeNodeVO.getTreeNodeLineInfoList());
            treeNodeVO = treeNodeMap.get(nextNode);
            logger.info("决策树引擎=>{} userId：{} treeId：{} treeNode：{} ruleKey：{} matterValue：{}", treeRoot.getTreeName(), matter.getUserId(), matter.getTreeId(), treeNodeVO.getTreeNodeId(), ruleKey, matterValue);

        }

        return treeNodeVO;
    }

}
