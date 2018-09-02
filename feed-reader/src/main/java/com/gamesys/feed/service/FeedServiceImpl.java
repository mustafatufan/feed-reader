package com.gamesys.feed.service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gamesys.feed.parser.service.FeedParserService;
import com.gamesys.feed.repository.FeedRepository;
import com.gamesys.feed.repository.FeedRepositoryUnavailableException;

@Service("feedService")
public class FeedServiceImpl implements FeedService {

	private final Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
	private final String STREAM_NOT_CLOSED_ERROR_LOG = "Stream could not be closed.";

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
		InputStream inputStream = null;
		try {
			inputStream = new URL(feedUrl).openStream();
			List<Feed> list = feedParserService.parseFeed(inputStream);
			saveFeedList(list);
		} catch (Exception ex) {
			throw new FeedServiceUnavailableException(ex);
		} finally {
			try {
				inputStream.close();
			} catch (Exception ex) {
				logStreamNotClosedError(ex);
			}
		}
	}

	private void saveFeedList(List<Feed> list) throws FeedServiceUnavailableException {
		try {
			Long max = feedRepository.getMaxFeedId();
			for (Feed feed : list) {
				if (feed.getId().compareTo(max) > 0) {
					feedRepository.save(feed.getId(), feed.getText());
				}
			}
		} catch (FeedRepositoryUnavailableException ex) {
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

	private void logStreamNotClosedError(Throwable ex) {
		logger.error(STREAM_NOT_CLOSED_ERROR_LOG, ex);
	}
}
