package com.gamesys.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;

import com.gamesys.feed.service.Feed;

@Repository("feedRepository")
public class FeedRepositoryImpl implements FeedRepository {

	private final String INSERT_FEED = "insert into FEED (id, text) values (?, ?)";
	private final String SELECT_FEED = "select id, text from FEED order by id desc limit ?";
	private final String SELECT_MAX_FEED_ID = "select max(id) from FEED";

	private EmbeddedDatabase database;

	@PostConstruct
	private void init() {
		initiate();
	}

	private void initiate() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		database = builder.setType(EmbeddedDatabaseType.H2).addScript("h2/create-feed.sql").build();
	}

	@Override
	public void save(Long id, String text) throws FeedRepositoryUnavailableException {
		try (Connection con = database.getConnection()) {
			PreparedStatement prep = con.prepareStatement(INSERT_FEED);
			prep.setLong(1, id);
			prep.setString(2, text);
			prep.execute();
		} catch (Exception ex) {
			throw new FeedRepositoryUnavailableException(ex);
		}
	}

	@Override
	public List<Feed> getLast(int size) throws FeedRepositoryUnavailableException {
		List<Feed> list = new ArrayList<Feed>();
		try (Connection con = database.getConnection()) {
			PreparedStatement prep = con.prepareStatement(SELECT_FEED);
			prep.setInt(1, size);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				Long id = result.getLong(1);
				String text = result.getString(2);
				list.add(new Feed(id, text));
			}
		} catch (Exception ex) {
			throw new FeedRepositoryUnavailableException(ex);
		}
		return list;
	}

	@Override
	public Long getMaxFeedId() throws FeedRepositoryUnavailableException {
		Long max = Long.MIN_VALUE;
		try (Connection con = database.getConnection()) {
			PreparedStatement prep = con.prepareStatement(SELECT_MAX_FEED_ID);
			ResultSet result = prep.executeQuery();
			if (result.next()) {
				max = result.getLong(1);
			}
		} catch (Exception ex) {
			throw new FeedRepositoryUnavailableException(ex);
		}
		return max;
	}
}
