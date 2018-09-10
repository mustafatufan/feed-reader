package com.gamesys.feed.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamesys.feed.service.FeedService;
import com.gamesys.feed.service.FeedServiceUnavailableException;
import com.gamesys.feed.service.IFeed;

@RestController
public class FeedController {

	private final Logger logger = LoggerFactory.getLogger(FeedController.class);

	private FeedService feedService;

	@RequestMapping("/feed")
	public List<IFeed> feed(@RequestParam("size") int size) {
		List<IFeed> list = new ArrayList<IFeed>();
		try {
			list = feedService.publishFeed(size);
		} catch (FeedServiceUnavailableException ex) {
			logger.error("Feed is unavailable.", ex);
		}
		return list;
	}

	@Autowired
	@Qualifier("feedService")
	public void setFeedService(FeedService feedService) {
		this.feedService = feedService;
	}

}