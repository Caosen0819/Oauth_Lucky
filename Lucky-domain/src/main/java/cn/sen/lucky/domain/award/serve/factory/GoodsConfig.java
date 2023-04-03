package cn.sen.lucky.domain.award.serve.factory;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
import cn.sen.lucky.domain.award.serve.goods.impl.CouponGoods;
import cn.sen.lucky.domain.award.serve.goods.impl.DescGoods;
import cn.sen.lucky.domain.award.serve.goods.impl.PhysicalGoods;
import cn.sen.lucky.domain.award.serve.goods.impl.RedeemCodeGoods;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author caosen
 * @Date 2023/3/30 17:03
 */
public class GoodsConfig {
    protected static Map<Integer, IDistributionGoods> goodsMap = new ConcurrentHashMap<>();

    @Resource
    private DescGoods descGoods;

    @Resource
    private RedeemCodeGoods redeemCodeGoods;

    @Resource
    private CouponGoods couponGoods;

    @Resource
    private PhysicalGoods physicalGoods;

    @PostConstruct
    public void init() {
        goodsMap.put(Constants.AwardType.DESC.getCode(), descGoods);
        goodsMap.put(Constants.AwardType.RedeemCodeGoods.getCode(), redeemCodeGoods);
        goodsMap.put(Constants.AwardType.CouponGoods.getCode(), couponGoods);
        goodsMap.put(Constants.AwardType.PhysicalGoods.getCode(), physicalGoods);
    }
}
