package com.zz.map.controller.portal;

import com.zz.map.common.RPCClient;
import com.zz.map.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*",allowCredentials = "true")
public class UserController {

    @Autowired
    private RPCClient rpcClient;

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse login(String username, String password, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String res = rpcClient.send(username+","+password);
        if(res==null) return ServerResponse.creatByErrorMessage("登录失败，请重新尝试");
        if(res.equals("1")) return ServerResponse.creatByErrorMessage("用户不存在");
        if(res.equals("2")) return ServerResponse.creatByErrorMessage("密码错误");
        if(res.equals("3")) return ServerResponse.creatByErrorMessage("未知错误");
        return ServerResponse.creatBySuccess(res);
    }


}