package cn.sen.lucky.test.domain;


import cn.sen.lucky.domain.rule.model.req.DecisionMatterReq;
import cn.sen.lucky.domain.rule.model.res.EngineResult;
import cn.sen.lucky.domain.rule.service.engine.EngineFilter;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @description: 规则引擎测试
 * @author: 森林有秘密
 * @date: 2021/10/8
 * @github: Caosen0819
 * @Copyright: 森林有秘密
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTest {

    private Logger logger = LoggerFactory.getLogger(ActivityTest.class);

    @Resource
    private EngineFilter engineFilter;

    @Test
    public void test_process() {
        DecisionMatterReq req = new DecisionMatterReq();
        req.setTreeId(2110081902L);
        req.setUserId("fustack");
        req.setValMap(new HashMap<String, Object>() {{
            put("gender", "man");
            put("age", "25");
        }});

        EngineResult res = engineFilter.process(req);

        logger.info("请求参数：{}", JSON.toJSONString(req));
        logger.info("测试结果：{}", JSON.toJSONString(res));
    }

}
