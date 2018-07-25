package com.zz.map.service.impl;


import com.zz.map.common.RPCClient;
import com.zz.map.common.ResponseCode;
import com.zz.map.common.ServerResponse;
import com.zz.map.entity.User;
import com.zz.map.repository.UserRepository;
import com.zz.map.service.IUserService;
import com.zz.map.util.JsonUtil;
import com.zz.map.util.MD5Util;
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
        User user = userRepository.findByEmail(username); //username 就是email
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
                user.setAvatar("https://hellogwu.com/uc_server/avatar.php?uid="+user.getUid());
                user.setToken((String)map.get("token"));
                user.setGender((Integer)map.get("gender"));
                Date now = new Date();
                user.setCreateTime(now);
                user.setUpdateTime(now);
                //System.out.println(res);
                //放入数据库
                User addedUser = userRepository.save(user);
                user.setId(addedUser.getId());
                user.setPassword(MD5Util.MD5EncodeUtf8(password));
                userRepository.save(user);
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
        ServerResponse checkEmail = checkEmail(user.getEmail());
        if(!checkEmail.isSuccess()) return ServerResponse.creatByErrorMessage("邮箱已注册");
        if(user.getGender()==null) user.setGender(2);
        Date now = new Date();
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userRepository.save(user);
        return ServerResponse.creatBySuccessMessage("注册成功");
    }

    public ServerResponse checkEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user!=null) return ServerResponse.creatByErrorMessage("邮箱已注册");
        return ServerResponse.creatBySuccessMessage("邮箱未注册，可以使用");
    }
}
