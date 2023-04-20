//package cn.sen.lucky.mq.consumer;
//
//import cn.hutool.core.lang.Assert;
//import cn.sen.lucky.common.Constants;
//import cn.sen.lucky.domain.activity.model.vo.InvoiceVO;
//import cn.sen.lucky.domain.award.model.req.GoodsReq;
//import cn.sen.lucky.domain.award.model.res.DistributionRes;
//import cn.sen.lucky.domain.award.serve.factory.DistributionGoodsFactory;
//import cn.sen.lucky.domain.award.serve.goods.IDistributionGoods;
//import com.alibaba.fastjson.JSON;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Optional;
//
///**
// * @Author caosen
// * @Date 2023/4/5 10:20
// */
//@Component
//public class KafkaConsumer {
//
//    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
//
//    @Resource
//    private DistributionGoodsFactory distributionGoodsFactory;
//
//    @KafkaListener(topics = "lucky-invoice", groupId = "lucky")
//    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        Optional<?> message = Optional.ofNullable(record.value());
//        if (!message.isPresent()) {
//            return ;
//
//        }
//
//        try {
//            /*先把message里面的对象转化成为们的实体类*/
//            InvoiceVO invoiceVO = JSON.parseObject((String) message.get(), InvoiceVO.class);
//            //获取发送奖品工厂，执行发奖
//            IDistributionGoods distributionGoodsService = distributionGoodsFactory.getDistributionGoodsService(invoiceVO.getAwardType());
//            DistributionRes distributionRes = distributionGoodsService.doDistribution(new GoodsReq(invoiceVO.getuId(), invoiceVO.getOrderId(), invoiceVO.getAwardId(), invoiceVO.getAwardName(), invoiceVO.getAwardContent()));
//
//            Assert.isTrue(Constants.AwardState.SUCCESS.getCode().equals(distributionRes.getCode()), distributionRes.getInfo());
//            // 3. 打印日志
//            logger.info("消费MQ消息，完成 topic：{} bizId：{} 发奖结果：{}", topic, invoiceVO.getuId(), JSON.toJSONString(distributionRes));
//
//            // 4. 消息消费完成
//            ack.acknowledge();
//        } catch (Exception e) {
//            // 发奖环节失败，消息重试。所有到环节，发货、更新库，都需要保证幂等。
//            logger.error("消费MQ消息，失败 topic：{} message：{}", topic, message.get());
//            throw e;
//        } finally {
//        }
//
//
//    }
//
//}