package com.rnv.media.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rnv.media.bean.QueriesBean;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Trace;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.BaseActionBarActivity;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class ViewComments_SingleActivity extends BaseActionBarActivity{

	private String TAG="AdminViewComments_SingleActivity";
	private ViewPager vp=null;
	private ArrayList<QueriesBean> galleryimages=null;
	public ImageLoader imageLoader; 
	private int position=0;
	JSONparser jsonParser = new JSONparser();
	ViewPagerAdapter adapter;
	private ViewComments_SingleActivity mContext=null;

	@SuppressWarnings("unchecked")
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_viewpage);
		mContext=this;
		vp=(ViewPager) findViewById(R.id.vp);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.view_comments);

		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			galleryimages=(ArrayList<QueriesBean>) b.getSerializable("items");
			position=b.getInt("itempos");
		}
		
		adapter = new ViewPagerAdapter(mContext, galleryimages);
		
		vp.setAdapter(adapter);
		vp.setCurrentItem(position);
		
		adapter.imageLoader.clearCache();
		/*if(Utils.isOnline(mContext))
		{
		new GetComments(this,galleryimages.get(position).getId()).execute();
		}
		else{
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}*/
		Trace.d(TAG, ""+galleryimages.get(position).getId());
	}

	/**
	 * Getting comments from server
	 */
	public class GetComments extends AsyncTask<String, String, JSONArray>
	{
		private String idvalue;
		private Activity context;
		private ProgressDialog pDialog;
		public GetComments(ViewComments_SingleActivity adminViewComments_SingleActivity, String id) {
			context=adminViewComments_SingleActivity;
			idvalue=id;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sending...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		} 
		@Override
		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> paramdetails = new ArrayList<NameValuePair>();
			Trace.d(TAG+"ASYNC..", ""+idvalue);

			paramdetails.add(new BasicNameValuePair("Newsid",idvalue));
			JSONArray json=jsonParser.makeHttpRequestforJsonarr(Constants.GET_COMMENT_NEWS_DETAILS,"POST",paramdetails);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
		}
	}
	public class ViewPagerAdapter extends PagerAdapter {
		
		private android.app.Activity activity;
		private ArrayList<QueriesBean> galleryimagesval;
		LayoutInflater inflater=null;
		public ImageLoader imageLoader;
		@SuppressWarnings("static-access")
		  
		public ViewPagerAdapter(ViewComments_SingleActivity act, ArrayList<QueriesBean> galleryimages) {
			galleryimagesval = galleryimages;
			activity = act;
			imageLoader = new ImageLoader(activity);
			inflater=(LayoutInflater)activity.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
		}
		public int getCount() {
			return galleryimagesval.size();
		}
		public Object instantiateItem(View collection, int position) {
			final ViewHolder holder;
			View view= inflater.inflate(R.layout.viewcomments_single,null);
			holder = new ViewHolder();

			holder.username=(TextView) view.findViewById(R.id.name);
			holder.comment=(TextView) view.findViewById(R.id.comment);
			holder.image=(ImageView) view.findViewById(R.id.img);
			
			holder.username.setText(galleryimagesval.get(position).getName());
			holder.comment.setText(galleryimagesval.get(position).getText());
			imageLoader.DisplayImage(galleryimagesval.get(position).getUrls(), activity, holder.image);

			
			((ViewPager) collection).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		} 
	}
	public class ViewHolder{
		TextView username;
		TextView comment;
		ImageView image;
		
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		return true; 
	}  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
			
		default:
			break; 
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}