package com.zz.map.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Slf4j
//@Component
//public class RabbitMQServer {
//
//    @RabbitListener(queues = "api-queue")
//    private void receive(int message){
//        log.info("message : {}",message);
//    }
//}
