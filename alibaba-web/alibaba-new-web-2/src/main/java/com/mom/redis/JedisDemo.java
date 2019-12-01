package com.mom.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: yogiCai
 * @create: 2019-11-24 23:57
 **/
public class JedisDemo {

    public void demo1() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("jedisName", "value1");
        jedis.close();
    }

    public void demo2() {
        //连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(10);
        //最大空闲连接数
        config.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("jedisName", "value1");
        jedis.close();
    }

}
