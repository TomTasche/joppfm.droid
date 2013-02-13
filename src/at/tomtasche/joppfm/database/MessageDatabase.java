package at.tomtasche.joppfm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class MessageDatabase extends SQLiteOpenHelper {

	public static final String TABLE_MESSAGES = "message";

	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_FROM = "sender";
	public static final String COLUMN_SENT = "sent";

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_BODY,
			COLUMN_FROM, COLUMN_SENT };

	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_MESSAGES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_BODY
			+ " text not null, " + COLUMN_FROM + " text not null, "
			+ COLUMN_SENT + " integer);";

	private SQLiteDatabase database;

	public MessageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MessageDatabase.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		onCreate(db);
	}

	public void open() {
		database = getWritableDatabase();
	}

	public void open(boolean writable) {
		if (writable) {
			open();
		} else {
			database = getReadableDatabase();
		}
	}

	public Message createMessage(String body, String from) {
		ContentValues values = new ContentValues();
		values.put(MessageDatabase.COLUMN_BODY, body);
		values.put(MessageDatabase.COLUMN_FROM, from);
		values.put(MessageDatabase.COLUMN_SENT, 1);

		long insertId = database.insert(MessageDatabase.TABLE_MESSAGES, null,
				values);

		Cursor cursor = database.query(MessageDatabase.TABLE_MESSAGES,
				COLUMNS, MessageDatabase.COLUMN_ID + " = " + insertId, null,
				null, null, null);

		Message newMessage = null;
		try {
			if (cursor.moveToFirst()) {
				newMessage = Message.fromCursor(cursor);
			} else {
				throw new RuntimeException("message not written to database");
			}
		} finally {
			cursor.close();
		}

		return newMessage;
	}

	public List<Message> getAllMessages() {
		List<Message> messages = new ArrayList<Message>();

		Cursor cursor = database.query(MessageDatabase.TABLE_MESSAGES,
				COLUMNS, null, null, null, null, null);
		try {
			if (!cursor.moveToFirst()) {
				// throw new RuntimeException("no messages in database");
			}

			while (cursor.moveToNext()) {
				Message message = Message.fromCursor(cursor);
				messages.add(message);
			}
		} finally {
			cursor.close();
		}

		return messages;
	}
}
