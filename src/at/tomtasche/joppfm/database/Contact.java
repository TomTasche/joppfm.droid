package at.tomtasche.joppfm.database;

import android.database.Cursor;

public class Contact {

	public static Contact fromCursor(Cursor cursor) {
		Contact contact = new Contact();
		contact.setAddress(cursor.getString(ContactSchema
				.getColumnIndex(ContactSchema.COLUMN_ADDRESS)));
		contact.setName(cursor.getString(ContactSchema
				.getColumnIndex(ContactSchema.COLUMN_NAME)));

		return contact;
	}

	private long id;
	private String address;
	private String name;

	private Contact() {
	}

	public Contact(String address, String name) {
		this(-1, address, name);
	}

	public Contact(long id, String address, String name) {
		this.id = id;
		this.address = address;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
