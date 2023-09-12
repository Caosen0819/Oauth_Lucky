package cn.sen.lucky.domain.receive.service.logic.impl;


import cn.sen.lucky.domain.receive.model.BehaviorMatter;
import cn.sen.lucky.domain.receive.service.logic.LogicFilter;
import cn.sen.lucky.rpc.activity.booth.ILuckyActivityBooth;
import cn.sen.lucky.rpc.activity.booth.dto.AwardDTO;
import cn.sen.lucky.rpc.activity.booth.req.DrawReq;
import cn.sen.lucky.rpc.activity.booth.res.DrawRes;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 抽奖服务
 * @author: 森林有秘密
  * @date: 2022/12/18
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@Service
public class LotteryLogicFilter implements LogicFilter {

    @Resource
    private ILuckyActivityBooth lotteryActivityBooth;

    @Override
    public String filter(BehaviorMatter request) {

        DrawReq drawReq = new DrawReq();
        drawReq.setuId(request.getOpenId().substring(1, 11));
        drawReq.setActivityId(100001L);

        DrawRes drawRes = lotteryActivityBooth.doDraw(drawReq);
        AwardDTO awardDTO = drawRes.getAwardDTO();

        if (!"0000".equals(drawRes.getCode())) {
            return "抽奖💐 提示：" + drawRes.getInfo();
        }

        return "恭喜💐 您已中奖：" + awardDTO.toString();
    }

}
