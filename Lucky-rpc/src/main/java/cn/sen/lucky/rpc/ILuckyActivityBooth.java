package cn.sen.lucky.rpc;

import cn.sen.lucky.rpc.req.DrawReq;
import cn.sen.lucky.rpc.req.QuantificationDrawReq;
import cn.sen.lucky.rpc.res.DrawRes;

/**
 * @Author caosen
 * @Date 2023/4/4 19:35
 */
public interface ILuckyActivityBooth {

    /**
     * 指定抽奖
     * @param drawReq
     * @return
     */
    DrawRes doDraw(DrawReq drawReq);

    /**
     * 量化人群抽奖
     * @param quantificationDrawReq
     * @return
     */
    DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq);
}
