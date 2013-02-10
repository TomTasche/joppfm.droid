package at.tomtasche.joppfm.xmpp.gmail;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class GmailNotificationProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GmailQueryResponse gmailQueryResp = null;
		GmailNotification gmailNotif = null;
		boolean done = false;
		GmailQueryResponse.Mailbox mailbox = null;
		GmailQueryResponse.Mailbox.MailThreadInfo mailThreadInfo = null;
		GmailQueryResponse.Mailbox.MailThreadInfo.Sender sender = null;

		// Cache the text
		String cachedText = "";

		// Mailbox attributes
		String resultTime = "";
		String totalMatched = "";
		String totalEstimated = null;
		String mailboxUrl = "";

		// MailThreadInfo attributes
		String tid = "";
		String participation = "";
		String messages = "";
		String date = "";
		String mailThreadInfoUrl = "";
		String labels = "";
		String subject = "";
		String snippet = "";

		// Sender attributes
		String name = "";
		String address = "";
		String originator = null;
		String unread = null;

		if (parser.getName().equals("new-mail")) {
			gmailNotif = new GmailNotification();
		} else {
			// Initialize the variables from the parsed XML
			resultTime = parser.getAttributeValue("", "result-time");
			totalMatched = parser.getAttributeValue("", "total-matched");
			totalEstimated = parser.getAttributeValue("", "total-estimate");
			mailboxUrl = parser.getAttributeValue("", "mailboxUrl");

			// Create a new Mailbox and add it to the GmailNotifications
			gmailQueryResp = new GmailQueryResponse();
			mailbox = new GmailQueryResponse.Mailbox();
			mailbox.setResultTime(Long.parseLong(resultTime));
			mailbox.setTotalMatched(Integer.parseInt(totalMatched));
			mailbox.setTotalEstimated(totalEstimated != null ? Boolean
					.parseBoolean(totalEstimated) : null);
			mailbox.setUrl(mailboxUrl);
			gmailQueryResp.setMailbox(mailbox);
		}

		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("mail-thread-info")) {
					// Initialize the variables from the parsed XML
					tid = parser.getAttributeValue("", "tid");
					participation = parser.getAttributeValue("",
							"participation");
					messages = parser.getAttributeValue("", "messages");
					date = parser.getAttributeValue("", "date");
					mailThreadInfoUrl = parser.getAttributeValue("", "url");

					// Create a new MailThreadInfo and add it to the Mailbox
					mailThreadInfo = new GmailQueryResponse.Mailbox.MailThreadInfo();
					mailThreadInfo.setTid(Long.parseLong(tid));
					mailThreadInfo.setParticipation(Integer
							.parseInt(participation));
					mailThreadInfo.setMessages(Integer.parseInt(messages));
					mailThreadInfo.setDate(Long.parseLong(date));
					mailThreadInfo.setUrl(mailThreadInfoUrl);
					mailbox.addMailThreadInfo(mailThreadInfo);
				} else if (parser.getName().equals("sender")) {
					// Initialize the variables from the parsed XML
					name = parser.getAttributeValue("", "name");
					address = parser.getAttributeValue("", "address");
					originator = parser.getAttributeValue("", "originator");
					unread = parser.getAttributeValue("", "unread");

					// Create a new Sender and add it to the MailThreadInfo
					sender = new GmailQueryResponse.Mailbox.MailThreadInfo.Sender();
					sender.setName(name);
					sender.setAddress(address);
					sender.setOriginator(originator != null ? Boolean
							.parseBoolean(totalEstimated) : null);
					sender.setUnread(unread != null ? Boolean
							.parseBoolean(unread) : null);
					mailThreadInfo.addSender(sender);
				}
			} else if (eventType == XmlPullParser.TEXT) {
				cachedText = parser.getText();
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("new-mail")
						|| parser.getName().equals("mailbox")) {
					done = true;
				} else if (parser.getName().equals("mail-thread-info")) {
				} else if (parser.getName().equals("sender")) {
				} else if (parser.getName().equals("labels")) {
					labels = cachedText;
					mailThreadInfo.setLabels(labels);
				} else if (parser.getName().equals("subject")) {
					subject = cachedText;
					mailThreadInfo.setSubject(subject);
				} else if (parser.getName().equals("snippet")) {
					snippet = cachedText;
					mailThreadInfo.setSnippet(snippet);
				}
			}
		}

		if (gmailNotif != null) {
			return gmailNotif;
		} else {
			return gmailQueryResp;
		}
	}
}
