package at.tomtasche.joppfm.xmpp.gmail;

import org.jivesoftware.smack.packet.IQ;

// taken from: http://www.iteye.com/topic/901558
public class GmailQueryRequest extends IQ {

	private Long newerThanTime;
	private Long newThanTid;
	private String query;

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"").append("google:mail:notify").append("\"");
		if (newerThanTime != null) {
			buf.append(" newer-than-time=\"").append(newerThanTime)
					.append("\"");
		}
		if (newThanTid != null) {
			buf.append(" newer-than-tid=\"").append(newThanTid).append("\"");
		}
		if (query != null) {
			buf.append(" q=\"").append(query).append("\"");
		}
		buf.append("/>");
		return buf.toString();
	}

	public void setNewerThanTime(Long newerThanTime) {
		this.newerThanTime = newerThanTime;
	}

	public Long getNewerThanTime() {
		return newerThanTime;
	}

	public void setNewThanTid(Long newThanTid) {
		this.newThanTid = newThanTid;
	}

	public Long getNewThanTid() {
		return newThanTid;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
}
