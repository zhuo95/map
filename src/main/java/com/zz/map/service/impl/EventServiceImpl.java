package com.zz.map.service.impl;

import com.google.common.collect.Lists;
import com.zz.map.common.Const;
import com.zz.map.common.ResponseCode;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.entity.Place;
import com.zz.map.entity.User;
import com.zz.map.repository.EventRepository;
import com.zz.map.repository.PlaceRepository;
import com.zz.map.service.IEventService;
import com.zz.map.util.*;
import com.zz.map.vo.PlaceVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("IEventService")
@Slf4j
public class EventServiceImpl implements IEventService {
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private EventRepository eventRepository;

    //获取当前地址周围的有事件的event
    @Transactional
    public ServerResponse getByLatitudeAndLongitude(Double latitude, Double longitude){
        if(latitude==null||longitude==null) return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        Double laLow = latitude - Const.RADIUS;
        Double laHigh = latitude + Const.RADIUS;
        Double loLow = longitude - Const.RADIUS;
        Double loHigh = longitude + Const.RADIUS;

        List<Place> places = placeRepository.findAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThanAndEventNumGreaterThan(laLow,laHigh,loLow,loHigh,0);

        List<PlaceVo> res = Lists.newArrayList();
        //find event for every palce
        for(Place p : places){
            String id = p.getId();
            PlaceVo pv = new PlaceVo();
            pv.setEventNum(p.getEventNum());
            pv.setCreateTime(p.getCreateTime());
            pv.setId(id);
            pv.setLatitude(p.getLatitude());
            pv.setLongitude(p.getLongitude());
            pv.setPlaceName(p.getPlaceName());
            pv.setUpdateTime(p.getUpdateTime());
            //new event list

            ServerResponse sr = getEventByPlaceId(id);
            List<Event> events = (List<Event>)sr.getData();
            //更新place的eventNum
            int numOfEvent = events.size();
            if(numOfEvent!=p.getEventNum()){
                Place place = placeRepository.getPlaceForUpdate(id);
                place.setEventNum(numOfEvent);
                place.setUpdateTime(new Date());
                placeRepository.save(place);
            }

            List<String> eventTitles = Lists.newArrayList();
            for(Event e : events){
                String title = e.getTitle();
                eventTitles.add(title);
            }
            pv.setEventTitles(eventTitles);
            //放入res
            if(numOfEvent>0) res.add(pv);
        }

        return ServerResponse.creatBySuccess(res);
    }

