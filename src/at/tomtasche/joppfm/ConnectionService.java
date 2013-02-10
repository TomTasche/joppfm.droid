package at.tomtasche.joppfm;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import at.tomtasche.joppfm.xmpp.ConnectionManager;

public class ConnectionService extends Service {

	private ConnectionManager manager;
	private Handler worker;

	private AuthPreferences authPreferences;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		manager = new ConnectionManager(this);

		HandlerThread workerThread = new HandlerThread(
				"ConnectionService - WorkerThread");
		workerThread.start();

		worker = new Handler(workerThread.getLooper());

		authPreferences = new AuthPreferences(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		worker.post(new Runnable() {

			@Override
			public void run() {
				String user = authPreferences.getUser();
				String password = authPreferences.getPassword();
				if (!manager.isConnected() && user != null && password != null)
					manager.connect(user, password);
			}
		});

		return Service.START_NOT_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
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
}
