package at.tomtasche.joppfm.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.util.Log;
import at.tomtasche.joppfm.MessageCallback;
import at.tomtasche.joppfm.xmpp.gmail.GmailXmppExtension;

public class ConnectionManager {

	private SmackAndroid smack;
	private XMPPConnection connection;
	private GmailXmppExtension gmailExtension;
	private ChatXmppExtension chatExtension;
	private MessageCallback messageCallback;

	public ConnectionManager(Context context, MessageCallback messageListener) {
		this.messageCallback = messageListener;

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
			connection.login(user, password, "joppfm");
		} catch (XMPPException e) {
			throw new RuntimeException(e);
		}

		if (!connection.isConnected())
			throw new RuntimeException("Could not connect to XMPP server");

		connection.addPacketListener(new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				Log.d("smn", arg0.getFrom() + ": " + arg0.toXML());
			}
		}, null);

		gmailExtension = new GmailXmppExtension(connection);
		gmailExtension.queryMails();

		chatExtension = new ChatXmppExtension(connection);
		chatExtension.setMessageCallback(messageCallback);

		discoverServices();
	}

	private void discoverServices() {
		connection.addPacketListener(new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				Log.e("smn", arg0.toXML());
			}
		}, new PacketFilter() {

			@Override
			public boolean accept(Packet arg0) {
				return arg0.getPacketID().equals("discovery");
			}
		});

		Packet discoveryPacket = new Packet() {

			public String toXML() {
				return "<iq type='get'"
						+ " to='gmail.com'"
						+ " id='discovery'>"
						+ "<query xmlns='http://jabber.org/protocol/disco#info'/>"
						+ "</iq>";
			}
		};

		connection.sendPacket(discoveryPacket);
	}

	public void disconnect() {
		connection.disconnect();

		smack.onDestroy();
	}
}
