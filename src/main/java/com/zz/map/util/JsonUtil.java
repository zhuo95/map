package com.zz.map.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;


@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static{
        //always对象所有字段全部列入 none_null只有不是null的列入，none_default是只有赋值过的才会列入，none_empty是空的不列入包括length为0
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有日期格式都统一样式 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略在json字符串存在，但是在java对象中不存在的情况
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String obj2String(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to string error",e);
            return null;
        }
    }
    //更好看，会换行
    public static <T> String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to string error",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str)|| clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)? (T) str : objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("parse string to object error",e);
            return null;
        }
    }

    /**
     *===========================================================
     *
     * 复杂的包含list和map的str转obj
     * 下列两种方法都可以
     *
     * ==========================================================
     */

    // 1 通用反序列化方法，比如有list map的
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)|| typeReference==null){
            return null;
        }
        try {
            return  (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("parse string to object error",e);
            return null;
        }
    }

    //2 通用反序列化方法，比如有list map的 ...表示可变长，多个的时候传入数组
    public static <T> T string2Obj(String str, Class<?> collectionClass,Class<?> ... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("parse string to object error",e);
            return null;
        }
    }

//    public static void main(String[] args) {
//        User u1 = new User();
//        u1.setUsername("zzz");
//        u1.setPassword("212121");
//        User u2 = new User();
//        u2.setUsername("zzz22");
//        u2.setPassword("23133132");
//        List<User> lis = Lists.newArrayList();
//        lis.add(u1);
//        lis.add(u2);
//        String user1Json = JsonUtil.obj2StringPretty(lis);
//        //1 test
//        List<User> lis2 = JsonUtil.string2Obj(user1Json, new TypeReference<List<User>>() {
//        });
//        // 2 test
//        List<User> lis3 = JsonUtil.string2Obj(user1Json,List.class,User.class);
//        log.info(user1Json);
//
//
//        System.out.println("end");
//    }
}
