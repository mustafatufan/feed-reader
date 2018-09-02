package com.gamesys.feed.feedreader;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamesys.feed.repository.FeedRepository;
import com.gamesys.feed.repository.FeedRepositoryUnavailableException;
import com.gamesys.feed.service.Feed;
import com.gamesys.feed.service.FeedService;
import com.gamesys.feed.service.FeedServiceUnavailableException;
import com.gamesys.feed.service.IFeed;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedReaderApplicationTests {

	@Autowired
	@Qualifier("feedService")
	private FeedService feedService;

	@Autowired
	@Qualifier("feedRepository")
	private FeedRepository feedRepository;

	@Test
	public void testSavePublish() {
		try {
			List<Feed> savedList = createFeed();
			saveFeed(savedList);
			List<IFeed> publishedList = publishFeed(savedList.size());
			checkFeedSize(savedList, publishedList);
			checkFeedText(savedList, publishedList);
		} catch (FeedRepositoryUnavailableException | FeedServiceUnavailableException ex) {
			ex.printStackTrace();
		}
	}

	private List<Feed> createFeed() {
		List<Feed> list = new ArrayList<Feed>();
		Random random = new Random();
		int count = random.nextInt(100);
		for (int i = 0; i < count; i++) {
			list.add(new Feed(new Long(i), "@Gamesys #Hiring !".concat(String.valueOf(i))));
		}
		return list;
	}

	private void saveFeed(List<Feed> list) throws FeedRepositoryUnavailableException {
		for (Feed feed : list) {
			feedRepository.save(feed.getId(), feed.getText());
		}
	}

	private List<IFeed> publishFeed(int size) throws FeedServiceUnavailableException {
		return feedService.publishFeed(size);
	}

	private void checkFeedSize(List<Feed> savedList, List<IFeed> publishedList) {
		assertEquals(savedList.size(), publishedList.size());
	}

	private void checkFeedText(List<Feed> savedList, List<IFeed> publishedList) {
		for (Feed feed : savedList) {
			assertEquals(feed.getText(), getFeed(feed.getId(), publishedList).getText());
		}
	}

	private IFeed getFeed(Long id, List<IFeed> list) {
		for (IFeed feed : list) {
			if (id.equals(feed.getId())) {
				return feed;
			}
		}
		return null;
	}
}
