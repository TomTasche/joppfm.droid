package at.tomtasche.joppfm.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;
import at.tomtasche.joppfm.MessageCallback;

public class ChatXmppExtension {

	private XMPPConnection connection;
	private MessageCallback messageCallback;

	public ChatXmppExtension(XMPPConnection connection) {
		this.connection = connection;

		Presence presence = new Presence(Presence.Type.available,
				"tomtasche.at", 24, Presence.Mode.chat);
		connection.sendPacket(presence);

		connection.getChatManager().addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new MessageListener() {

					@Override
					public void processMessage(Chat chat, Message message) {
						String from = chat.getParticipant();
						String body = message.getBody();

						Log.e("smn", from + ": " + body);

						if (messageCallback != null)
							messageCallback.onMessage(body, from);
					}
				});
			}
		});
	}

	public void setMessageCallback(MessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}
}
