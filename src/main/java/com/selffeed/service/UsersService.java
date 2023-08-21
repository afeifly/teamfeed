package com.selffeed.service;

import com.selffeed.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UsersService {
    public int saveUser(String username, String psw);
    public List<User> getList();
}
