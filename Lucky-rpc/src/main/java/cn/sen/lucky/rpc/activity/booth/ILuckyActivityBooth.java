package cn.sen.lucky.rpc.activity.booth;


import cn.sen.lucky.rpc.activity.booth.req.DrawReq;
import cn.sen.lucky.rpc.activity.booth.req.QuantificationDrawReq;
import cn.sen.lucky.rpc.activity.booth.res.DrawRes;


public interface ILuckyActivityBooth {

    /**
     * 指定活动抽奖
     * @param drawReq 请求参数
     * @return        抽奖结果
     */
    DrawRes doDraw(DrawReq drawReq);

    /**
     * 量化人群抽奖
     * @param quantificationDrawReq 请求参数
     * @return                      抽奖结果
     */
    DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq);

}
