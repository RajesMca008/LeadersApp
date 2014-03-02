package com.rnv.mediaapp;
 
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.rnv.media.util.Trace;
import com.rnv.media.util.Utils;
  
/** 
 * This is initial start-up screen will be shown on application launch. And is
 * responsible for performing version, if any other task in
 * background.
 *  
 */
public class SplashActivity extends ActionBarActivity{

	private final String TAG = "SplashActivity";
	private SplashFragment mFragment;
	MediaApplication aController;

	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	@SuppressLint("NewApi") 
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setTheme(R.style.Theme_AppCompat_Light);
		//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			//getActionBar().hide();
			getSupportActionBar().hide();
			
		//}
		setContentView(R.layout.activity_fragment);
		
		aController = (MediaApplication) getApplicationContext();
		if (!aController.isConnectingToInternet()) {

			aController.showAlertDialog(this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			return;
		}
		
		MediaApplication.getInstance().saveDeviceId(this);
		
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);

		} catch (Exception e) {
			Log.e("Error", e.toString());
		}
		 
		registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
		 
		final String regId = GCMRegistrar.getRegistrationId(this);
		//Toast.makeText(getApplicationContext(), "Registration id"+regId,Toast.LENGTH_LONG).show();
		if (regId.equals("")) {
			// Register with GCM
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				//aController.register(this, "", "", regId); 
				Log.d(TAG, "regsTest"+regId);
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						aController.register(context, "", "", regId);
						//Toast.makeText(getApplicationContext(), "Registration"+""+""+regId,Toast.LENGTH_LONG).show();
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
		mFragment = new SplashFragment();
		FragmentManager fragManager = getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.add(R.id.fl_fragment, mFragment);
		fragTransaction.commit(); 

		// Get Version Details information
		String version = Utils.getVersionDetails(this); 
		Trace.d(TAG, "Version: "+version); 
	}
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//Toast.makeText(getApplicationContext(), "Inside Broadcast receiver",Toast.LENGTH_LONG).show();

			//String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
			aController.acquireWakeLock(getApplicationContext());
			//Toast.makeText(getApplicationContext(),"Got Message: " + newMessage, Toast.LENGTH_LONG).show();
			aController.releaseWakeLock();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}
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