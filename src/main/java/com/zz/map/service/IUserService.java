package com.zz.map.service;

import com.zz.map.common.ServerResponse;
import com.zz.map.entity.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse signup(User user);
}
