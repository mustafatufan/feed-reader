package com.gamesys.feed.parser.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gamesys.feed.service.Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service("feedParserService")
public class FeedParserServiceImpl implements FeedParserService {

	@Override
	public List<Feed> parseFeed(InputStream inputStream) throws FeedParserServiceUnavailableException {
		List<Feed> list = new ArrayList<Feed>();

		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed rss = input.build(new XmlReader(inputStream));
			for (SyndEntry entry : rss.getEntries()) {
				Long id = getId(entry.getLink());
				String text = getText(entry.getTitle());
				list.add(new Feed(id, text));
			}
		} catch (Exception ex) {
			throw new FeedParserServiceUnavailableException(ex);
		}
		return list;
	}

	private Long getId(String link) {
		String[] parsed = link.split("/");
		return new Long(parsed[parsed.length - 1]);
	}

	private String getText(String raw) {
		return raw
				.replaceAll("#", "[HASHTAG!]")
				.replaceAll("@", "[AT!]")
				.replaceAll("http://", "")
				.replaceAll("https://", "");
	}
}
