package com.mom.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController("redis")
@Slf4j
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/redis/set")
    public String get() {
        //键值对存储
        this.redisTemplate.opsForValue().set("name","张三");
        String name = this.stringRedisTemplate.opsForValue().get("name");
        log.info(name);

        //list存储
        this.redisTemplate.opsForList().leftPushAll("names","李四","王五","赵六");
        List names = this.stringRedisTemplate.opsForList().range("names", 0, 3);
        log.info(names.toString());

        //hash使用
        Map<String, String> map = new HashMap<>(4);
        map.put("name","秦始皇");
        map.put("age","未知");
        map.put("sex","男");
        this.stringRedisTemplate.opsForHash().putAll("HASH_PERSON",map);
        String s =(String) this.stringRedisTemplate.opsForHash().get("HASH_PERSON", "name");
        log.info(s);

        return "SUCCESS";
    }

    @GetMapping("/redis/get")
    public String set(String name, String value) {
        this.stringRedisTemplate.opsForValue().set(name, value);
        return "SUCCESS";
    }

}