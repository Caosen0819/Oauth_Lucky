package cn.sen.lucky.domain.award.serve.goods;

import cn.sen.lucky.domain.award.model.req.GoodsReq;
import cn.sen.lucky.domain.award.model.res.DistributionRes;
import cn.sen.lucky.domain.award.repository.IAwardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author caosen
 * @Date 2023/3/30 16:34
 */
public class DistributionBase {

    protected Logger logger = LoggerFactory.getLogger(DistributionBase.class);


    private IAwardRepository awardRepository;

    protected void updateUserAwardState(String uId, String orderId, String awardId, Integer awardState, String awardStateInfo) {
        // TODO 后期添加更新分库分表中，用户个人的抽奖记录表中奖品发奖状态
        logger.info("TODO 后期添加更新分库分表中，用户个人的抽奖记录表中奖品发奖状态 uId：{}", uId);
    }
}
