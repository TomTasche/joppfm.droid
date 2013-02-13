package at.tomtasche.joppfm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MessageDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.COLUMN_BODY, DatabaseHelper.COLUMN_FROM };

	public MessageDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Message createMessage(String body, String from) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_BODY, body);
		values.put(DatabaseHelper.COLUMN_FROM, from);
		long insertId = database.insert(DatabaseHelper.TABLE_MESSAGES, null,
				values);
		Cursor cursor = database.query(DatabaseHelper.TABLE_MESSAGES,
				allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Message newMessage = cursorToMessage(cursor);
		cursor.close();
		return newMessage;
	}

	public List<Message> getAllMessages() {
		List<Message> messages = new ArrayList<Message>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_MESSAGES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Message message = cursorToMessage(cursor);
			messages.add(message);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return messages;
	}

	private Message cursorToMessage(Cursor cursor) {
		Message message = new Message();
		message.setId(cursor.getLong(0));
		message.setBody(cursor.getString(1));
		message.setFrom(cursor.getString(2));
		return message;
	}
}