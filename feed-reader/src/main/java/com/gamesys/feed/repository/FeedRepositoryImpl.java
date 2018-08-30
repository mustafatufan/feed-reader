package com.gamesys.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Repository;

import com.gamesys.feed.service.Feed;

@Repository("feedRepository")
public class FeedRepositoryImpl implements FeedRepository {

	private final Logger logger = LoggerFactory.getLogger(FeedRepositoryImpl.class);

	private final String INSERT_FEED = "insert into FEED (id, text) values (?, ?)";
	private final String SELECT_FEED = "select id, text from FEED order by id desc limit ?";

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
		Connection con = null;
		try {
			con = database.getConnection();
			PreparedStatement prep = con.prepareStatement(INSERT_FEED);
			prep.setLong(1, id);
			prep.setString(2, text);
			prep.execute();
		} catch (Exception ex) {
			throw new FeedRepositoryUnavailableException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				logger.error("Connection could not be closed.", ex);
			}
		}
	}

	@Override
	public List<Feed> getLast(int size) throws FeedRepositoryUnavailableException {
		List<Feed> list = new ArrayList<Feed>();
		Connection con = null;
		try {
			con = database.getConnection();
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
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				logger.error("Connection could not be closed.", ex);
			}
		}
		return list;
	}
}
