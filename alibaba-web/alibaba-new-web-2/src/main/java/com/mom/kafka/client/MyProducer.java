package com.mom.kafka.client;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

public class MyProducer {

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //ack模式，all是最慢但最安全的
        props.put("acks", "-1");
        //失败重试次数
        props.put("retries", 0);
        //每个分区未发送消息总字节大小（单位：字节），超过设置的值就会提交数据到服务端
        props.put("batch.size", 10);
        //props.put("max.request.size",10);
        //消息在缓冲区保留的时间，超过设置的值就会被提交到服务端
        props.put("linger.ms", 10000);
        //整个Producer用到总内存的大小，如果缓冲区满了会提交数据到服务端
        //buffer.memory要大于batch.size，否则会报申请内存不足的错误
        props.put("buffer.memory", 10240);
        //序列化器
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(
                new ProducerRecord<String, String>("mytopic1", Integer.toString(i), "dd:" + i));
        }
        //Thread.sleep(1000000);
        producer.close();
    }

    @Test
    public void transactional(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        props.put("transactional.id", "my_transactional_id");
        Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());

        producer.initTransactions();

        try {
            //数据发送必须在beginTransaction()和commitTransaction()中间，否则会报状态不对的异常
            producer.beginTransaction();
            for (int i = 0; i < 100; i++) {
                producer.send(new ProducerRecord<>("mytopic1", Integer.toString(i), Integer.toString(i)));
            }
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // 这些异常不能被恢复，因此必须要关闭并退出Producer
            producer.close();
        } catch (KafkaException e) {
            // 出现其它异常，终止事务
            producer.abortTransaction();
        }
        producer.close();
    }
}