package com.gamesys.feed.service;

import java.util.List;

public interface FeedService {

	public void loadFeed() throws FeedServiceUnavailableException;

	public List<IFeed> publishFeed(int size) throws FeedServiceUnavailableException;
}
