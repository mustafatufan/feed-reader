package com.gamesys.feed.service;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gamesys.feed.parser.service.FeedParserService;
import com.gamesys.feed.repository.FeedRepository;
import com.gamesys.feed.repository.FeedRepositoryUnavailableException;

@Service("feedService")
public class FeedServiceImpl implements FeedService {

	@Value("${feed.url}")
	private String feedUrl;

	@Autowired
	@Qualifier("feedParserService")
	private FeedParserService feedParserService;

	@Autowired
	@Qualifier("feedRepository")
	private FeedRepository feedRepository;

	@Override
	public void loadFeed() throws FeedServiceUnavailableException {
		try {
			List<Feed> list = feedParserService.parseFeed(new URL(feedUrl).openStream());
			for (Feed feed : list) {
				feedRepository.save(feed.getId(), feed.getText());
			}
		} catch (Exception ex) {
			throw new FeedServiceUnavailableException(ex);
		}
	}

	@Override
	public List<IFeed> publishFeed(int size) throws FeedServiceUnavailableException {
		try {
			return feedRepository
					.getLast(size)
					.stream()
					.map(feed -> convertFeed(feed))
					.collect(Collectors.toList());
		} catch (FeedRepositoryUnavailableException ex) {
			throw new FeedServiceUnavailableException(ex);
		}
	}

	private IFeed convertFeed(Feed feed) {
		return feed;
	}
}
