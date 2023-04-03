package cn.sen.lucky.infrastructure.dao;


import cn.sen.lucky.infrastructure.po.UserTakeActivity;

import cn.sen.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IUserTakeActivityDao {

    /**
     * 插入用户领取活动信息
     *
     * @param userTakeActivity 入参
     */
    @DBRouter(Key = "uId")
    void insert(UserTakeActivity userTakeActivity);

}
