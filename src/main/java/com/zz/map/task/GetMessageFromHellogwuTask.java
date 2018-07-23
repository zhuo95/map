package com.zz.map.task;


import com.zz.map.common.Const;
import com.zz.map.util.HTTPSUtil;
import com.zz.map.util.JsonUtil;
import com.zz.map.util.PropertyUtil;
import com.zz.map.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;

@Component
@Slf4j
public class GetMessageFromHellogwuTask {

    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //释放分布式锁
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void closeOrderTask(){
      log.info("开始定时任务");
      long lockTimeout = Long.parseLong(PropertyUtil.getProperty("lock.timeout","5000"));
      //设置lock 和 lock的timeout
      Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
      if(setnxResult!=null&&setnxResult.intValue()==1){
          //如果返回时1表示设置成功，获取到锁
          closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
      }else {
          //没有获得分布式锁,继续判断时间戳
          String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
          if(lockValueStr!=null&&System.currentTimeMillis() > Long.parseLong(lockValueStr)){
              String res = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
              //再次用当前事件戳getset,用旧值判断是否可以获取锁
              if(res==null|| (res!=null&& StringUtils.equals(res,lockValueStr))){
                  //在getset返回时null，或者是原值的时候可以获取锁，表示没有被其他进程set
                  closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
              }
          }
      }

    }
    //设置expire时间，释放锁
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,5);
        //log.info("获取{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        getEvent();
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //释放分布式锁
        //log.info("释放{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
    }

    //定时任务，从hellogwu获取
    private void getEvent(){
        //只抓取3页
        List<SellEvent> sells = new ArrayList<>();
        for(int i=1;i<=3;i++){
            Map<String,String> argsMap = new HashMap<>();
            argsMap.put("sortid","5");
            argsMap.put("page",String.valueOf(i));
            argsMap.put("fid","41");
            argsMap.put("mod","forum");
            argsMap.put("act","forumdisplay");
            argsMap.put("vister_token", PropertyUtil.getProperty("vister_token"));
            String res = HTTPSUtil.doPost(PropertyUtil.getProperty("api_url"),argsMap,"utf-8");
            Map<String,Object> map = JsonUtil.string2Obj(res,Map.class);
            if(map!=null&&(Integer)map.get("success")==1){
                List events = (List)map.get("threads");
                //获取id，用于查看详细信息
                for(int j=0;j<events.size();j++){
                    Map<String,Object> cur = (Map)events.get(j);
                    SellEvent e = new SellEvent();
                    e.setTid(Long.valueOf((Integer) cur.get("tid")));
                    e.setCreateTime(new Date(Long.valueOf((Integer)cur.get("dateline"))));
                    sells.add(e);
                }
            }else log.error("读取hellogwu错误");

        }

        //查询具体信息
        for(SellEvent sellEvent : sells){
            Map<String,String> argsMap = new HashMap<>();
            argsMap.put("page","1");
            argsMap.put("tid",String.valueOf(sellEvent.getTid()));
            argsMap.put("fid","41");
            argsMap.put("mod","forum");
            argsMap.put("act","viewthread");
            argsMap.put("vister_token", PropertyUtil.getProperty("vister_token"));
            String res = HTTPSUtil.doPost(PropertyUtil.getProperty("api_url"),argsMap,"utf-8");
            Map<String,Object> map = JsonUtil.string2Obj(res,Map.class);
            if(map!=null&&(Integer)map.get("success")==1){
                Map<String,Object> thread = (Map)map.get("thread");
                String author = (String)thread.get("author");
                Long uid = Long.valueOf((Integer)thread.get("authorid"));
                String subject = (String)thread.get("subject");
                Map<String,Object> show = (Map)map.get("threadsortshow");
                List option = (List)show.get("optionlist");
                Map<String,String> loc = (Map)option.get(1);
                String address = loc.get("value");
                String url = "https://hellogwu.com/thread-"+sellEvent.getTid()+"-1-1.html";
                sellEvent.setUsername(author);
                sellEvent.setUid(uid);
                sellEvent.setAddress(address);
                sellEvent.setUrl(url);
                sellEvent.setSubject(subject);
            }
        }
        //将sells存入redis 保存一天 第二天重新load
        for(SellEvent event : sells){
            String address = event.getAddress();
            String eventJson = JsonUtil.obj2String(event);
            if(StringUtils.isNotEmpty(address))RedisShardedPoolUtil.lpush(address,eventJson);
        }
        //设置过期时间
        RedisShardedPoolUtil.expire("Washington DC",60*60*23);
        RedisShardedPoolUtil.expire("Maryland",60*60*23);
        RedisShardedPoolUtil.expire("Virginia",60*60*23);
    }

//    public static void main(String[] args) {
//        getEvent();
//    }
}
