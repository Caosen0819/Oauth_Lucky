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
 * @author: 小傅哥，微信：fustack
 * @date: 2021/12/18
 * @github: https://github.com/fuzhengwei
 * @Copyright: 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
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

        return "恭喜💐 您已中奖：" + awardDTO.getAwardName() + " - Lottery 抽奖系统测试";
    }

}
