package com.gamesys.feed.feedreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.gamesys.feed.*")
public class FeedReaderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FeedReaderApplication.class, args);
	}
}
