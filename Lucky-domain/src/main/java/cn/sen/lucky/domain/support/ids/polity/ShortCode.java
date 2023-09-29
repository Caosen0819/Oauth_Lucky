package cn.sen.lucky.domain.support.ids.polity;

import cn.sen.lucky.domain.support.ids.IIdGenerator;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;

/**
 * @Author caosen
 * @Date 2023/3/31 13:13
 */

@Component
public class ShortCode implements IIdGenerator {

    @Override
    public synchronized Long nextId() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONDAY);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // 打乱排序：2020年为准 + 小时 + 周期 + 日 + 三位随机数

        StringBuilder idStr = new StringBuilder();
        idStr.append(year - 2000);
        idStr.append(hour);
        idStr.append(String.format("%02d", week));
        idStr.append(day);
        idStr.append(String.format("%03d", new Random().nextInt(1000)));

        return Long.parseLong(idStr.toString());



    }
}
