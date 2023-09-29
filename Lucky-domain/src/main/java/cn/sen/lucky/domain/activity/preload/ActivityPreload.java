package cn.sen.lucky.domain.activity.preload;

import cn.sen.lucky.domain.activity.model.vo.ActivityVO;
import cn.sen.lucky.domain.activity.repository.IActivityRepository;

import cn.sen.lucky.domain.util.RedisUti2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author caosen
 * @Date 2023/9/11 11:06
 */
@Component
public class ActivityPreload {
    @Resource
    private IActivityRepository activityRepository;

    @Resource
    public RedisUti2 redisUtil2;
    @PostConstruct
    private void init(){
        // 得到活动存入redis
        List<ActivityVO> activityS = activityRepository.queryAllActivity();
        for (ActivityVO activityvo : activityS) {
            String name = "Activity_" + activityvo.getActivityId();
            redisUtil2.set(name, activityvo);
            String key = activityvo.getActivityId() + "_Order";
            redisUtil2.set(key, 0);
//            HashMap<String, Object> userCount = new HashMap<>();
//            String key2 = activityvo.getActivityId() + "_UserCount";
//            redisUtil2.hmset(key2, userCount, -1);

        }

//        List<Object> objectList = activityVOS.stream()
//                .map(activityVO -> (Object) activityVO)
//                .collect(Collectors.toList());
//        redisUtil2.rightpush("AllActivity", objectList);

    }
}
