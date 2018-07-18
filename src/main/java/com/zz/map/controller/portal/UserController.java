package com.zz.map.controller.portal;

import com.zz.map.common.ServerResponse;
import com.zz.map.util.ucenterUtil.client.Client;
import com.zz.map.util.ucenterUtil.util.XMLHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {


    @PostMapping("/login")
    @ResponseBody
    public ServerResponse login(String username, String password, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Client e = new Client();
        String result = e.uc_user_login(username, password);
        LinkedList<String> rs = XMLHelper.uc_unserialize(result);
        if (rs.size() > 0) {
            int $uid = Integer.parseInt(rs.get(0));
            String $username = rs.get(1);
            String $password = rs.get(2);
            String $email = rs.get(3);
            if ($uid > 0) {
                String $ucsynlogin = e.uc_user_synlogin($uid);
                return ServerResponse.creatBySuccess("登录成功",$ucsynlogin);
            }else if($uid == -1) {
               return ServerResponse.creatByErrorMessage("用户不存在,或者被删除");
            }else if($uid == -2) {
                return ServerResponse.creatByErrorMessage("密码错误");
            } else {
                return ServerResponse.creatByErrorMessage("未定义");
            }
        }
        return ServerResponse.creatByErrorMessage(result);
    }


}