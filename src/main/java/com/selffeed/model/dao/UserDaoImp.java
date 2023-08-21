package com.selffeed.model.dao;

import com.selffeed.pojo.Feed;
import com.selffeed.pojo.User;
import com.selffeed.util.Others;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class UserDaoImp implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int add(String user,String psw) throws SQLException {
        log.debug("Add User");
        log.info("Add User encoder psw to " + DigestUtils.md5DigestAsHex(psw.getBytes()));
        String sql = "INSERT INTO users (username, nickname,icon_index," +
                " password, attack) VALUES (?,?,?,?,?)";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, Others.getCuteNickname());
            ps.setInt(3, Others.getRandomIconIndex());
            ps.setString(4,DigestUtils.md5DigestAsHex(psw.getBytes()));
            ps.setInt(5,100);
            return ps;

        });
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public User getUser(int id) throws SQLException {
        return null;
    }

    @Override
    public List<User> getUsers() throws SQLException {
        List<User> userList = jdbcTemplate.query("select * from users",
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return User.builder()
                                .u_id(rs.getInt(1))
                                .username(rs.getString(2))
                                .nickname(rs.getString(3))
                                .iconIndex(rs.getInt(4))
                                .build();
                    }
                });
        log.debug("Get Users count = "+userList.size());
        return userList;
    }

    @Override
    public int update(User emp) throws SQLException {

        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("update users set nickname=?, " +
                    "icon_index=? where id=?");
            ps.setString(1, emp.getNickname());
            ps.setInt(2, emp.getIconIndex());
            ps.setInt(3, emp.getU_id());
            return ps;
        });
    }


    public User login(User emp) throws SQLException{

        List<User> list = jdbcTemplate.query("select * from users" +
                        " where username=? and password=?",

                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, emp.getUsername());
                        preparedStatement.setString(2, DigestUtils.md5DigestAsHex(emp.getPsw().getBytes()));
                    }
                },
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return User.builder()
                                .u_id(rs.getInt(1))
                                .username(rs.getString(2).trim())
                                .nickname(rs.getString(3).trim())
                                .iconIndex(rs.getInt(4))
                                .attack(rs.getInt(6))
                                .build();
                    }
                });
        log.debug("Get feeds count = "+list.size());
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }


    public int increaseAllAttack() throws SQLException{

        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("update users set attack = attack+1 where attack<150");
            return ps;
        });
    }
    public int increaseAttack(int id,int attack) throws SQLException{

        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("update users set attack = attack+? where attack<150 and id=?");
            ps.setInt(1,attack);
            ps.setInt(2,id);
            return ps;
        });
    }
    public int consumeAttack(int uid) throws SQLException{

        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("update users set attack = attack-2 where attack>0 and id=?");
            ps.setInt(1,uid);
            return ps;
        });
    }

    @Override
    public int checkUserAttack(User user) throws SQLException {
        int attack = 0;
        List<User> list = jdbcTemplate.query("select attack from users" +
                        " where id=? ",

                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setInt(1, user.getU_id());
                    }
                },
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return User.builder()
                                .attack(rs.getInt(1))
                                .build();
                    }
                });
        log.debug("Get feeds count = "+list.size());
        if(list.size()>0){
            return list.get(0).getAttack();
        }
        return 0;
    }
}
