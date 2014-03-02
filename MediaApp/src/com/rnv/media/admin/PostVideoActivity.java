package com.rnv.media.admin;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.SearchView;

import com.rnv.media.util.Constants;
import com.rnv.mediaapp.BaseNavDrawerActivity;
import com.rnv.mediaapp.R;

public class PostVideoActivity extends BaseNavDrawerActivity{
	PostVideoActivity mContext;
	PostVideoFragment mFragment;
	private SearchView mSearchView;

	private static ArrayList<String> mResults;
	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//Enable Action bar title and Navigation icons
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setCustomView(R.layout.actionbar_title);
		getSupportActionBar().setHomeButtonEnabled(true);
		//getSupportActionBar().setIcon(R.drawable.ic_abcrop_logo_inner);
		getSupportActionBar().setTitle(R.string.post_videos);
		mFragment = new PostVideoFragment();
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
	private boolean isAlwaysExpanded() {
		return false;
	}}
