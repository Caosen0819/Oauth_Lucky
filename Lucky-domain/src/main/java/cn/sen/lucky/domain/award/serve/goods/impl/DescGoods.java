package cn.sen.lucky.domain.award.serve.goods.impl;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.award.model.req.GoodsReq;
import cn.sen.lucky.domain.award.model.res.DistributionRes;
import cn.sen.lucky.domain.award.serve.goods.DistributionBase;
import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
import org.springframework.stereotype.Component;

/**
 * @Author caosen
 * @Date 2023/3/30 16:46
 */
@Component
public class DescGoods extends DistributionBase implements IDistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        super.updateUserAwardState(req.getuId(), req.getOrderId(), req.getAwardId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());

        return new DistributionRes(req.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

    @Override
    public Integer getDistributionGoodsName() {
        return Constants.AwardType.DESC.getCode();
    }

}