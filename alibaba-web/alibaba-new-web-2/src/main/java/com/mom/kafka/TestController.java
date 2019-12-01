package com.mom.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("test1")
@Slf4j
public class TestController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/kafka/sendMsg")
    public String sendMsg() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
                log.info("---------------第" + (i + 1) + "条信息已发送。。。-----------------");
            kafkaTemplate.send("imooc-kafka-topic", "test_" + i, "value_" + i * 10);
            Thread.sleep(2000);
        }

        //消息发送的监听器，用于回调返回信息IsolationLevel
        kafkaTemplate.setProducerListener(new ProducerListener<String, Object>() {
            public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
            }
            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
            }
        });

        return "message send success";
    }

}