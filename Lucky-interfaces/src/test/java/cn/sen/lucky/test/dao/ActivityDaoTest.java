package cn.sen.lucky.test.dao;


import cn.sen.lucky.infrastructure.dao.IActivityDao;
import cn.sen.lucky.infrastructure.po.Activity;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description: 测试活动表 DAO
 * @author: 森林有秘密
 * @date: 2021/9/22
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityDaoTest {

    private Logger logger = LoggerFactory.getLogger(ActivityDaoTest.class);

    @Resource
    private IActivityDao activityDao;

    @Test
    public void test_queryActivityById() {
        Activity activity = activityDao.queryActivityById(100001L);
        logger.info("测试结果：{}", JSON.toJSONString(activity));
    }

}
