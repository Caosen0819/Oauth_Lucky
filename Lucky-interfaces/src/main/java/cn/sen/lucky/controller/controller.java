package cn.sen.lucky.controller;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.common.Result;
import cn.sen.lucky.process.draw.req.DrawProcessReq;
import cn.sen.lucky.process.draw.res.DrawProcessResult;
import cn.sen.lucky.rpc.activity.booth.ILuckyActivityBooth;
import cn.sen.lucky.rpc.activity.booth.req.DrawReq;
import cn.sen.lucky.rpc.activity.booth.res.DrawRes;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author caosen
 * @Date 2023/4/22 15:26
 */
@RestController
@RequestMapping("/do")
public class controller {

    @Resource
    private ILuckyActivityBooth luckyActivityBooth;
    @GetMapping("/hello")
    public String hello(){

        DrawReq req = new DrawReq();
        req.setuId("fuzhengwei");
        req.setActivityId(100001L);
        DrawRes drawRes = luckyActivityBooth.doDraw(req);
        return Constants.ResponseCode.SUCCESS.toString();
    }
}