    //提交event
    @Transactional
    public ServerResponse postEvent(Event event,User user){
        if(event.getCategory()==null||event.getCategory()>4||event.getCategory()<0||
                event.getAddress()==null||event.getExpireDays()==null||event.getDate()==null||event.getLatitude()==null||event.getLongitude()==null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Date now = new Date();
        //处理date和过期时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try{
            date = sdf.parse(event.getDate());
        }catch (ParseException e){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        event.setCreateTime(now);
        event.setUpdateTime(now);
        event.setStatus(Const.EVENT_STATUS.OPEN);
        event.setExpireTime(DateUtils.addDays(date,event.getExpireDays()));
        event.setUserId(user.getId());
        event.setUserName(user.getUsername());
        //查看是否有place，求出id
        String lon = String.valueOf(event.getLongitude());
        String la = String.valueOf(event.getLatitude());
        String id = lon + "," + la;
        Place place = placeRepository.getPlaceForUpdate(id);
        //不存在则添加
        if(place==null){
            Place newPlace = new Place();
            newPlace.setId(id);
            newPlace.setLatitude(event.getLatitude());
            newPlace.setLongitude(event.getLongitude());
            newPlace.setPlaceName(event.getAddress());
            newPlace.setEventNum(1);
            newPlace.setCreateTime(now);
            newPlace.setUpdateTime(now);
            placeRepository.save(newPlace);
        }else {
            place.setEventNum(place.getEventNum()+1);
            place.setUpdateTime(now);
            placeRepository.save(place);
        }
        //save event
        event.setPlaceId(id);
        Event event1 = eventRepository.save(event);
        //放入redis
        RedisShardedPoolUtil.hset(id,String.valueOf(event1.getId()),JsonUtil.obj2String(event1));
        //设置redis过期时间
        RedisShardedPoolUtil.expire(id,Const.RedisCacheExTime.REDIS_EVENT_TIME); // 一天
        return ServerResponse.creatBySuccessMessage("操作成功");
    }



    //按照placeId获取event
    public ServerResponse<List> getEventByPlaceId(String placeId){

        //先到redis中找
        Map<String,String> eventsJsonStr = RedisShardedPoolUtil.hgetall(placeId);
        List<Event> lis = new ArrayList<>();
        if(eventsJsonStr.size()!=0){
            for(String eventJson : eventsJsonStr.values()){
                Event e = JsonUtil.string2Obj(eventJson,Event.class);
                lis.add(e);
            }
            return ServerResponse.creatBySuccess(lis);
        }
        //redis中找不到
        List<Event> events = eventRepository.findAllByPlaceIdAndExpireTimeAfterAndStatus(placeId,new Date(),Const.EVENT_STATUS.OPEN);
        //存入redis
        for(Event e : events){
            RedisShardedPoolUtil.hset(placeId,String.valueOf(e.getId()),JsonUtil.obj2String(e));
        }
        RedisShardedPoolUtil.expire(placeId,Const.RedisCacheExTime.REDIS_EVENT_TIME); // 一天
        return ServerResponse.creatBySuccess(events);
    }

    //按照eventId 查找
    public ServerResponse<Event> getEventById(Long id){
        Event event = eventRepository.findById(id).orElse(null);
        if(event==null||event.getStatus()==Const.EVENT_STATUS.CLOSE) return ServerResponse.creatByErrorMessage("找不到该活动");
        return ServerResponse.creatBySuccess(event);
    }

    //按照分类查找
    public ServerResponse<Page> getEventByCategory(Integer category,int pageIndex,int pageSize){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<Event> events = eventRepository.findAllByCategoryAndExpireTimeAfterAndStatus(category,new Date(),Const.EVENT_STATUS.OPEN,pageable);
        if(events.getTotalElements()==0) return ServerResponse.creatByErrorMessage("没有活动");
        return ServerResponse.creatBySuccess(events);
    }

    //按照event ID 删除
    @Transactional
    public ServerResponse deleteEventById(Long id,User user){
        Event event = eventRepository.findById(id).orElse(null);
        if(event==null||event.getStatus()==Const.EVENT_STATUS.CLOSE) return ServerResponse.creatByErrorMessage("没有该活动");
        Long userid = event.getUserId();
        if(!user.getId().equals(userid)) return ServerResponse.creatByErrorMessage("没有权限");
        Date now = new Date();
        event.setStatus(Const.EVENT_STATUS.CLOSE);
        event.setUpdateTime(now);
        eventRepository.save(event);

        String placeId = event.getPlaceId();
        //修改place eventNum
        Place place = placeRepository.getPlaceForUpdate(placeId);
        if(place.getEventNum()>0) place.setEventNum(place.getEventNum()-1);
        place.setUpdateTime(now);
        placeRepository.save(place);
        //修改redis中
        RedisShardedPoolUtil.hdel(placeId,String.valueOf(event.getId()));
        return ServerResponse.creatBySuccess("操作成功");
    }

    //Update
    public ServerResponse<Event> updateEventById(Long id,Event event,Long userId){
        if(StringUtils.isBlank(event.getPlaceId())) return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        String eventJson = RedisShardedPoolUtil.hget(event.getPlaceId(),String.valueOf(id));
        Event e = JsonUtil.string2Obj(eventJson,Event.class);
        if(e==null){
            //不在缓存中,取数据库取
            e = eventRepository.findById(id).orElse(null);
        }
        if(!userId.equals(e.getUserId())) return ServerResponse.creatByErrorMessage("没有权限");
        if(event.getDate()!=null){
            //需要修改过期时间
            //处理date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date date = null;
            try{
                date = sdf.parse(event.getDate());
            }catch (ParseException ex){
                return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            //重新设置过期时间
            event.setExpireTime(DateUtils.addDays(date,event.getExpireDays()));
        }
        //更新非空fields
        UpdateUtil.copyNullProperties(e,event);
        event.setUpdateTime(new Date());
        if(event.getStatus()==Const.EVENT_STATUS.OPEN){
            //更新redis
            RedisShardedPoolUtil.hset(event.getPlaceId(),String.valueOf(id),JsonUtil.obj2String(event));
            //更新过期时间
            RedisShardedPoolUtil.expire(event.getPlaceId(),Const.RedisCacheExTime.REDIS_EVENT_TIME); //一天
        }else{
            //关闭的要在redis中删除
            RedisShardedPoolUtil.hdel(event.getPlaceId(),String.valueOf(event.getId()));
        }
        //存到数据库
        eventRepository.save(event);
        return ServerResponse.creatBySuccess(event);
    }


    //查找当前登录对象的发布信息
    public ServerResponse findInfo(Long userId){
        List<Event> events = eventRepository.findAllByUserIdAndStatus(userId,Const.EVENT_STATUS.OPEN);
        return ServerResponse.creatBySuccess(events);
    }


}
