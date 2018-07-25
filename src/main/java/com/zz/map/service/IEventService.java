package com.zz.map.service;

import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEventService {

    ServerResponse getByLatitudeAndLongitude(Double latitude, Double longitude);

    ServerResponse postEvent(Event event,User user);

    ServerResponse<List> getEventByPlaceId(String placeId);

    ServerResponse<Event> getEventById(Long id);

    ServerResponse<Page> getEventByCategory(Integer category,int pageIndex,int pageSize);

    ServerResponse deleteEventById(Long id,User user);

    ServerResponse<Event> updateEventById(Long id,Event event,Long userId);

    ServerResponse findInfo(Long userId);
}
