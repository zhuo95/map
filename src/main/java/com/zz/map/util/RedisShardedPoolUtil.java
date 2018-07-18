package com.zz.map.util;

import com.zz.map.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;

@Slf4j
public class RedisShardedPoolUtil {

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{}",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

//设置expire时间
    public static String setEx(String key,int exTime,String value){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("set key:{} value:{}",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //重新expire时间，单位是秒，1成功 0失败
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //删除key >0成功，0失败
    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("set key:{}",key,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //setnx
    public static Long setnx(String key,String value){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.setnx(key,value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{}",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //setnx
    public static String getSet(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        try {
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{}",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //hset hash数据结构 如果set成功返回1
    public static Long hset(String key,String field,String value){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        try{
            result = jedis.hset(key,field,value);
        }catch (Exception e){
            log.error("hset key:{} field:{} value:{}",key,field,value,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //hgetall
    public static Map<String,String> hgetall(String key){
        ShardedJedis jedis = null;
        Map<String,String>  result = null;

        jedis = RedisShardedPool.getJedis();
        try{
            result = jedis.hgetAll(key);
        }catch (Exception e){
            log.error("hexist key:{}",key,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //hdel 失败是返回0,成功1
    public static Long hdel(String key,String field){
        ShardedJedis jedis = null;
        Long result = null;

        jedis = RedisShardedPool.getJedis();
        try{
            result = jedis.hdel(key,field);
        }catch (Exception e){
            log.error("hdel key:{} field:{}",key,field,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    public static String hget(String key ,String field){
        ShardedJedis jedis = null;
        String result = null;

        jedis = RedisShardedPool.getJedis();
        try{
            result = jedis.hget(key,field);
        }catch (Exception e){
            log.error("hdel key:{} field:{}",key,field,e);
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }



}
