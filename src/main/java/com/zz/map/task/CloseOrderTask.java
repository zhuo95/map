package com.zz.map.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {
//    @Autowired
//    private ICartService iCartService;
//
//    @PreDestroy
//    public void delLock(){
//        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //释放分布式锁
//    }
//
//    public void closeOrderTask(){
//      log.info("开始定时任务");
//      long lockTimeout = Long.parseLong(PropertyUtil.getProperty("lock.timeout","5000"));
//      //设置lock 和 lock的timeout
//      Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
//      if(setnxResult!=null&&setnxResult.intValue()==1){
//          //如果返回时1表示设置成功，获取到锁
//          closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//      }else {
//          //没有获得分布式锁,继续判断时间戳
//          String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//          if(lockValueStr!=null&&System.currentTimeMillis() > Long.parseLong(lockValueStr)){
//              String res = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
//              //再次用当前事件戳getset,用旧值判断是否可以获取锁
//              if(res==null|| (res!=null&&StringUtils.equals(res,lockValueStr))){
//                  //在getset返回时null，或者是原值的时候可以获取锁，表示没有被其他进程set
//                  closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//              }
//          }
//      }
//
//    }
//    //设置expire时间，释放锁
//    private void closeOrder(String lockName){
//        RedisShardedPoolUtil.expire(lockName,5);
//        //log.info("获取{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//        iCartService.closeOrder(3);
//        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //释放分布式锁
//        //log.info("释放{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//    }
}
