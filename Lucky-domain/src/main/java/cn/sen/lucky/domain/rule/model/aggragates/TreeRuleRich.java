package cn.sen.lucky.domain.rule.model.aggragates;

import cn.sen.lucky.domain.rule.model.vo.TreeNodeVO;
import cn.sen.lucky.domain.rule.model.vo.TreeRootVO;

import java.util.Map;

/**
 * 规则树集合，用的map结构，具体规则还需以来其他类
 * @Author caosen
 * @Date 2023/4/4 12:14
 */
public class TreeRuleRich {

    /** 树根信息 */
    private TreeRootVO treeRoot;
    /** 树节点ID -> 子节点 */
    private Map<Long, TreeNodeVO> treeNodeMap;

    public TreeRootVO getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(TreeRootVO treeRoot) {
        this.treeRoot = treeRoot;
    }

    public Map<Long, TreeNodeVO> getTreeNodeMap() {
        return treeNodeMap;
    }

    public void setTreeNodeMap(Map<Long, TreeNodeVO> treeNodeMap) {
        this.treeNodeMap = treeNodeMap;
    }
}
