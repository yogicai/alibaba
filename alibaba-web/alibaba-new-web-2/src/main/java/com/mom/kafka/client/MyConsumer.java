package com.mom.kafka.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

/**
 * @author Created by caiyz1 on 2019/12/16
 */
public class MyConsumer {

    /*
        消费者拉取数据之后自动提交偏移量，不关心后续对消息的处理是否正确
        优点：消费快，适用于数据一致性弱的业务场景
        缺点：消息很容易丢失
     */
    @Test
    public void autoCommit() {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "true");
        //自动提交时间间隔
        props.put("auto.commit.interval.ms", "1000");
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题，可以订阅多个主题
        consumer.subscribe(Arrays.asList("mytopic1"));
        //死循环不停的从broker中拿数据
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

    @Test
    public void munualCommit() {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "false");
        //一次最大拉取条数 默认500
        props.put("max.poll.records", 100);
        //一次poll的业务处理时间 默认300s consumer执行poll的时候会向GroupCoordinator发送一次心跳
        //业务处理时间大于max.poll.interval.ms值 会导致GroupCoordinator以为本地consumer节点挂掉了，引发了partition在consumerGroup里的rebalance
        props.put("max.poll.interval.ms", 1800000);
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题，可以订阅多个主题
        consumer.subscribe(Arrays.asList("mytopic1"));
        final int minBatchSize = 50;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                //insertIntoDb(buffer);
                for (ConsumerRecord bf : buffer) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", bf.offset(), bf.key(), bf.value());
                }
                //手动提交offset
                consumer.commitSync();
                buffer.clear();
            }
        }
    }

    @Test
    public void munualCommitByPartition() {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "false");
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题，可以订阅多个主题
        consumer.subscribe(Arrays.asList("mytopic3"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("partition: " + partition.partition() + " , " + record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    /*
                        提交的偏移量应该始终是您的应用程序将要读取的下一条消息的偏移量。因此，在调用commitSync（）时，
                        offset应该是处理的最后一条消息的偏移量加1
                        为什么这里要加上面不加喃？因为上面Kafka能够自动帮我们维护所有分区的偏移量设置，有兴趣的同学可以看看SubscriptionState.allConsumed()就知道
                     */
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 消费者从指定分区拉取数据
     * ​ a.kafka提供的消费者组内的协调功能就不再有效
     *   b.这样的写法可能出现不同消费者分配了相同的分区，为了避免偏移量提交冲突，每个消费者实例的group_id要不重复
     */
    @Test
    public void munualPollByPartition() {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "false");
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题，并设置要拉取的分区
        TopicPartition partition0 = new TopicPartition("mytopic3", 0);
        //TopicPartition partition1 = new TopicPartition("mytopic2", 1);
        //consumer.assign(Arrays.asList(partition0, partition1));
        consumer.assign(Arrays.asList(partition0));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("partition: " + partition.partition() + " , " + record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    /*
        手动设置指定分区的offset，只适用于使用Consumer.assign方法添加主题的分区，不适用于kafka自动管理消费者组中的消费者场景，
        后面这种场景可以使用ConsumerRebalanceListener做故障恢复使用
     */
    @Test
    public void controlsOffset() {
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "false");
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题，并设置要拉取的分区

        //加一段代码将自己保存的分区和偏移量读取到内存
        //load partition and it's offset
        TopicPartition partition0 = new TopicPartition("mytopic3", 0);
        consumer.assign(Arrays.asList(partition0));

        //告知Consumer每个分区应该从什么位置开始拉取数据，offset从你加载的值或者集合中拿
        consumer.seek(partition0, 4140l);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("partition: " + partition.partition() + " , " + record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }


    /**
     *  添加因消费者改变导致kafka rebalance的监听
     *  kafka提供该监听来处理当某一个topic的消费者发生变化（加入、退出）时分区重新分配（先解除与消费者的绑定关系，再重新与消费者绑定）用户想做回调的情况，
     *  分区与消费者解除绑定时调用onPartitionsRevoked方法；
     *  重新绑定时调用onPartitionsAssigned
     */
    @Test
    public void autoCommitAddListner(){
        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交 true-开启 false-关闭
        props.put("enable.auto.commit", "false");
        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        MyConsumerRebalanceListener myListener = new MyConsumerRebalanceListener(consumer);
        //消费者订阅主题，可以订阅多个主题
        consumer.subscribe(Arrays.asList("mytopic3"),myListener);
        //consumer.subscribe(Arrays.asList("mytopic3"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("partition: " + partition.partition() + " , " + record.offset() + ": " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    /*
                        可以将这里的偏移量提交挪到监听的onPartitionsRevoked方法中，控制灵活，但是也很容易出问题
                     */
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

}
