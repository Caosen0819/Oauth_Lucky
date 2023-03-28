package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.StrategyDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author caosen
 * @Date 2023/3/28 13:23
 */
@Mapper
public interface IStrategyDetailDao {

    List<StrategyDetail> queryStrategyDetailList(Long strategyId);

}
