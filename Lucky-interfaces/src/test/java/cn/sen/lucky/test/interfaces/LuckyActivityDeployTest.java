package cn.sen.lucky.test.interfaces;

import cn.sen.lucky.rpc.activity.deploy.ILuckyActivityDeploy;
import cn.sen.lucky.rpc.activity.deploy.req.ActivityPageReq;
import cn.sen.lucky.rpc.activity.deploy.res.ActivityRes;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description: 活动部署测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuckyActivityDeployTest {

    private Logger logger = LoggerFactory.getLogger(LuckyActivityDeployTest.class);

    @Resource
    private ILuckyActivityDeploy luckyActivityDeploy;

    @Test
    public void test_queryActivityListByPageForErp() {
        ActivityPageReq req = new ActivityPageReq(1, 10);
        req.setErpId("xiaofuge");

        ActivityRes res = luckyActivityDeploy.queryActivityListByPageForErp(req);

        logger.info("请求参数：{}", JSON.toJSONString(req));
        logger.info("测试结果：{}", JSON.toJSONString(res));
    }

}
