package com.rnv.media.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.rnv.mediaapp.MediaApplication;
import com.rnv.mediaapp.R;

public class GCMNotificationActivity extends Activity {
 
	// label to display gcm messages
	TextView lblMessage;
	MediaApplication aController;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
  
	public static String name;
	public static String email;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gcm);
		aController = (MediaApplication) getApplicationContext();
		if (!aController.isConnectingToInternet()) {

			aController.showAlertDialog(GCMNotificationActivity.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			return;
		}
		Intent i = getIntent();

		name = i.getStringExtra("name");
		email = i.getStringExtra("email");

		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);

		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		 
		 
		lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
		
		 
		final String regId = GCMRegistrar.getRegistrationId(this);
		Toast.makeText(getApplicationContext(), "Registration id"+regId,Toast.LENGTH_LONG).show();
		if (regId.equals("")) {
			// Register with GCM
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				aController.register(this, name, email, regId); 
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						aController.register(context, name, email, regId);
						Toast.makeText(getApplicationContext(), "Registration"+name+email+regId,Toast.LENGTH_LONG).show();
						return null;
					}
					@Override 
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(getApplicationContext(), "Inside Broadcast receiver",Toast.LENGTH_LONG).show();

			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
			aController.acquireWakeLock(getApplicationContext());
			lblMessage.append(newMessage + "");
			Toast.makeText(getApplicationContext(),"Got Message: " + newMessage, Toast.LENGTH_LONG).show();
			aController.releaseWakeLock();
		}
	};
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
}
