package com.zz.map.common;
import com.zz.map.util.ucenterUtil.client.Client;
import com.zz.map.util.ucenterUtil.util.XMLHelper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import java.util.LinkedList;


public class RPCServer {
    @RabbitListener(queues = "tut.rpc.requests")
    // @SendTo("tut.rpc.replies") used when the
    // client doesn't set replyTo.
    public String call(String message) {
        System.out.println(" [x] Received request for " + message);
        String result = callApi(message);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    public String callApi(String message){
        String[] args =  message.split(",");
        Client e = new Client();
        String result = e.uc_user_login(args[0], args[1]);
        LinkedList<String> rs = XMLHelper.uc_unserialize(result);
        if (rs.size() > 0) {
            int $uid = Integer.parseInt(rs.get(0));
            String $username = rs.get(1);
            String $password = rs.get(2);
            String $email = rs.get(3);
            if ($uid > 0) {
                String $ucsynlogin = e.uc_user_synlogin($uid);
                return $ucsynlogin;
            }else if($uid == -1) {
                return "1";
            }else if($uid == -2) {
                return "2";
            } else {
                return "3";
            }
        }
        return result;
    }
}
