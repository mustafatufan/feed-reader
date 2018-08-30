package com.gamesys.feed.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gamesys.feed.service.FeedService;
import com.gamesys.feed.service.FeedServiceUnavailableException;

@Component
public class FeedLoaderJob {

	private final Logger logger = LoggerFactory.getLogger(FeedLoaderJob.class);

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;

	@Scheduled(fixedRateString = "${feed.period}")
	public void loadFeed() {
		try {
			feedService.loadFeed();
		} catch (FeedServiceUnavailableException ex) {
			logger.error("Feed is unavailable.", ex);
		}

	}
}
