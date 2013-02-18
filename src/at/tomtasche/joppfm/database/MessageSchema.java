package at.tomtasche.joppfm.database;

import android.provider.BaseColumns;

public class MessageSchema implements BaseColumns {

	public static final String TABLE_NAME = "message";

	public static final String COLUMN_ID = _ID;
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PENDING = "pending";
	public static final String COLUMN_FROM = "from_id";
	public static final String COLUMN_TO = "to_id";

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_BODY,
			COLUMN_TIMESTAMP, COLUMN_PENDING, COLUMN_FROM, COLUMN_TO };

	public static String buildCreateStatement() {
		return "create table " + TABLE_NAME + " (" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_TIMESTAMP
				+ " integer not null, " + COLUMN_BODY + " text not null, "
				+ COLUMN_FROM + " text not null, " + COLUMN_TO
				+ " text not null, " + COLUMN_PENDING + " integer not null);";
	}

	public static int getColumnIndex(String name) {
		if (name.equals(COLUMN_ID))
			return 0;
		if (name.equals(COLUMN_BODY))
			return 1;
		if (name.equals(COLUMN_TIMESTAMP))
			return 2;
		if (name.equals(COLUMN_PENDING))
			return 3;
		if (name.equals(COLUMN_FROM))
			return 4;
		if (name.equals(COLUMN_TO))
			return 5;

		return -1;
	}
}
