package com.selffeed.main;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@SpringBootApplication
@RestController
@EnableScheduling
@ComponentScan({"com.selffeed.controller", "com.selffeed.util"})
public class DemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping("/hello")
	public String hello(){

		log.info("hhh");
		if(1==1){

			return "hello w";
		}
		// RSS feed URL
		String feedUrl = "https://rsshub.exmm.top/t66y/7/";

		SyndFeed feed = null;
		try {
			feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
			System.out.println(feed.getTitle());

			for (SyndEntry entry : feed.getEntries()) {
				System.out.println("1" +entry.getTitle());
				System.out.println("2" +entry.getAuthor());
				System.out.println("3" +entry.getTitleEx());
				System.out.println("4" +entry.getComments());
				System.out.println("5" +entry.getLink());
				System.out.println("5" +entry.getPublishedDate());
				System.out.println("contents" );
				List<SyndContent> contents = entry.getContents();
				for(SyndContent content : contents){
					System.out.println(content.getValue());
				}
			}
		} catch (FeedException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}



		return "hello  zzm xawww";
	}

}
