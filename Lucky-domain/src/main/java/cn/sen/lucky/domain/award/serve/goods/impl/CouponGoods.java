package cn.sen.lucky.domain.award.serve.goods.impl;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.award.model.req.GoodsReq;
import cn.sen.lucky.domain.award.model.res.DistributionRes;
import cn.sen.lucky.domain.award.serve.goods.DistributionBase;
import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
import org.springframework.stereotype.Component;

/**
 * @Author caosen
 * @Date 2023/3/30 16:37
 */
@Component
public class CouponGoods extends DistributionBase implements IDistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq goodsReq) {
        logger.info("优惠卷发放接口");
        super.updateUserAwardState(goodsReq.getuId(), goodsReq.getOrderId(), goodsReq.getAwardId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
        return new DistributionRes(goodsReq.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());

    }

    @Override
    public Integer getDistributionGoodsName() {
        return Constants.AwardType.CouponGoods.getCode();
    }
}
