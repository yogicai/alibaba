package com.mom.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("kafka-receiver")
@Slf4j
public class Receiver {

    /**
     * 使用ConsumerRecord类消费  每次消费一条数据
     */
    @KafkaListener(topics = "imooc-kafka-topic", groupId = "receive-1")
    public void receive(ConsumerRecord<String, Object> consumer) {
        log.info("消息已消费：topic为[{0}]-key为[{1}]-value为[{2}]", consumer.topic(), consumer.key(), consumer.value());
        System.out.println(consumer.value());
    }


    /**
     * 批量消费 并发量不能大于partition的数量
     * @TopicPartition：topic--需要监听的Topic的名称，partitions --需要监听Topic的分区id，partitionOffsets --可以设置从某个偏移量开始监听
     * @PartitionOffset：partition --分区Id，非数组，initialOffset --初始偏移量
     * @Payload：获取的是消息的消息体，也就是发送内容
     * @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY)：获取发送消息的key
     * @Header(KafkaHeaders.RECEIVED_PARTITION_ID)：获取当前消息是从哪个分区中监听到的
     * @Header(KafkaHeaders.RECEIVED_TOPIC)：获取监听的TopicName
     * @Header(KafkaHeaders.RECEIVED_TIMESTAMP)：获取时间戳
     */
    @KafkaListener(topics = {"imooc-kafka-topic"}, containerFactory = "kafkaListenerContainerFactory", groupId = "receive-2")
    public void receiveMessage(@Payload List<String> data,
                               @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
        log.info("消息已消费：partition为[{0}]-topic为[{1}]-key为[{2}]-value为[{3}]", partition, topic, key, data);
    }

    /**
     * Ack机制确认消费
     * 设置ENABLE_AUTO_COMMIT_CONFIG=false，禁止自动提交
     * 设置AckMode=MANUAL_IMMEDIATE
     * 监听方法加入Acknowledgment ack 参数
     */
    @KafkaListener(topics = {"imooc-kafka-topic"}, containerFactory = "ackContainerFactory", groupId = "receive-3")
    public void ackListener(ConsumerRecord record, Acknowledgment ack) {
        log.info("topic.quick.ack receive:{0}, ack:{1}", record.value(), ack.toString());
        ack.acknowledge();
    }
}