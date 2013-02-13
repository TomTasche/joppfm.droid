package at.tomtasche.joppfm.database;

import android.database.Cursor;

public class Message {

	public static Message fromCursor(Cursor cursor) {
		Message message = new Message();
		message.setId(cursor.getLong(0));
		message.setBody(cursor.getString(1));
		message.setFrom(cursor.getString(2));
		message.setSent(cursor.getInt(3) > 0);

		return message;
	}

	private long id;
	private String body;
	private String from;
	private boolean sent;

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

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
}
