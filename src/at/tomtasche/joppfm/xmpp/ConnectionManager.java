package at.tomtasche.joppfm.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import at.tomtasche.joppfm.xmpp.gmail.GmailXmppExtension;

public class ConnectionManager {

	private SmackAndroid smack;
	private XMPPConnection connection;
	private GmailXmppExtension gmailExtension;
	private ChatXmppExtension chatExtension;

	public ConnectionManager(Context context) {
		smack = SmackAndroid.init(context);

		SASLAuthentication.registerSASLMechanism(GTalkOAuthSASLMechanism.NAME,
				GTalkOAuthSASLMechanism.class);
		SASLAuthentication
				.supportSASLMechanism(GTalkOAuthSASLMechanism.NAME, 0);

		ConnectionConfiguration configuration = new ConnectionConfiguration(
				"talk.google.com", 5222, "gmail.com");
		configuration.setSASLAuthenticationEnabled(true);

		connection = new XMPPConnection(configuration);
	}

	public boolean isConnected() {
		return connection.isConnected() && connection.isAuthenticated();
	}

	public void connect(String user, String password) {
		try {
			connection.connect();
			connection.login(user, password);
		} catch (XMPPException e) {
			throw new RuntimeException(e);
		}

		if (!connection.isConnected())
			throw new RuntimeException("Could not connect to XMPP server");

		gmailExtension = new GmailXmppExtension(connection);
		gmailExtension.queryMails();

		chatExtension = new ChatXmppExtension(connection);
	}

	public void disconnect() {
		connection.disconnect();

		smack.onDestroy();
	}
}
