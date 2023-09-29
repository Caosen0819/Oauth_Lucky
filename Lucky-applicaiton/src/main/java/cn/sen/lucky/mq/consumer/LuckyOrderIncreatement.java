package cn.sen.lucky.mq.consumer;

import cn.sen.lucky.common.Constants;
import cn.sen.lucky.domain.activity.model.res.StockResult;
import cn.sen.lucky.domain.util.RedisUti2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @Author caosen
 * @Date 2023/9/20 22:45
 */
@Component
public class LuckyOrderIncreatement {

    private Logger logger = LoggerFactory.getLogger(LuckyInvoiceListener.class);

    @Resource
    RedisUti2 redisUti2;

    @KafkaListener(topics = "lucky_increatement", groupId = "lucky")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (!message.isPresent()) {
            return;
        }

        try {
            String key = (String) message.get();
            Integer order_num = (int)redisUti2.incr(key, 1);
            if (order_num <= 0) {
                logger.info("redis订单数量增加失败");
            } else {
                logger.info("key订单数量增加成功");
            }
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
