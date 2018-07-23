package com.zz.map.service.impl;


import com.zz.map.common.Const;
import com.zz.map.common.RPCClient;
import com.zz.map.common.ResponseCode;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.User;
import com.zz.map.repository.UserRepository;
import com.zz.map.service.IUserService;
import com.zz.map.util.JsonUtil;
import com.zz.map.util.MD5Util;
import com.zz.map.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service("IUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private RPCClient rpcClient;
    @Autowired
    private UserRepository userRepository;

    public ServerResponse<User> login(String username,String password){
        //首先先在本地查找
        User user = userRepository.findByUsername(username);
        if(user!=null){
            String newPass = MD5Util.MD5EncodeUtf8(password);
            if(StringUtils.equals(newPass,user.getPassword())){
                user.setPassword(StringUtils.EMPTY);
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                return ServerResponse.creatBySuccess(user);
            }else{
                return ServerResponse.creatByErrorMessage("密码错误");
            }
        }
        //如果user为null说明本地没有
        //远程调用
        String res = rpcClient.send(username+","+password);
        Map<String,Object> map = JsonUtil.string2Obj(res,Map.class);
        if(map!=null){
            if(map.get("success")!=null){
                if((int)map.get("success")==0) return ServerResponse.creatByErrorMessage((String)map.get("reason"));
                //登录成功处理
                user = new User();
                user.setEmail((String)map.get("email"));
                user.setUsername((String)map.get("username"));
                user.setUid(Long.valueOf((Integer)map.get("uid")));
                user.setAvatar("https://hellogwu.com/uc_server/avatar.php?uid="+user.getUid()+"&size=small");
                user.setToken((String)map.get("token"));
                user.setGender((Integer)map.get("gender"));
                Date now = new Date();
                user.setCreateTime(now);
                user.setUpdateTime(now);
                //System.out.println(res);
                //放入数据库
                user.setPassword(MD5Util.MD5EncodeUtf8(password));
                return ServerResponse.creatBySuccess(user);
            }else{
                return ServerResponse.creatByErrorMessage("发生错误");
            }
        }
        return ServerResponse.creatByErrorMessage("发生错误");
    }

    public ServerResponse signup(User user){
        if(StringUtils.isBlank(user.getEmail())||StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if(user.getGender()==null) user.setGender(2);
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userRepository.save(user);
        return ServerResponse.creatBySuccessMessage("注册成功");
    }
}
