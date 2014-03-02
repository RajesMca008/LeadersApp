package com.rnv.mediaapp;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.rnv.media.util.Constants;
import com.rnv.media.util.MediaDialogFragment;
import com.rnv.media.util.OnDialogFragmentClickListener;
import com.rnv.media.util.Utils;

/**
 * Base class for all the Activities in the project.
 *   
 */        
public class BaseActionBarActivity extends ActionBarActivity implements
OnDialogFragmentClickListener {
	private String TAG = "BaseActionBarActivity";
	private BroadcastReceiver mLogoutReceiver; // Receiver to close the activity
	@SuppressLint("NewApi")
	@Override  
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		 
		Drawable d=getResources().getDrawable(R.drawable.navbar1);  
		getSupportActionBar().setBackgroundDrawable(d);
		//getSupportActionBar().setCustomView(R.layout.actionbar_title);
		//getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		//getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		//getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		//getSupportActionBar().setBackgroundDrawable(null);
		//getSupportActionBar().setTitle(R.string.app_name);
	//	getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM | getSupportActionBar().DISPLAY_SHOW_HOME);
		registerLogoutBroadCast();
	}       
	@Override    
	protected void onDestroy() {
		super.onDestroy(); 
		unregisterReceiver(mLogoutReceiver);  
	}  
 
	@Override  
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Show the Dialog Fragment for the respective ID passed.
	 * @param id The unique state id for a dialog.
	 */
	public void showDialogFragment(int id) {
		MediaDialogFragment dialogFrag;

		switch (id) {
		case Constants.ERROR_NO_NETWORK_DIALOG:
			dialogFrag = new MediaDialogFragment(getString(R.string.error_network), getString(R.string.no_connection),
					getString(R.string.try_again), getString(R.string.ok_text), id);
			dialogFrag.show(getSupportFragmentManager(), "dialog_network_error");
			break;
		
		case Constants.EXIT:
			dialogFrag = new MediaDialogFragment("", getString(R.string.exit_message),
					getString(R.string.cancel), getString(R.string.ok_text), id);
			dialogFrag.show(getSupportFragmentManager(), "dialog_network_exit");
			break;
		default:
			break;
		}
	}
	/**
	 * Register the Logout Broadcast receiver. A broadcast is received when
	 * clicking on sign-out(in Settings).
	 */
	private void registerLogoutBroadCast() {
		// Instantiate a receiver.
		mLogoutReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// finish the activity on signout.
				finish();
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_LOGOUT_BROADCAST);
		registerReceiver(mLogoutReceiver, filter);
	} 

	/**
	 * To dismiss a specific dialog fragment in the application
	 * @param id
	 */
	public void dismissDialogFragment(int id) {
		Fragment dialog = getSupportFragmentManager().findFragmentById(id);
		if (dialog != null) {
			DialogFragment df = (DialogFragment) dialog;
			df.dismiss();
		}
	}
 
	@Override
	public void onPositiveButtonClicked(int id) {
		switch (id) {

		case Constants.ERROR_NO_NETWORK_DIALOG:
			//Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
			break;

		case Constants.EXIT:
			Utils.sendLogoutBroadcast(this);
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onNegativeButtonClicked(int id) {
		switch (id) {
		case Constants.ERROR_NO_NETWORK_DIALOG:
			break;
			
		case Constants.EXIT:
			
			break;
		default:
			break;
		}
	}

	/**
	 * Navigates the user to Dash board or user selected pervious screen 
	 */
	protected void showDashBoardOrOtherScreen() {
		// we should navigate based on user condition

	}
}