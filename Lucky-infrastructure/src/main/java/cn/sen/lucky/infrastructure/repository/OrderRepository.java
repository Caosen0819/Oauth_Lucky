package cn.sen.lucky.infrastructure.repository;

import cn.sen.lucky.domain.award.repository.IOrderRepository;
import cn.sen.lucky.infrastructure.dao.IUserStrategyExportDao;
import cn.sen.lucky.infrastructure.po.UserStrategyExport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description: 奖品表仓储服务
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IUserStrategyExportDao userStrategyExportDao;

    @Override
    public void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId(uId);
        userStrategyExport.setOrderId(orderId);
        userStrategyExport.setAwardId(awardId);
        userStrategyExport.setGrantState(grantState);
        userStrategyExportDao.updateUserAwardState(userStrategyExport);
    }
}
