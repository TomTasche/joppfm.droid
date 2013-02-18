package at.tomtasche.joppfm.database;

import android.database.Cursor;

public class Message {

	public static Message fromCursor(Cursor cursor) {
		Message message = new Message();
		message.setId(cursor.getLong(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_ID)));
		message.setBody(cursor.getString(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_BODY)));
		message.setFrom(cursor.getLong(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_FROM)));
		message.setTo(cursor.getLong(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_TO)));
		message.setPending(cursor.getInt(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_PENDING)) <= 0);
		message.setTimestamp(cursor.getLong(MessageSchema
				.getColumnIndex(MessageSchema.COLUMN_TIMESTAMP)));

		return message;
	}

	private long id;
	private String body;
	private long timestamp;
	private boolean pending;
	private long from;
	private long to;

	private Message() {
	}

	public Message(String body, long from, long to) {
		this(-1, body, System.currentTimeMillis(), true, from, to);
	}

	public Message(long id, String body, long timestamp, boolean pending,
			long from, long to) {
		this.id = id;
		this.body = body;
		this.timestamp = timestamp;
		this.pending = pending;
		this.from = from;
		this.to = to;
	}

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

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return from + ": " + body;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
