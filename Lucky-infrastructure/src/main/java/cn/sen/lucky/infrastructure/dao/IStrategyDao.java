package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author caosen
 * @Date 2023/3/28 13:23
 */
@Mapper
public interface IStrategyDao {

    /**
     * 查询策略配置
     *
     * @param strategyId 策略ID
     * @return           策略配置信息
     */
    Strategy queryStrategy(Long strategyId);

    /**
     * 插入策略配置
     *
     * @param req 策略配置
     */
    void insert(Strategy req);

}
