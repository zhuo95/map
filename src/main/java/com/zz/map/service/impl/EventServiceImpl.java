package com.zz.map.service.impl;

import com.zz.map.common.Const;
import com.zz.map.common.ResponseCode;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.entity.Place;
import com.zz.map.entity.User;
import com.zz.map.repository.EventRepository;
import com.zz.map.repository.PlaceRepository;
import com.zz.map.service.IEventService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("IEventService")
public class EventServiceImpl implements IEventService {
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private EventRepository eventRepository;

    //获取当前地址周围的有事件的event
    public ServerResponse getByLatitudeAndLongitude(Double latitude, Double longitude){
        List<Place> places = placeRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude- Const.RADIUS,latitude+ Const.RADIUS,longitude- Const.RADIUS,longitude+ Const.RADIUS);
        return ServerResponse.creatBySuccess(places);
    }

    //提交event
    public ServerResponse postEvent(Event event, User user){
        if(event.getCategory()==null||event.getCategory()>4||event.getCategory()<0||
                event.getAddress()==null||event.getExpireDays()==null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Date now = new Date();
        event.setCreateTime(now);
        event.setExpireTime(DateUtils.addDays(now,event.getExpireDays()));
        event.setUserId(user.getId());
        event.setUserName(user.getNickName());
        //查看是否有place
        Place place = placeRepository.findByLatitudeAndLongitude(event.getLatitude(),event.getLongitude());
        //不存在则添加
        if(place==null){
            Place newPlace = new Place();
            newPlace.setLatitude(event.getLatitude());
            newPlace.setLongitude(event.getLongitude());
            newPlace.setPlaceName(event.getAddress());
            newPlace.setEventNum(1);
            newPlace.setCreateTime(now);
            newPlace.setUpdateTime(now);
            placeRepository.save(newPlace);
        }else {
            place.setEventNum(place.getEventNum()+1);
            placeRepository.save(place);
        }

        return null;
    }


}
