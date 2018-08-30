package com.gamesys.feed.service;

public class Feed implements IFeed {

	private Long id;
	private String text;

	public Feed(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
