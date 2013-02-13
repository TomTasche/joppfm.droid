package at.tomtasche.joppfm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_MESSAGES = "message";
	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_FROM = "sender";

	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_MESSAGES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_BODY
			+ " text not null, " + COLUMN_FROM + " text not null);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		onCreate(db);
	}
}
