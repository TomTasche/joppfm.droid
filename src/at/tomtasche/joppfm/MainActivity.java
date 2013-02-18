package at.tomtasche.joppfm;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.ArrayAdapter;
import at.tomtasche.joppfm.database.DatabaseManager;
import at.tomtasche.joppfm.database.Message;

public class MainActivity extends ListActivity {

	public static final String EXTRA_KEY_REAUTHORIZE = "reauthorize";

	private AuthPreferences authPreferences;
	private DatabaseManager database;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		authPreferences = new AuthPreferences(this);

		if (getIntent().getBooleanExtra(EXTRA_KEY_REAUTHORIZE, false))
			invalidateToken();

		if (authPreferences.getUser() != null
				&& authPreferences.getPassword() != null) {
			startService();
		} else {
			requestToken();
		}

		database = new DatabaseManager(this);
		database.open(false);

		HandlerThread thread = new HandlerThread("workerThread");
		thread.start();
		handler = new Handler(thread.getLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
				queryMessages();

				scheduleRequery();
			}
		});
	}

	private void scheduleRequery() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				queryMessages();

				scheduleRequery();
			}
		}, 1000);
	}

	private void queryMessages() {
		final List<Message> values = database.getMessages();

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(
						MainActivity.this, android.R.layout.simple_list_item_1,
						values);
				setListAdapter(adapter);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			requestToken();
		}
	}

	private void startService() {
		Intent intent = new Intent(this, ConnectionService.class);

		startService(intent);
	}

	private void invalidateToken() {
		AccountManager accountManager = AccountManager.get(this);
		accountManager.invalidateAuthToken("com.google",
				authPreferences.getPassword());

		authPreferences.setUser(null);
		authPreferences.setPassword(null);
	}

	private void requestToken() {
		AccountManager accountManager = AccountManager.get(this);
		Account account = accountManager.getAccountsByType("com.google")[0];

		authPreferences.setUser(account.name);

		accountManager.getAuthToken(account,
				"oauth2:https://www.googleapis.com/auth/googletalk", null,
				this, new OnTokenAcquired(), null);
	}

	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {

		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				Bundle bundle = result.getResult();

				Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
				if (launch != null) {
					startActivityForResult(launch, 0);
				} else {
					String token = bundle
							.getString(AccountManager.KEY_AUTHTOKEN);

					authPreferences.setPassword(token);

					startService();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}