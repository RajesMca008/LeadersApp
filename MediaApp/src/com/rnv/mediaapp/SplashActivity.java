package com.rnv.mediaapp;
 
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

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
		MediaApplication.getInstance().saveDeviceId(this);

		mFragment = new SplashFragment();
		FragmentManager fragManager = getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.add(R.id.fl_fragment, mFragment);
		fragTransaction.commit(); 

		// Get Version Details information
		String version = Utils.getVersionDetails(this); 
		Trace.d(TAG, "Version: "+version); 
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}