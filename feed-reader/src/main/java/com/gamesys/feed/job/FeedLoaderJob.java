package com.gamesys.feed.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gamesys.feed.service.FeedService;
import com.gamesys.feed.service.FeedServiceUnavailableException;

@Component
public class FeedLoaderJob {

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;

	@Scheduled(fixedRateString = "${feed.period}")
	public void loadFeed() {
		try {
			feedService.loadFeed();
		} catch (FeedServiceUnavailableException ex) {
			// TODO: log ex
		}

	}
}
