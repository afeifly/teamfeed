package com.selffeed.model.dao;

import com.selffeed.pojo.Feed;
import com.selffeed.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class FeedDaoImp implements FeedDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int add(String title,String url) throws SQLException {
        log.debug("Add Feed");
        String sql = "INSERT INTO feeds (title, url) VALUES (?, ?)";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setString(1, title.trim());
            ps.setString(2, url.trim());
            return ps;
        });
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Feed getFeed(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Feed> getFeeds() throws SQLException {
        List<Feed> list = jdbcTemplate.query("select * from feeds",
                new RowMapper<Feed>() {
                    @Override
                    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return Feed.builder()
                                .f_id(rs.getInt(1))
                                .title(rs.getString(2).trim())
                                .url(rs.getString(3).trim())
                                .date(rs.getTimestamp(4))
                                .build();
                    }
                });
        log.debug("Get feeds count = "+list.size());
        return list;
    }

    @Override
    public int updateDate(int id,Date date) throws SQLException {
        return jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("update feeds set ts=? where id=?");
            ps.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
            ps.setInt(2, id);
            return ps;
        });

    }

}
