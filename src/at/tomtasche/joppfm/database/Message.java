package at.tomtasche.joppfm.database;

import android.provider.BaseColumns;

public class Message implements BaseColumns {

	private long id;
	private String body;
	private String from;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return from + ": " + body;
	}
}
