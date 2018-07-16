package com.zz.map.service;

import com.zz.map.common.ServerResponse;

public interface IEventService {

    ServerResponse getByLatitudeAndLongitude(Double latitude, Double longitude);
}
