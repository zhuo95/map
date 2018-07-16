package com.zz.map.common;

import com.zz.map.util.PropertyUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    //jedis连接池
    private static ShardedJedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertyUtil.getProperty("redis.max.total","20"));
    //最大和最小的idle状态(空闲状态)连接实例个数
    private static Integer maxIdle = Integer.parseInt(PropertyUtil.getProperty("redis.max.idle","10"));
    private static Integer minIdle =Integer.parseInt(PropertyUtil.getProperty("redis.min.idle","2"));
    //申请jedis实例之前是否要测试是否可用
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertyUtil.getProperty("redis.test.borrow","true"));
    //在还的时候是否进行test操作，如果设置为true则返回的实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertyUtil.getProperty("redis.test.return","true"));

    private static String ip1 = PropertyUtil.getProperty("redis1.ip");
    private static Integer port1 = Integer.parseInt(PropertyUtil.getProperty("redis1.port"));

    private static String ip2 = PropertyUtil.getProperty("redis2.ip");
    private static Integer port2 = Integer.parseInt(PropertyUtil.getProperty("redis2.port"));


    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候是否阻塞，true是阻塞直到连接超时，false则会直接抛出异常
        config.setBlockWhenExhausted(true);
        //info
        JedisShardInfo info1 = new JedisShardInfo(ip1,port1,2*1000);
        JedisShardInfo info2 = new JedisShardInfo(ip2,port2,2*1000);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        //MURMUR_HASH 设置哈希算法
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static{
        initPool();
    }
    //borrow
    public static ShardedJedis getJedis(){
       return pool.getResource();
    }
    //return
    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }
    //return broken resource
    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }


}
