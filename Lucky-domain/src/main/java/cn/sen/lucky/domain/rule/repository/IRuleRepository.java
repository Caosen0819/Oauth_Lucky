package cn.sen.lucky.domain.rule.repository;

import cn.sen.lucky.domain.rule.model.aggragates.TreeRuleRich;

/**
 * @Author caosen
 * @Date 2023/4/4 12:25
 */
public interface IRuleRepository {

    /**
     * 查询规则决策树配置
     *
     * @param treeId    决策树ID
     * @return          决策树配置
     */
    TreeRuleRich queryTreeRuleRich(Long treeId);

}
