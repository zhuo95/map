package com.zz.map.common;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;



public class RPCClient {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;


    public String send(String message) {
        String response = (String) template.convertSendAndReceive
                (exchange.getName(), "rpc", message);
        return response;
    }
}
