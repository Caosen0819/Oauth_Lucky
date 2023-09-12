package cn.sen.lucky.domain.strategy.service.draw;

import cn.sen.lucky.domain.strategy.model.req.DrawReq;
import cn.sen.lucky.domain.strategy.model.res.DrawResult;

/**
 * @Author caosen
 * @Date 2023/3/28 16:41
 */
public interface IDrawExec {
    DrawResult doDrawExec(DrawReq req);

    DrawResult doDrawExec2(DrawReq req);
}
