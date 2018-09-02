# feed-reader

- Import the inner folder **feed-reader/feed-reader/** as Maven project.
- Run **com.gamesys.feed.feedreader.FeedReaderApplication.java** as Java Application
- Browse **localhost:8080/feed?size=10**

# application.properties

#The feed that will be loaded. user=? must be a Twitter account.

feed.url=https://twitrss.me/twitter_user_to_rss/?user=gamesys

#Period time to load & update feed in milliseconds.

feed.period=10000
