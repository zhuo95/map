package com.zz.map.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class TokenCache {
    public static final String  TOKEN_PREFIX = "token_";
    //设置缓存初始化容量1000,最大容量10000，当超过时guawa会用LRU来替换,缓存12h超过后会删除
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当get取值的时候，如果key没有对应值，就调用这个方法加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (Exception e){
            System.out.println("localCache get error");
        }
        return null;
    }
}
