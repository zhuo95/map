package com.zz.map.controller.portal;

import com.zz.map.common.Const;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.User;
import com.zz.map.service.IUserService;
import com.zz.map.util.CookieUtil;
import com.zz.map.util.JsonUtil;
import com.zz.map.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
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
    private IUserService iUserService;

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse login(String username, String password, HttpServletResponse response, HttpServletRequest request){
        ServerResponse rs = iUserService.login(username,password);
        if(rs.isSuccess()){
            User user = (User)rs.getData();
            //放入redis
            RedisShardedPoolUtil.setEx(user.getToken(), Const.RedisCacheExTime.REDIS_SESSION_TIME , JsonUtil.obj2String(user));
            //放入cookie
            CookieUtil.writeLoginToken(response,user.getToken());
            user.setPassword(StringUtils.EMPTY);
            return rs;
        }
       return rs;
    }

    //signup
    @PostMapping("/signup")
    @ResponseBody
    public ServerResponse signup(User user){
        return iUserService.signup(user);
    }


}