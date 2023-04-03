package cn.sen.lucky.domain.support.ids.polity;

import cn.sen.lucky.domain.support.ids.IIdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author caosen
 * @Date 2023/3/31 13:07
 */
@Component
public class RandomNumeric implements IIdGenerator {


    @Override
    public Long nextId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(11));
    }
}
