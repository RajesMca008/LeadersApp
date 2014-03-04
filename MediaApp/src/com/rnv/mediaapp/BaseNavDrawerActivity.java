package com.rnv.mediaapp;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rnv.media.admin.PostGalleryActivity;
import com.rnv.media.admin.PostNewsActivity;
import com.rnv.media.admin.PostSpeechesActivity;
import com.rnv.media.admin.PostVideoActivity;
import com.rnv.media.admin.ViewQueriesActivity;
import com.rnv.media.dashboard.DashBoardActivity;
import com.rnv.media.dashboard.DashBoardFragment;
import com.rnv.media.gallery.GalleryActivity;
import com.rnv.media.speech.SpeechesActivity;
import com.rnv.media.suggestion.SuggestActivity;
import com.rnv.media.util.Constants;
import com.rnv.media.util.LeftMenuExpandableList;
import com.rnv.media.util.OnDialogFragmentClickListener;
import com.rnv.media.util.SharedPreferenceUtil;
import com.rnv.media.util.Trace;
import com.rnv.media.util.Utils;
import com.rnv.media.video.VideoActivity;

/** 
 * The Drawers which allows the user to choose an alternate parent for up
 * navigation. The Left Side drawers to open and close the navigation drawer
 * with help of ActionBarDrawerToggle and The Right Side drawers should be used
 * for actions not navigation. The BaseNavDrawerActivity provides left
 * navigation menu drawer if this navigation not required then use
 * BaseActionBarActivity as default for all activities and when we use
 * BaseNavDrawerActivity should use only "content_frame" for other fragment
 * views
 * 
 *      
 */
