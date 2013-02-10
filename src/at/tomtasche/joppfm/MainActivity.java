package at.tomtasche.joppfm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	private AuthPreferences authPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		authPreferences = new AuthPreferences(this);
		if (authPreferences.getUser() == null) {
			requestToken();
		} else {
			startService();
		}
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