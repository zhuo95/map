package com.zz.map.controller.portal;


import com.zz.map.common.ResponseCode;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.entity.User;
import com.zz.map.service.IEventService;
import com.zz.map.task.SellEvent;
import com.zz.map.util.CookieUtil;
import com.zz.map.util.JsonUtil;
import com.zz.map.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/event")
@CrossOrigin(origins = "*",allowCredentials ="true")
public class EventController {

    @Autowired
    private IEventService iEventService;

    //获取选择地点的附近的event
    @GetMapping
    @ResponseBody
    public ServerResponse getByLatitudeAndLongitude(Double longitude, Double latitude){

       return iEventService.getByLatitudeAndLongitude(latitude,longitude);
    }

    @PostMapping
    @ResponseBody
    public ServerResponse postEvent(Event event,HttpServletRequest request){
        //判断session
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null) return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        return iEventService.postEvent(event,user);
    }


    @GetMapping("/place/{id}")
    @ResponseBody
    public ServerResponse getEventByPlaceId(@PathVariable("id") String placeId){  // @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize
        return iEventService.getEventByPlaceId(placeId);
    }

    //按照事件id查找
    @GetMapping("/{id}")
    @ResponseBody
    public ServerResponse getEventById(@PathVariable("id") Long id){
        return iEventService.getEventById(id);
    }

    //分类查找
    @GetMapping("/category/{id}")
    @ResponseBody
    public ServerResponse getEventByCategory(@PathVariable("id") Integer category,
                                             @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex,
                                             @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iEventService.getEventByCategory(category,pageIndex,pageSize);
    }

    //删除event
    @DeleteMapping("/{id}")
    @ResponseBody
    public ServerResponse deleteEventById(@PathVariable("id") Long id,HttpServletRequest request){
        //判断session
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null)  return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        return iEventService.deleteEventById(id,user);
    }

    //更新event
    @PatchMapping("/{id}")
    @ResponseBody
    public ServerResponse updateEventById(@PathVariable("id") Long id,Event event,HttpServletRequest request){
        //判断session
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null)  return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        return iEventService.updateEventById(id,event,user.getId());
    }

    //查找sell
    @GetMapping("/sell/{location}")
    @ResponseBody
    public ServerResponse<List<SellEvent>> getsellByPlace(@PathVariable("location") Integer location){
        if(location==0){
            //Washington Dc
            Long l = RedisShardedPoolUtil.llen("Washington DC");
            List<String> res = RedisShardedPoolUtil.lrange("Washington DC",0L,l);
            List<SellEvent> events = new ArrayList();
            for(String s :  res){
                SellEvent e = JsonUtil.string2Obj(s,SellEvent.class);
                events.add(e);
            }
            return ServerResponse.creatBySuccess(events);
        }
        if(location==1){
            //Virginia
            Long l = RedisShardedPoolUtil.llen("Virginia");
            List<String> res = RedisShardedPoolUtil.lrange("Virginia",0L,l);
            List<SellEvent> events = new ArrayList();
            for(String s :  res){
                SellEvent e = JsonUtil.string2Obj(s,SellEvent.class);
                events.add(e);
            }
            return ServerResponse.creatBySuccess(events);
        }
        if(location==2){
            //Maryland
            Long l = RedisShardedPoolUtil.llen("Maryland");
            List<String> res = RedisShardedPoolUtil.lrange("Maryland",0L,l);
            List<SellEvent> events = new ArrayList();
            for(String s : res){
                SellEvent e = JsonUtil.string2Obj(s,SellEvent.class);
                events.add(e);
            }
            return ServerResponse.creatBySuccess(events);
        }
        return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    @GetMapping("/my")
    @ResponseBody
    public ServerResponse getMyEvents(HttpServletRequest request){
        //判断session
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user==null) return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEEDLOG_IN.getCode(),ResponseCode.NEEDLOG_IN.getDesc());
        return iEventService.findInfo(user.getId());
    }
}
