package com.selffeed.model.dao;

import com.selffeed.pojo.Feed;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public interface FeedDao {
    public int add(String title,String url)
            throws SQLException;
    public void delete(int id)
            throws SQLException;
    public Feed getFeed(int id)
            throws SQLException;
    public List<Feed> getFeeds()
            throws SQLException;

    public int updateDate(int id,Date date) throws SQLException ;
}
