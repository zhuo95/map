package com.zz.map.common;

import com.zz.map.util.PropertyUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    //jedis连接池
    private static JedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertyUtil.getProperty("redis.max.total","20"));
    //最大和最小的idle状态(空闲状态)连接实例个数
    private static Integer maxIdle = Integer.parseInt(PropertyUtil.getProperty("redis.max.idle","10"));
    private static Integer minIdle =Integer.parseInt(PropertyUtil.getProperty("redis.min.idle","2"));
    //申请jedis实例之前是否要测试是否可用
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertyUtil.getProperty("redis.test.borrow","true"));
    //在还的时候是否进行test操作，如果设置为true则返回的实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertyUtil.getProperty("redis.test.return","true"));
    private static String ip = PropertyUtil.getProperty("redis1.ip");
    private static Integer port = Integer.parseInt(PropertyUtil.getProperty("redis1.port"));
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候是否阻塞，true是阻塞直到连接超时，false则会直接抛出异常
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config,ip,port,1000*2);
    }

    static{
        initPool();
    }
    //borrow
    public static Jedis getJedis(){
        return pool.getResource();
    }
    //return
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }
    //return broken resource
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("zz","good");
        returnResource(jedis);

        System.out.println("操作成功");
    }
}
