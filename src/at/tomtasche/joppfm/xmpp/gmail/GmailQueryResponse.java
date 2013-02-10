package at.tomtasche.joppfm.xmpp.gmail;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.packet.IQ;

// taken from: http://www.iteye.com/topic/901558
public class GmailQueryResponse extends IQ {

	private Mailbox mailbox;

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		if (mailbox != null) {
			buf.append(mailbox.toXML());
		}
		return buf.toString();
	}

	public Mailbox getMailbox() {
		return mailbox;
	}

	public void setMailbox(Mailbox mailbox) {
		this.mailbox = mailbox;
	}

	public static class Mailbox {
		private Long resultTime;
		private Integer totalMatched;
		private Boolean totalEstimated;
		private String url;
		private List<MailThreadInfo> mailThreadInfos = new CopyOnWriteArrayList<MailThreadInfo>();

		public String toXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<mailbox xmlns=\"").append("google:mail:notify")
					.append("\"");
			buf.append(" result-time=\"").append(resultTime).append("\"");
			buf.append(" total-matched=\"").append(totalMatched).append("\"");
			if (totalEstimated != null) {
				if (totalEstimated) {
					buf.append(" total-estimate=\"").append("1").append("\"");
				} else {
					buf.append(" total-estimate=\"").append("0").append("\"");
				}
			}
			buf.append(">");
			synchronized (mailThreadInfos) {
				for (MailThreadInfo mailThreadInfo : mailThreadInfos) {
					buf.append(mailThreadInfo.toXML());
				}
			}
			buf.append("</mailbox>");
			return buf.toString();
		}

		public void addMailThreadInfo(MailThreadInfo mailThreadInfo) {
			synchronized (mailThreadInfos) {
				mailThreadInfos.add(mailThreadInfo);
			}
		}

		public Iterator<MailThreadInfo> getMailThreadInfos() {
			synchronized (mailThreadInfos) {
				return Collections.unmodifiableList(mailThreadInfos).iterator();
			}
		}

		public Long getResultTime() {
			return resultTime;
		}

		public void setResultTime(Long resultTime) {
			this.resultTime = resultTime;
		}

		public Integer getTotalMatched() {
			return totalMatched;
		}

		public void setTotalMatched(Integer totalMatched) {
			this.totalMatched = totalMatched;
		}

		public Boolean getTotalEstimated() {
			return totalEstimated;
		}

		public void setTotalEstimated(Boolean totalEstimated) {
			this.totalEstimated = totalEstimated;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public static class MailThreadInfo {
			private Long tid;
			private Integer participation;
			private Integer messages;
			private Long date;
			private String url;
			private String labels;
			private String subject;
			private String snippet;
			private List<Sender> senders = new CopyOnWriteArrayList<Sender>();

			public String toXML() {
				StringBuilder buf = new StringBuilder();
				buf.append("<mail-thread-info tid=\"").append(tid).append("\"");
				buf.append(" participation=\"").append(participation)
						.append("\"");
				buf.append(" date=\"").append(date).append("\"");
				buf.append(" url=\"").append(url).append("\"");
				buf.append(">");
				buf.append("<senders>");
				synchronized (senders) {
					for (Sender sender : senders) {
						buf.append(sender.toXML());
					}
				}
				buf.append("</senders>");
				buf.append("<labels>").append(labels).append("</labels>");
				buf.append("<subject>").append(subject).append("</subject>");
				buf.append("<snippet>").append(snippet).append("</snippet>");
				buf.append("</mail-thread-info>");
				return buf.toString();
			}

			public void addSender(Sender sender) {
				synchronized (senders) {
					senders.add(sender);
				}
			}

			public Iterator<Sender> getSenders() {
				synchronized (senders) {
					return Collections.unmodifiableList(senders).iterator();
				}
			}

			public String getLabels() {
				return labels;
			}

			public void setLabels(String labels) {
				this.labels = labels;
			}

			public String getSubject() {
				return subject;
			}

			public void setSubject(String subject) {
				this.subject = subject;
			}

			public String getSnippet() {
				return snippet;
			}

			public void setSnippet(String snippet) {
				this.snippet = snippet;
			}

			public Long getTid() {
				return tid;
			}

			public void setTid(Long tid) {
				this.tid = tid;
			}

			public Integer getParticipation() {
				return participation;
			}

			public void setParticipation(Integer participation) {
				this.participation = participation;
			}

			public Integer getMessages() {
				return messages;
			}

			public void setMessages(Integer messages) {
				this.messages = messages;
			}

			public Long getDate() {
				return date;
			}

			public void setDate(Long date) {
				this.date = date;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public static class Sender {
				private String name;
				private String address;
				private Boolean originator;
				private Boolean unread;

				public String toXML() {
					StringBuilder buf = new StringBuilder();
					buf.append("<sender name=\"").append(name).append("\"");
					buf.append(" address=\"").append(address).append("\"");
					if (originator != null) {
						if (originator) {
							buf.append(" originator=\"").append("1")
									.append("\"");
						} else {
							buf.append(" originator=\"").append("0")
									.append("\"");
						}
					}
					if (unread != null) {
						if (unread) {
							buf.append(" unread=\"").append("1").append("\"");
						} else {
							buf.append(" unread=\"").append("0").append("\"");
						}
					}
					buf.append("/>");
					return buf.toString();
				}

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public Boolean getOriginator() {
					return originator;
				}

				public void setOriginator(Boolean originator) {
					this.originator = originator;
				}

				public Boolean getUnread() {
					return unread;
				}

				public void setUnread(Boolean unread) {
					this.unread = unread;
				}
			}
		}
	}
}
