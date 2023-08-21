package com.selffeed.model.dao;

import com.selffeed.pojo.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface UserDao {
    public int add(String user,String psw)
            throws SQLException;
    public void delete(int id)
            throws SQLException;
    public User getUser(int id)
            throws SQLException;
    public List<User> getUsers()
            throws SQLException;
    public int update(User emp)
            throws SQLException;

    public int increaseAttack(int id,int attack) throws SQLException;

    public int increaseAllAttack() throws SQLException;
    public User login(User emp) throws SQLException;
    public int consumeAttack(int uid) throws SQLException;

    public int checkUserAttack(User user) throws SQLException;
}
