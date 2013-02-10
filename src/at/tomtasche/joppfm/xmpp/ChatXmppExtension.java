package at.tomtasche.joppfm.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class ChatXmppExtension {

	private XMPPConnection connection;

	public ChatXmppExtension(XMPPConnection connection) {
		this.connection = connection;

		connection.getChatManager().addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(final Chat chat,
					final boolean createdLocally) {
				if (!createdLocally) {
					chat.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat chat, Message message) {
							Log.e("smn", message.getBody());
						}
					});
				}
			}
		});
	}
}
