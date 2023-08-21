package com.selffeed.controller;

import com.selffeed.model.dao.UserDao;
import com.selffeed.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@Slf4j
@ComponentScan("com.selffeed.model.dao")
public class UserController {

    @Autowired
    UserDao userDao;

    @PostMapping("/api/users")
    public User addUser(@RequestBody User user){

        int result = 0;
        try {
            result = userDao.add(user.getUsername(),user.getPsw());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("Save user"+ user.toString()+ " result: "+result);
        return user;
    }
    @GetMapping("/api/users")
    public List<User> getUsers(){
        List<User> result = null;
        try {
            result = userDao.getUsers();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return result;
    }
    @PostMapping("/api/login")
    public User login(@RequestBody User  user){
        User newUser = null;
        try {
            newUser = userDao.login(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return newUser;
    }

    @PutMapping("/api/users")
    public int updateUser(@RequestBody User  user){
        int result = 0;
        try {
            result = userDao.update(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("Update user"+ user.toString()+ " result: "+result);
        return result;
    }

    @PostMapping("/api/check_attack")
    public int checkAttack(@RequestBody User  user){
        int attack = 0;
        try {
            attack = userDao.checkUserAttack(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return attack;
    }

}
