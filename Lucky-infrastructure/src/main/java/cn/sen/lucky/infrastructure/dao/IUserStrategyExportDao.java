package cn.sen.lucky.infrastructure.dao;


import cn.sen.lucky.infrastructure.po.UserStrategyExport;

import cn.sen.middleware.db.router.annotation.DBRouter;
import cn.sen.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 用户策略计算结果表DAO

 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserStrategyExportDao {

    /**
     * 新增数据
     * @param userStrategyExport 用户策略
     */
    @DBRouter(Key = "uId")
    void insert(UserStrategyExport userStrategyExport);

    /**
//     * 查询数据
     * @param uId 用户ID
     * @return 用户策略
     */
    @DBRouter
    UserStrategyExport queryUserStrategyExportByUId(String uId);



}
