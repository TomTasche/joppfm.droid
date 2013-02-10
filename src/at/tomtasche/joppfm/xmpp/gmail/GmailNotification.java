package at.tomtasche.joppfm.xmpp.gmail;

import org.jivesoftware.smack.packet.IQ;

// taken from: http://www.iteye.com/topic/901558
public class GmailNotification extends IQ {
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<new-mail xmlns=\"").append("google:mail:notify")
				.append("\"");
		buf.append("/>");
		return buf.toString();
	}
}
