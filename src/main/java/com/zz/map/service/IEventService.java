package com.zz.map.service;

import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import org.springframework.data.domain.Page;

public interface IEventService {

    ServerResponse getByLatitudeAndLongitude(Double latitude, Double longitude);

    ServerResponse postEvent(Event event);

    ServerResponse<Page> getEventByPlaceId(String placeId, int pageIndex, int pageSize);

    ServerResponse<Event> getEventById(Long id);

    ServerResponse<Page> getEventByCategory(Integer category,int pageIndex,int pageSize);

    ServerResponse deleteEventById(Long id);

    ServerResponse<Event> updateEventById(Long id,Event event);
}
