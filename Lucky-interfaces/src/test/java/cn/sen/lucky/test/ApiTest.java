package cn.sen.lucky.test;

import cn.sen.lucky.infrastructure.dao.IActivityDao;
import cn.sen.lucky.infrastructure.po.Activity;
import cn.sen.lucky.rpc.IActivityBooth;
import cn.sen.lucky.rpc.req.ActivityReq;
import cn.sen.lucky.rpc.res.ActivityRes;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Resource
    IActivityDao activityDao;

//    @Reference(interfaceClass = IActivityBooth.class, url = "dubbo://127.0.0.1:20800")
//    private IActivityBooth iActivityBooth;

    @Test
    public void test_insert() {
        Activity activity = new Activity();
        activity.setActivityId(100002L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("仅用于插入数据测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(100);
        activity.setTakeCount(10);
        activity.setState(0);
        activity.setCreator("xiaofuge");
        activityDao.insert(activity);
    }

    @Test
    public void test_select() {
        Activity activity = activityDao.queryActivityById(100001L);
        logger.info("测试结果：{}", JSON.toJSONString(activity));
    }

//    @Test
//    public void test_rpc()  {
//
//        ActivityReq req = new ActivityReq();
//        req.setActivityId(10001L);
//        ActivityRes res = iActivityBooth.queryActivityById(req);
//        logger.info("测试结果是：",JSON.toJSONString(res));
//
//    }

}
