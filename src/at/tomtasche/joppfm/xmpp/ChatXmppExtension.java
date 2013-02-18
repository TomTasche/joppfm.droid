package at.tomtasche.joppfm.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;
import at.tomtasche.joppfm.MessageCallback;

public class ChatXmppExtension {

	private String user;
	private XMPPConnection connection;
	private ChatManager manager;
	private MessageCallback messageCallback;

	public ChatXmppExtension(XMPPConnection connection) {
		this.connection = connection;
		this.user = connection.getUser();

		Presence presence = new Presence(Presence.Type.available,
				"tomtasche.at", 24, Presence.Mode.chat);
		connection.sendPacket(presence);

		manager = connection.getChatManager();
		manager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new MessageListener() {

					@Override
					public void processMessage(Chat chat, Message message) {
						String from = chat.getParticipant();
						String body = message.getBody();

						Log.e("smn", from + ": " + body);

						if (body == null || from == null)
							return;

						if (messageCallback != null)
							messageCallback.onMessage(body, from, user);
					}
				});
			}
		});
	}

	public void sendMessage(String to, String message) {
		try {
			manager.createChat(to, null).sendMessage(message);

			messageCallback.onMessage(message, user, to);
		} catch (XMPPException e) {
			throw new RuntimeException(e);
		}
	}

	public void setMessageCallback(MessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}
}
