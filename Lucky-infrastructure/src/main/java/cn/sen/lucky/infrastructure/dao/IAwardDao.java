package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.Award;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author caosen
 * @Date 2023/3/28 13:23
 */
@Mapper
public interface IAwardDao {

    Award queryAwardInfo(String awardId);

}
