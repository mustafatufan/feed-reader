package com.gamesys.feed.parser.service;

import java.io.InputStream;
import java.util.List;

import com.gamesys.feed.service.Feed;

public interface FeedParserService {
	public List<Feed> parseFeed(InputStream inputStream) throws FeedParserServiceUnavailableException;
}