public class BaseNavDrawerActivity extends BaseActionBarActivity implements
OnDialogFragmentClickListener, OnClickListener {

	private final String TAG = "BaseNavDrawerActivity";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	DashBoardFragment mFragments;
	//Admin_PostNewsFragment aFragments;
	private String mSelectedItem="Madhavaram Kantharao BJP";

	private List<String> mGroupDataList; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> mChildDataList;
	private View footerView = null;
	private Context mContext;
	private String[] itemslist;
	private String[] admin_itemslist;

//	private int[] images={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basenavigation_drawer);
		
		Drawable d=getResources().getDrawable(R.drawable.navbar1);  
		getSupportActionBar().setBackgroundDrawable(d);
		mContext = this; 
		initalizeUIandActionBar(); 
		setLeftNavDrawerWidth();
		//createOrUpdateExpandableList();
		setProfilestoLeftMenuAdapter();
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		// Change sliding drawer icons and content of action navigation drawer
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mSelectedItem);
			}
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(R.string.app_name);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	} 
	/**
	 * Initialize all ui components and action bar header part ui title
	 */
	private void initalizeUIandActionBar() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(R.string.app_name);
	}
	/**
	 * Set Dynamically left Navigation drawer width
	 */ 
	private void setLeftNavDrawerWidth() {
		String devRes = Utils.getDeviceResolution(this);
		LayoutParams params = mDrawerList.getLayoutParams();
		//params.width = (int) (Integer.parseInt(devRes.substring(0, 3)) * 3.5 / 4);
		String[] splitdevRes=devRes.split("X");
		//params.width = (int) (Integer.parseInt(devRes.substring(0, 3)) * 25 / 4);
		params.width = (int) (Integer.parseInt(splitdevRes[0]) * 3 / 4);
		mDrawerList.setLayoutParams(params);
		Trace.d(TAG, "Res " + devRes +params.width); 
	}       
	/**  
	 * Set left menu navigation data and bind to list adapter
	 */
	private void setProfilestoLeftMenuAdapter() {
		new LeftMenuExpandableList(this, mGroupDataList,mChildDataList); 
		if(LoginActivity.adminflag)
		{
			itemslist=mContext.getResources().getStringArray(R.array.Home_menu_items_admin);
		}
		else{
			itemslist=mContext.getResources().getStringArray(R.array.Home_menu_items);
		}
		//itemslist=mContext.getResources().getStringArray(R.array.Home_menu_items);
		//admin_itemslist=mContext.getResources().getStringArray(R.array.Home_menu_items_admin);
		createFooterView(); 
		mDrawerList.addFooterView(footerView);
		mDrawerList.setAdapter(new Customview(mContext,itemslist));
	}
	/**       
	 * Create Footer View for expandable list and initialize all Ui and
	 * listeners.
	 */  
	private void createFooterView() {
		footerView = View.inflate(this, R.layout.left_menu_row_bottom, null);
		Button bottomView = (Button) footerView.findViewById(R.id.bottomTitle);
		/*TextView rate=(TextView) footerView.findViewById(R.id.rate);
		TextView termsofuse=(TextView) footerView.findViewById(R.id.terms);
		TextView policy=(TextView) footerView.findViewById(R.id.policy);
		rate.setOnClickListener(this);*/ 
		bottomView.setOnClickListener(this);
	}
	// DrawItemClick listeners for expandable listview in left navigation drawer
	// menu
	private class DrawerItemClickListener implements
	ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mDrawerList.setItemChecked(position, true);
			mDrawerLayout.closeDrawer(mDrawerList);
			TextView tv=(TextView)view.findViewById(R.id.left_menu_titlte);
			mSelectedItem=tv.getText().toString();
			setTitle(mSelectedItem);
			Trace.d(TAG, "In OnItem click listener"+position);
			Trace.d(TAG, "Item iss"+parent.getItemAtPosition(position));
			switch(position)
			{ 
			case 0:
				Intent in;
				if(LoginActivity.adminflag)
				{
					in=new Intent(mContext, PostNewsActivity.class);
					startActivity(in);
				}
				else{
					in=new Intent(mContext, DashBoardActivity.class);
					startActivity(in);
				}
				break; 
			case 1:
				Intent speechin;
				if(LoginActivity.adminflag)
				{
					speechin=new Intent(mContext, PostSpeechesActivity.class);
					startActivity(speechin);
				}
				else{
					speechin=new Intent(mContext, SpeechesActivity.class);
					startActivity(speechin);
				}
				break;
			case 2:
				Intent Galleryin;
				if(LoginActivity.adminflag)
				{
					Galleryin=new Intent(mContext, PostGalleryActivity.class);
					startActivity(Galleryin);
				}
				else
				{
					Galleryin=new Intent(mContext, GalleryActivity.class);
					startActivity(Galleryin);
				}
				break; 
			case 3:
				Intent videoin;
				if(LoginActivity.adminflag)
				{
					videoin=new Intent(mContext, PostVideoActivity.class);
					startActivity(videoin);	
				}
				else{
					videoin=new Intent(mContext, VideoActivity.class);
					startActivity(videoin);	
				}
				break;
			case 4:
				Intent suggest;
				if(LoginActivity.adminflag)
				{
					suggest=new Intent(mContext, ViewQueriesActivity.class);
					startActivity(suggest);	
				}
				else
				{
				suggest=new Intent(mContext, SuggestActivity.class);
				startActivity(suggest);
				}
				
				break;
			default:
				break;
			}}}
	/**
	 * TODO: If any default fragment want then call this method in oncreate when
	 * saved instance null
	 * 
	 * @param position
	 */
	private void selectItem(int position) {
		// update the main content by replacing fragments
		mFragments = new DashBoardFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.content_frame, mFragments).commit();
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		//// setTitle(mAllergyMenuList[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	} 
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public void onPositiveButtonClicked(int id) {
		super.onPositiveButtonClicked(id);
		if (mFragments != null)
			mFragments.onPositiveButtonClicked(id);
	}
	@Override
	public void onNegativeButtonClicked(int id) {
		super.onNegativeButtonClicked(id);
		if (mFragments != null)
			mFragments.onNegativeButtonClicked(id);
	}

	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		menu.findItem(R.id.action_share).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}*/




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);  
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerVisible(mDrawerList))
				mDrawerLayout.closeDrawer(mDrawerList);
			else
				mDrawerLayout.openDrawer(mDrawerList);
			break;
			/*case R.id.action_search:
			if(mDrawerLayout.isDrawerVisible(mDrawerList))
			{
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			break; 
		case R.id.share:
			if(mDrawerLayout.isDrawerVisible(mDrawerList))
			{ 
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			break;*/

		default:
			break;  
		}
		return super.onOptionsItemSelected(item);
	} 
	@Override 
	public void onBackPressed() {
		//super.onBackPressed();
		showDialogFragment(Constants.EXIT);
	} 
	@Override
	public void onClick(View v) {
		// if navigation drawer open then Close left navigation drawer.
		mDrawerLayout.closeDrawer(mDrawerList);
		switch (v.getId()) {
		case R.id.bottomTitle:
			
			Intent settingsIntent = new Intent(this,LoginActivity.class);
			settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(settingsIntent);
			SharedPreferenceUtil.clearFromSP(mContext);
			break;
		
		default:
			break;
		}}
	/**
	 * Customizing LeftMenu list view
	 * @author Administrator
	 *
	 */
	public class Customview extends BaseAdapter{
		Context con;
		private LayoutInflater inflater;
		private String[] items;
		public Customview(Context mContext, String[] itemslist) {
			con=mContext;
			inflater=(LayoutInflater)con.getSystemService(con.LAYOUT_INFLATER_SERVICE);
			items=itemslist;
		}
		@Override
		public int getCount() {
			return items.length;
		}
		@Override
		public Object getItem(int position) {
			return position;
		} 
		@Override
		public long getItemId(int position) {
			return position;
		} 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			ImageView picture;
			TextView name; 

			if(v == null) {
				v = inflater.inflate(R.layout.left_menu_row_child_list, parent, false);
				v.setTag(R.id.left_menu_image, v.findViewById(R.id.left_menu_image));
				v.setTag(R.id.left_menu_titlte, v.findViewById(R.id.left_menu_titlte));
			}

			picture = (ImageView)v.getTag(R.id.left_menu_image);
			name = (TextView)v.getTag(R.id.left_menu_titlte);

			//picture.setImageResource(R.drawable.app_inner_icon);
			name.setText(items[position]);

			return v;



		}
	}
}