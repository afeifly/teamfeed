package com.selffeed.util;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.selffeed.model.dao.ArticleDao;
import com.selffeed.model.dao.BuildingDao;
import com.selffeed.model.dao.FeedDao;
import com.selffeed.model.dao.UserDao;
import com.selffeed.pojo.Article;
import com.selffeed.pojo.Feed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    FeedDao feedDao;

    @Autowired
    ArticleDao articleDao;
    @Autowired
    UserDao userDao;

    @Autowired
    BuildingDao buildingDao;

    @Scheduled(fixedRate = 20 * 60 * 60 * 1000)
    public void fetchBuildingInfo() {
        log.info("exec building check schedule.");
        //TODO get all buildings and checking.
//        buildingDao.getBuildings();
    }
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void fetchRSS() {
        log.info("exec schedule.");
        try {
            userDao.increaseAllAttack();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        List<Feed> feedList = null;
        try {
            feedList = feedDao.getFeeds();
            for (Feed feedPojo : feedList) {
                log.info("Fetch feed : " + feedPojo.getTitle());
                String feedUrl = feedPojo.getUrl();
                SyndFeed feed = null;
                try {
                    feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
                    log.info("Ready to fetch feed : " + feed.getTitle());
                    Date latestDate = null;
                    List<Article> articleList = new ArrayList<>();
                    for (SyndEntry entry : feed.getEntries()) {
                        System.out.println("1" + entry.getTitle());
                        System.out.println("desc");
                        SyndContent content = entry.getDescription();
                        if(entry.getPublishedDate()==null){
                            continue;
                        }
                        if (feedPojo.getDate() == null
                                || feedPojo.getDate().compareTo(entry.getPublishedDate()) < 0) {
                            String tmpContent = null;
                            if(entry.getContents().size()>0){
                                tmpContent = entry.getContents().get(0).getValue();
                            }else{
                                tmpContent = entry.getDescription().getValue();
                            }
                            articleList.add(Article.builder()
                                    .title(entry.getTitle())
                                    .auther(entry.getAuthor())
                                    .url(entry.getLink())
                                    .ts(entry.getPublishedDate())
                                    .content(tmpContent)
                                    .f_id(feedPojo.getF_id())
                                    .build());

                            if (latestDate == null) {
                                latestDate = entry.getPublishedDate();
                            } else {
                                if (latestDate.compareTo(entry.getPublishedDate()) < 0) {
                                    latestDate = entry.getPublishedDate();
                                }
                            }
                        }
                    }

                    articleDao.add(articleList);
                    if (latestDate != null) {
                        feedDao.updateDate(feedPojo.getF_id(), latestDate);
                    }
                } catch (FeedException e) {
                    log.error(e.getMessage());
                    continue;
                } catch (IOException e) {
                    log.error(e.getMessage());
                    continue;
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }
}
