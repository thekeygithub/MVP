package com.ebao.hospitaldapp.config;

import com.ebao.hospitaldapp.data.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {

    private  String host;

    private int port;

    private int timeout;

    private JedisPoolConfig jedisPoolConfig;

    private JedisPool jedisPool;

    public void init()
    {

        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(300); // 可用连接实例的最大数目,如果赋值为-1,表示不限制.
        jedisPoolConfig.setMaxIdle(10); // 控制一个Pool最多有多少个状态为idle(空闲的)jedis实例,默认值也是8
        jedisPoolConfig.setMaxWaitMillis(1000 * 100); // 等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时/如果超过等待时间,则直接抛出异常
        jedisPoolConfig.setTestOnBorrow(true); // 在borrow一个jedis实例时,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的
System.out.println("hahaha"+host);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);

        RedisClient.setJedisPool(jedisPool);

    }

}
