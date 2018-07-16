package com.zz.map.controller.portal;


import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event")
@CrossOrigin(origins = "*")
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
    public ServerResponse postEvent(Event event){
        return null;
        //TODO
    }
}
