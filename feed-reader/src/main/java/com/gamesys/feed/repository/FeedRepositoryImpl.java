package com.gamesys.feed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	private final String INSERT = "insert into FEED values (?, ?)";
	private final String SELECT = "select * from FEED order by id desc limit ?";

	private EmbeddedDatabase database;

	@PostConstruct
	private void init() {
		initiate();

	}

	private void initiate() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		database = builder.setType(EmbeddedDatabaseType.H2).addScript("h2/create-table.sql").build();
	}

	@Override
	public void save(Long id, String text) throws FeedRepositoryUnavailableException {
		Connection con = null;
		try {
			con = database.getConnection();
			PreparedStatement prep = con.prepareStatement(INSERT);
			prep.setLong(1, id);
			prep.setString(2, text);
			prep.execute();
		} catch (SQLException ex) {
			throw new FeedRepositoryUnavailableException(ex);
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				// TODO
			}
		}
	}

	@Override
	public List<Feed> getLast(int size) throws FeedRepositoryUnavailableException {
		List<Feed> list = new ArrayList<Feed>();
		Connection con = null;
		try {
			con = database.getConnection();
			PreparedStatement prep = con.prepareStatement(SELECT);
			prep.setInt(1, size);
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				Feed feed = new Feed();
				feed.setId(result.getLong(1));
				feed.setText(result.getString(2));
				list.add(feed);
			}
		} catch (SQLException ex) {
			throw new FeedRepositoryUnavailableException(ex);
		} finally {
			try {
				con.close();
			} catch (SQLException ex) {
				// TODO:
			}
		}
		return list;
	}
}
