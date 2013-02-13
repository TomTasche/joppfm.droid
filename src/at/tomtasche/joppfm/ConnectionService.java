package at.tomtasche.joppfm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import at.tomtasche.joppfm.database.MessageDataSource;
import at.tomtasche.joppfm.xmpp.ConnectionManager;

public class ConnectionService extends Service implements MessageCallback {

	private ConnectionManager manager;
	private Handler worker;
	private MessageDataSource database;

	private AuthPreferences authPreferences;

	@Override
	public void onCreate() {
		super.onCreate();

		manager = new ConnectionManager(this, this);

		HandlerThread workerThread = new HandlerThread(
				"ConnectionService - WorkerThread");
		workerThread.start();

		worker = new Handler(workerThread.getLooper());

		worker.post(new Runnable() {

			@Override
			public void run() {
				database = new MessageDataSource(ConnectionService.this);
			}
		});

		authPreferences = new AuthPreferences(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		worker.post(new Runnable() {

			@Override
			public void run() {
				String user = authPreferences.getUser();
				String password = authPreferences.getPassword();

				Log.e("smn", "user: " + user);
				Log.e("smn", "password: " + password);

				if (!manager.isConnected() && user != null && password != null) {
					database.open();

					manager.connect(user, password);
				}
			}
		});

		return Service.START_NOT_STICKY;
	}

	@Override
	public void onMessage(final String body, final String from) {
		worker.post(new Runnable() {

			@Override
			public void run() {
				if (body == null || from == null)
					return;

				database.createMessage(body, from);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		worker.post(new Runnable() {

			@Override
			public void run() {
				manager.disconnect();

				worker.getLooper().quit();
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
