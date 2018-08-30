package com.gamesys.feed.repository;

import java.util.List;

import com.gamesys.feed.service.Feed;

public interface FeedRepository {

	public void save(Long id, String text) throws FeedRepositoryUnavailableException;

	public List<Feed> getLast(int size) throws FeedRepositoryUnavailableException;

}
