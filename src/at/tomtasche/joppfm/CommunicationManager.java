package at.tomtasche.joppfm;

import android.content.Context;
import at.tomtasche.joppfm.database.Contact;
import at.tomtasche.joppfm.database.DatabaseManager;
import at.tomtasche.joppfm.database.Message;

public class CommunicationManager {

	private DatabaseManager database;

	public void initialize(Context context) {
		database = new DatabaseManager(context);
		database.open();
	}

	public void onMessage(String body, String from, String to, boolean pending) {
		Contact fromContact = database.getContact(from);
		if (fromContact == null) {
			fromContact = new Contact(from, null);

			database.persistContact(fromContact);
		}

		Contact toContact = database.getContact(to);
		if (toContact == null) {
			toContact = new Contact(to, null);

			database.persistContact(toContact);
		}

		Message message = new Message(body, fromContact.getId(),
				toContact.getId());

		database.persistMessage(message);
	}
	
	public void close() {
		database.close();
	}
}
