package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 13:23
 */
@Mapper
public interface IAwardDao {

    /**
     * 查询奖品信息
     *
     * @param awardId 奖品ID
     * @return        奖品信息
     */
    Award queryAwardInfo(String awardId);

    /**
     * 插入奖品配置
     *
     * @param list 奖品配置
     */
    void insertList(List<Award> list);

}