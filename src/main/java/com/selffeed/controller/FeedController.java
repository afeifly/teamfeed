package com.selffeed.controller;

import com.selffeed.model.dao.FeedDao;
import com.selffeed.model.dao.UserDao;
import com.selffeed.pojo.Feed;
import com.selffeed.pojo.User;
import com.selffeed.util.FeedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@Slf4j
@ComponentScan("com.selffeed.model.dao")
public class FeedController {

    @Autowired
    FeedDao feedDao;
    @Autowired
    FeedUtil feedUtil;

    @PostMapping("/api/feeds")
    public String addFeed(@RequestBody Feed feed){

        int result = 0;
        String title = feedUtil.checkUrl(feed.getUrl());
        if(title != null){
            try {
                result = feedDao.add(title,feed.getUrl());
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            log.info("Save feed "+ feed.toString()+ " result: "+result);
            return "name = "+ feed.toString();
        }else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "URL not available");
        }

    }
    @GetMapping("/api/feeds")
    public List<Feed> addFeed(){

        List<Feed> list = null;
        try {
            list = feedDao.getFeeds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
