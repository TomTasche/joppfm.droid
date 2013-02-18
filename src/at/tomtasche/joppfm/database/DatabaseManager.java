package at.tomtasche.joppfm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase database;

	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(MessageSchema.buildCreateStatement());
		database.execSQL(ContactSchema.buildCreateStatement());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseManager.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		onCreate(db);
	}

	public void open() {
		if (database != null)
			return;

		database = getWritableDatabase();
	}

	public void open(boolean writable) {
		if (writable) {
			open();
		} else {
			database = getReadableDatabase();
		}
	}

	public void persistMessage(Message message) {
		ContentValues values = new ContentValues();
		values.put(MessageSchema.COLUMN_BODY, message.getBody());
		values.put(MessageSchema.COLUMN_FROM, message.getFrom());
		values.put(MessageSchema.COLUMN_TO, message.getTo());
		values.put(MessageSchema.COLUMN_PENDING, message.isPending() ? 0 : 1);
		values.put(MessageSchema.COLUMN_TIMESTAMP, message.getTimestamp());

		long insertId = database.insert(MessageSchema.TABLE_NAME, null, values);

		Cursor cursor = database.query(MessageSchema.TABLE_NAME,
				MessageSchema.COLUMNS, MessageSchema.COLUMN_ID + " = "
						+ insertId, null, null, null, null);

		try {
			if (!cursor.moveToFirst()) {
				throw new RuntimeException("message not written to database");
			} else {
				message.setId(insertId);
			}
		} finally {
			cursor.close();
		}
	}

	public List<Message> getMessages() {
		Cursor cursor = database.query(MessageSchema.TABLE_NAME,
				MessageSchema.COLUMNS, null, null, null, null, null);

		List<Message> messages = new ArrayList<Message>(cursor.getCount());
		try {
			if (!cursor.moveToFirst()) {
				// throw new RuntimeException("no messages in database");
			}

			do {
				Message message = Message.fromCursor(cursor);
				messages.add(message);
			} while (cursor.moveToNext());
		} finally {
			cursor.close();
		}

		return messages;
	}

	public void persistContact(Contact contact) {
		ContentValues values = new ContentValues();
		values.put(ContactSchema.COLUMN_ADDRESS, contact.getAddress());
		values.put(ContactSchema.COLUMN_NAME, contact.getName());

		long insertId = database.insert(ContactSchema.TABLE_NAME, null, values);

		Cursor cursor = database.query(ContactSchema.TABLE_NAME,
				ContactSchema.COLUMNS, ContactSchema.COLUMN_ID + " = "
						+ insertId, null, null, null, null);

		try {
			if (!cursor.moveToFirst()) {
				throw new RuntimeException("contact not written to database");
			} else {
				contact.setId(insertId);
			}
		} finally {
			cursor.close();
		}
	}

	public Contact getContact(String address) {
		Cursor cursor = database.query(ContactSchema.TABLE_NAME,
				ContactSchema.COLUMNS, ContactSchema.COLUMN_ADDRESS + " = ?",
				new String[] { address }, null, null, null);

		try {
			if (cursor.moveToFirst())
				return Contact.fromCursor(cursor);
		} finally {
			cursor.close();
		}

		return null;
	}
}
