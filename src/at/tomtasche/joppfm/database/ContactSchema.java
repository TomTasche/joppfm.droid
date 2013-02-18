package at.tomtasche.joppfm.database;

import android.provider.BaseColumns;

public class ContactSchema implements BaseColumns {

	public static final String TABLE_NAME = "contact";

	public static final String COLUMN_ID = _ID;
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_NAME = "name";
	// TODO: last_seen, last_status, face
	// NOTTODO: last_contact: query database for last message instead, if needed

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_ADDRESS,
			COLUMN_NAME };

	public static String buildCreateStatement() {
		return "create table " + TABLE_NAME + " (" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_ADDRESS
				+ " text not null, " + COLUMN_NAME + " text);";
	}

	public static int getColumnIndex(String name) {
		if (name.equals(COLUMN_ID))
			return 0;
		if (name.equals(COLUMN_ADDRESS))
			return 1;
		if (name.equals(COLUMN_NAME))
			return 2;

		return -1;
	}
}
