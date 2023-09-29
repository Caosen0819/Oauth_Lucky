package cn.sen.lucky.infrastructure.dao;

import cn.sen.lucky.infrastructure.po.UserTakeActivityCount;
import cn.sen.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 用户活动参与次数表Dao

 */
@Mapper
public interface IUserTakeActivityCountDao {

    /**
     * 查询用户领取次数信息
     * @param userTakeActivityCountReq 请求入参【活动号、用户ID】
     * @return 领取结果
     */
    @DBRouter
    UserTakeActivityCount queryUserTakeActivityCount(UserTakeActivityCount userTakeActivityCountReq);

    /**
     * 插入领取次数信息
     * @param userTakeActivityCount 请求入参
     */
//    @DBRouter
    void insert(UserTakeActivityCount userTakeActivityCount);

    /**
     * 更新领取次数信息
     * @param userTakeActivityCount 请求入参
     * @return 更新数量
     */
//    @DBRouter
    int updateLeftCount(UserTakeActivityCount userTakeActivityCount);

    /**
     * 通过redis更新次数
     * @param uId
     * @param leftCount
     * @return
     */
//    @DBRouter
    int updateLeftCountByRedis(String uId, int leftCount);

    int queryUserAndLeftCount(String uId);

}
