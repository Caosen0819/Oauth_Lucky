package cn.sen.lucky.domain.rule.service.logic;

import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * @Author caosen
 * @Date 2023/4/4 12:26
 */
public interface LogicFilter {

    /**
     * 逻辑决策器
     * @param matterValue          决策值
     * @param treeNodeLineInfoList 决策节点
     * @return                     下一个节点Id
     */
    Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineInfoList);

    /**
     * 获取决策值
     *
     * @param decisionMatter 决策物料
     * @return               决策值
     */
    String matterValue(DecisionMatterReq decisionMatter);

}
