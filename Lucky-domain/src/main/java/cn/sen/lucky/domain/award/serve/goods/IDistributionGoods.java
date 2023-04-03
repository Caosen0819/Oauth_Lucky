package cn.sen.lucky.domain.award.serve.goods;

import cn.sen.lucky.domain.award.model.req.GoodsReq;
import cn.sen.lucky.domain.award.model.res.DistributionRes;

/**
 * @Author caosen
 * @Date 2023/3/30 16:32
 */
public interface IDistributionGoods {
    /**
     *
     * @param goodsReq 配送信息
     * @return 配送结果的信息比如订单号啊这类
     */
    DistributionRes doDistribution(GoodsReq goodsReq);

    Integer getDistributionGoodsName();

}
