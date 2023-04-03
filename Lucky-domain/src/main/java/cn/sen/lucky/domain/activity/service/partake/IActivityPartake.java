package cn.sen.lucky.domain.activity.service.partake;

import cn.sen.lucky.domain.activity.model.req.PartakeReq;
import cn.sen.lucky.domain.activity.model.res.PartakeResult;

/**
 * @Author caosen
 * @Date 2023/3/30 23:17
 */
public interface IActivityPartake {
    PartakeResult doPartake(PartakeReq req);
}
