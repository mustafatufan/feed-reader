package com.gamesys.feed.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gamesys.feed.repository.FeedRepository;
import com.gamesys.feed.repository.FeedRepositoryUnavailableException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service("feedService")
public class FeedServiceImpl implements FeedService {

	@Value("${feed.url}")
	private String feedUrl;

	@Autowired
	@Qualifier("feedRepository")
	private FeedRepository feedRepository;

	@Override
	public void loadFeed() throws FeedServiceUnavailableException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new XmlReader(new URL(feedUrl)));
		} catch (IllegalArgumentException | FeedException | IOException ex) {
			throw new FeedServiceUnavailableException(ex);
		}

		for (SyndEntry entry : feed.getEntries()) {
			Long id = new Long(entry.getLink().replaceAll("\\D+", "")); // regex for get tweet id from tweet url
			String text = entry.getTitle().replaceAll("#", "").replaceAll("@", ""); // regex for get rid of twitter
																					// chars
			try {
				feedRepository.save(id, text);
			} catch (FeedRepositoryUnavailableException ex) {
				throw new FeedServiceUnavailableException(ex);
			}
		}
	}

	@Override
	public List<IFeed> publishFeed(int size) throws FeedServiceUnavailableException {
		try {
			return feedRepository.getLast(size).stream().map(feed -> convertFeed(feed)).collect(Collectors.toList());

		} catch (FeedRepositoryUnavailableException ex) {
			throw new FeedServiceUnavailableException(ex);
		}
	}

	private IFeed convertFeed(Feed feed) {
		return feed;
	}
}
