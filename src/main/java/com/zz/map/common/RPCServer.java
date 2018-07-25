package com.zz.map.common;

import com.zz.map.util.HTTPSUtil;

import com.zz.map.util.PropertyUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import java.util.HashMap;

import java.util.Map;

@Slf4j
public class RPCServer {
    @RabbitListener(queues = "tut.rpc.requests")
    // @SendTo("tut.rpc.replies") used when the
    // client doesn't set replyTo.
    public String call(String message) {
        //System.out.println(" [x] Received request for " + message);
        log.info("Received request for:{}",message);
        String result = callApi(message);
        //System.out.println(" [.] Returned " + result);
        return result;
    }

    private String callApi(String message){
        String[] args = message.split(",");
        String username = args[0];
        String password = args[1];
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(password)){
            Map<String,String> argsMap = new HashMap<>();
            argsMap.put("username",username);
            argsMap.put("password",password);
            argsMap.put("mod","member");
            argsMap.put("act","login");
            argsMap.put("vister_token", PropertyUtil.getProperty("vister_token"));
            String res = HTTPSUtil.doPost(PropertyUtil.getProperty("api_url"),argsMap,"utf-8");
            return res;
        }
        return null;
    }
}
