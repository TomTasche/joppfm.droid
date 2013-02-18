package at.tomtasche.joppfm;

public interface MessageCallback {

	public void onMessage(String body, String from, String to);
}
