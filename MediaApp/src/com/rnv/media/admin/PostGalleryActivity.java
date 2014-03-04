package com.rnv.media.admin;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.rnv.media.util.Constants;
import com.rnv.mediaapp.BaseNavDrawerActivity;
import com.rnv.mediaapp.R;

public class PostGalleryActivity extends BaseNavDrawerActivity{
	PostGalleryActivity mContext;
	PostGalleryFragment mFragment;

	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(R.string.post_images);
		mFragment = new PostGalleryFragment();
		FragmentManager fragManager = getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.add(R.id.content_frame, mFragment);
		fragTransaction.commit();
		mContext=this;
	} 
	@Override  
	public void onBackPressed() {
		//super.onBackPressed();
		showDialogFragment(Constants.EXIT);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*getMenuInflater().inflate(R.menu.main,menu);
		MenuItem item = menu.findItem(R.id.action_share);
		View actionView = item.getActionView().findViewById(R.id.imgbtnShare);*/
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	protected void onResume() {
		super.onResume();
	} 
	@SuppressWarnings("unused")
	private boolean isAlwaysExpanded() {
		return false;
	}}