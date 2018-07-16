package com.zz.map.util;

import com.zz.map.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        try {
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{}",key,value,e);
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

//设置expire时间
    public static String setEx(String key,int exTime,String value){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        try {
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("set key:{} value:{}",key,value,e);
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //重新expire时间，单位是秒，1成功 0失败
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;

        jedis = RedisPool.getJedis();
        try {
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        jedis = RedisPool.getJedis();
        try {
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //删除key >0成功，0失败
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        jedis = RedisPool.getJedis();
        try {
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }



}
