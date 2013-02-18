package at.tomtasche.joppfm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import at.tomtasche.joppfm.xmpp.ConnectionManager;

public class ConnectionService extends Service implements MessageCallback,
		ConnectionStatusCallback {

	private ConnectionManager connectionManager;
	private CommunicationManager communicationManager;
	private Handler worker;
	private AuthPreferences authPreferences;

	@Override
	public void onCreate() {
		super.onCreate();

		connectionManager = new ConnectionManager(this, this, this);
		communicationManager = new CommunicationManager();

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
				if (!connectionManager.isConnected() && user != null
						&& password != null) {
					communicationManager.initialize(ConnectionService.this);

					connectionManager.connect(user, password);
				}
			}
		});

		return Service.START_STICKY;
	}

	@Override
	public void onConnectionFailed() {
		Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Connection failed")
				.setContentText("Permissions need to be granted first");

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_KEY_REAUTHORIZE, true);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(42, builder.build());

		stopSelf();
	}

	@Override
	public void onMessage(final String body, final String from, final String to) {
		worker.post(new Runnable() {

			@Override
			public void run() {
				if (body == null || from == null || to == null)
					return;

				communicationManager.onMessage(body, from, to, false);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		worker.post(new Runnable() {

			@Override
			public void run() {
				connectionManager.disconnect();

				communicationManager.close();

				worker.getLooper().quit();
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
