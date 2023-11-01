package com.selffeed.util;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.selffeed.house.HouseServerOB;
import com.selffeed.house.PostUtil;
import com.selffeed.model.dao.ArticleDao;
import com.selffeed.model.dao.BuildingDao;
import com.selffeed.model.dao.FeedDao;
import com.selffeed.model.dao.UserDao;
import com.selffeed.pojo.Article;
import com.selffeed.pojo.Building;
import com.selffeed.pojo.BuildingSales;
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

    @Autowired
    PostUtil postUtil;
    public final static long GAP_INTERVAL = 24 * 60 * 60 * 1000;
//    public final static long GAP_INTERVAL = 60 * 1000;
    @Scheduled(fixedRate = GAP_INTERVAL)
    public void fetchBuildingInfo() {
        log.info("exec building check schedule.");
        try{
            List<Building> list = buildingDao.getBuildings();
            for (Building building : list) {
                //Check if ts update time is more then 24 hour or not.
                boolean exec = false;
                long gapTime = 0;
                if(building.getTs()==null){
                    exec = true;
                }else{
                    gapTime = System.currentTimeMillis()-building.getTs().getTime();
                    if(gapTime > GAP_INTERVAL){
                        exec = true;
                    }else{
                        exec = false;
                        log.info("No need exec ... Gap time = " + gapTime);
                    }
                }
                if(exec){
                    HouseServerOB tmp = postUtil.checkUrl(building.getPreSellId(),
                            building.getYsProjectId(),
                            String.valueOf(building.getFybId()),
                            building.getBuildBranch());
                    double percent = tmp.checkSellStatus();
                    try {
                        BuildingSales bs = BuildingSales.builder()
                                .b_id(building.getB_id())
                                .sold(tmp.getWholeSize() - tmp.getVirginSize())
                                .unsold(tmp.getVirginSize())
                                .percent(percent).build();
                        building.setSold(tmp.getWholeSize() - tmp.getVirginSize());
                        building.setUnsold(tmp.getVirginSize());
                        building.setPercent(percent);
                        log.info("update builiding.");
                        buildingDao.update(building);
                        log.info("add sales record.");
                        buildingDao.addSale(bs);
                    } catch (SQLException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void fetchRSS() {
        log.info("exec schedule.");
        //TODO will delete later
        if(1==1) return ;

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
