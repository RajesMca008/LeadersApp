package com.rnv.mediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rnv.media.admin.PostNewsActivity;
import com.rnv.media.dashboard.DashBoardActivity;
import com.rnv.media.util.Constants;
import com.rnv.media.util.SharedPreferenceUtil;
import com.rnv.media.util.Trace;

/**
 * Fragment to Display the UI of splash activity and contains the ImageButton
 * click functionalities.
 * @author Venu Appasani
 * Date: 05/10/2013
 */
public class SplashFragment extends Fragment{
	private static final String TAG = "SplashFragment";
	View mView;
	SplashActivity mContext; 
	private Thread mSplashThread;	 
	private RelativeLayout splashrel;
	private Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_splash_activity, container, false);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = (SplashActivity) getActivity();
		splashrel=(RelativeLayout) mContext.findViewById(R.id.splashrel);
		// The thread to wait for splash screen events
		mSplashThread =  new Thread(){


	 		@Override
			public void run(){
				try {
					synchronized(this){
						// Wait given period of time or exit on touch
						wait(2000);
					}
				}  
				catch(InterruptedException ex){    				
				}
				mContext.finish();
				//Animation hyperspaceJump = AnimationUtils.loadAnimation(mContext, R.anim.hyperspace_jump);
				//mContext.findViewById(R.id.splashrel).startAnimation(hyperspaceJump);
				String username= SharedPreferenceUtil.getStringFromSP(mContext,Constants.SP_LOGIN_UNCHECK_USERNAME);
				String password= SharedPreferenceUtil.getStringFromSP(mContext,Constants.SP_LOGIN_UNCHECK_PASSWORD);

				Trace.d(TAG, "Shared pref values areee..."+username  +"\n "+password);

				Intent intent; 
				if(!username.equalsIgnoreCase("") && !password.equalsIgnoreCase(""))
				{ 
					if(username.matches("admin")&& password.matches("admin"))
					{
						LoginActivity.adminflag=true;
						LoginActivity.userflag=false;
						intent = new Intent();
						intent.setClass(mContext, PostNewsActivity.class);
						startActivity(intent);
					}
					else{
						LoginActivity.adminflag=false;
						LoginActivity.userflag=true;
						intent = new Intent();
						intent.setClass(mContext, DashBoardActivity.class);
						startActivity(intent);
					}
		           
				} 
				else{ 
					 intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					startActivity(intent);
				}
				mContext.stopService(intent);
			}
		};
		mSplashThread.start();
	}}