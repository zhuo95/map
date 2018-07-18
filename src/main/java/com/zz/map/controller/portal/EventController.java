package com.zz.map.controller.portal;


import com.zz.map.common.ServerResponse;
import com.zz.map.entity.Event;
import com.zz.map.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse postEvent(Event event){
        return iEventService.postEvent(event);
    }


    @GetMapping("/place/{id}")
    @ResponseBody
    public ServerResponse getEventByPlaceId(@PathVariable("id") String placeId,
                                            @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex,
                                            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iEventService.getEventByPlaceId(placeId,pageIndex,pageSize);
    }

    //按照事件id查找
    @GetMapping("/{id}")
    @ResponseBody
    public ServerResponse getEventById(@PathVariable("id") Long id){
        return iEventService.getEventById(id);
    }

    //分类查找
    @GetMapping("/category/{id}")
    public ServerResponse getEventByCategory(@PathVariable("id") Integer category,
                                             @RequestParam(value = "pageIndex",defaultValue ="0") int pageIndex,
                                             @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iEventService.getEventByCategory(category,pageIndex,pageSize);
    }

    //删除event
    @DeleteMapping("/{id}")
    @ResponseBody
    public ServerResponse deleteEventById(@PathVariable("id") Long id){
        return iEventService.deleteEventById(id);
    }

    //更新event
    @PatchMapping("/{id}")
    @ResponseBody
    public ServerResponse updateEventById(@PathVariable("id") Long id,Event event){
        return iEventService.updateEventById(id,event);
    }
}
