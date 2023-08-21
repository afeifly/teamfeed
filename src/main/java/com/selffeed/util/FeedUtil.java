package com.selffeed.util;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
@Slf4j
@Component
public class FeedUtil {
    public String checkUrl(String feedUrl){
        String title = null;
        SyndFeed feed = null;
        log.info("Check with url = "+ feedUrl);
        try {
            feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
            title = feed.getTitle();
            log.info("fetch feed: "+ title);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return title;
    }
}
