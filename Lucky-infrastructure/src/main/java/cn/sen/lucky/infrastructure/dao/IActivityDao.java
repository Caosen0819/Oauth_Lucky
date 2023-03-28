package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.Activity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author caosen
 * @Date 2023/3/28 13:23
 */
@Mapper
public interface IActivityDao {

   void insert(Activity req);

   Activity queryActivityById(Long activityId);

}
