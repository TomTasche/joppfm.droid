package at.tomtasche.joppfm.xmpp.gmail;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import android.util.Log;

public class GmailXmppExtension {

	private static final String PACKET_ID = "mail-request";

	private XMPPConnection connection;

	public GmailXmppExtension(XMPPConnection connection) {
		this.connection = connection;

		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addIQProvider("mailbox", "google:mail:notify",
				new GmailNotificationProvider());
		providerManager.addIQProvider("new-mail", "google:mail:notify",
				new GmailNotificationProvider());

		connection.addPacketListener(new GmailPacketListener(),
				new GmailPacketFilter());
	}

	public void queryMails() {
		Packet queryPacket = new Packet() {
			public String toXML() {
				return "<iq type='get' to='tomtasche@gmail.com'" + " id='"
						+ PACKET_ID + "'>"
						+ "<query xmlns='google:mail:notify' />" + "</iq>";
			}
		};

		connection.sendPacket(queryPacket);
	}

	private class GmailPacketListener implements PacketListener {

		@Override
		public void processPacket(Packet packet) {
			Log.e("smn", packet.toXML());
		}
	}

	private class GmailPacketFilter implements PacketFilter {

		@Override
		public boolean accept(Packet packet) {
			return packet.getPacketID().equals(PACKET_ID)
					|| packet.getClass().equals(GmailNotification.class);
		}
	}
}
