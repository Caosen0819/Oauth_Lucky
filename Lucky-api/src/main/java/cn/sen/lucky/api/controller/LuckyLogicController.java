package cn.sen.lucky.api.controller;//package cn.sen.lucky.api.controller;


import cn.sen.lucky.rpc.activity.booth.ILuckyActivityBooth;
import cn.sen.lucky.rpc.activity.booth.dto.AwardDTO;
import cn.sen.lucky.rpc.activity.booth.req.DrawReq;
import cn.sen.lucky.rpc.activity.booth.res.DrawRes;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 抽奖服务
 * @author: Lucky
 * @date: 2021/12/18
 * @github: Lucky
 * @Copyright: Lucky
 */
@RestController
@RequestMapping("do")
public class LuckyLogicController  {

    @DubboReference
    private ILuckyActivityBooth LuckyActivityBooth;

    @GetMapping("doDraw")
    public void dddo(){
        System.out.println("jj");
        System.out.println("sddsf");
        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);
        DrawRes drawRes = LuckyActivityBooth.doDraw(req);
    }

}
