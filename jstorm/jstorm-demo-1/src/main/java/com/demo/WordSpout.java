package com.demo;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;


public class WordSpout implements IRichSpout {

    // 一定要 生成 一个 serialVersionUID，因为这些class 都是要经过序列化的
    private static final long serialVersionUID = -4515102038086645770L;

    private String[] strs = {"one", "two", "three", "four", "five", "six"};
    SpoutOutputCollector collector;

    public WordSpout() {
        super();
        System.out.println("WordSpout()****************************");
    }

    /**
     * 定义发射的字段类型，是第一个要是执行的方法。
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    /**
     * 打开 stream 流资源，只会执行一次
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        System.out.println(
            "*****************open(Map conf, TopologyContext context, SpoutOutputCollector collector)");
        this.collector = collector;
    }

    /**
     * 循环执行，向外发送 Tuple
     */
    @Override
    public void nextTuple() {
        int index = RandomUtils.nextInt(6);
        collector.emit(new Values(strs[index]));
        System.out.println("***************nextTuple() : " + strs[index]);
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}