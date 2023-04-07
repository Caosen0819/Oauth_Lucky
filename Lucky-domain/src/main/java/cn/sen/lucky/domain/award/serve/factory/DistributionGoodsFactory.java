package cn.sen.lucky.domain.award.serve.factory;

import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static cn.sen.lucky.domain.award.serve.factory.GoodsConfig.goodsMap;


/**
 * 配送商品简单工厂，提供获取配送服务
 * @Author caosen
 * @Date 2023/3/30 17:04
 */
@Service
public class DistributionGoodsFactory extends GoodsConfig {
    public IDistributionGoods getDistributionGoodsService(Integer awardType){
        return goodsMap.get(awardType);
    }
}
