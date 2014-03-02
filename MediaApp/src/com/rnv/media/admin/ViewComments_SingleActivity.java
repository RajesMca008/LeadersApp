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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rnv.media.bean.Newsbean;
import com.rnv.media.bean.QueriesBean;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Trace;
import com.rnv.mediaapp.BaseActionBarActivity;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class ViewComments_SingleActivity extends BaseActionBarActivity{

	private String TAG="AdminViewComments_SingleActivity";
	private ViewPager vp=null;
	private ArrayList<QueriesBean> galleryimages=null;
	public ImageLoader imageLoader; 
	private int position=0;
	private int posval=0;
	JSONparser jsonParser = new JSONparser();

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_viewpage);
		vp=(ViewPager) findViewById(R.id.vp);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent in=getIntent();
		Bundle b=in.getExtras();
		if(b!=null)
		{
			galleryimages=(ArrayList<QueriesBean>) b.getSerializable("items");
			position=b.getInt("itempos");
		}
		ViewPagerAdapter adapter = new ViewPagerAdapter(this, galleryimages);
		vp.setAdapter(adapter);
		vp.setCurrentItem(position);
		
		new GetComments(this,galleryimages.get(position).getId()).execute();
		
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
		Activity activity;
		private ArrayList<QueriesBean> galleryimagesval;
		LayoutInflater inflater=null;
		public ViewPagerAdapter(Activity act, ArrayList<QueriesBean> galleryimages) {
			galleryimagesval = galleryimages;
			activity = act;
			imageLoader = new ImageLoader(activity);
			inflater=(LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
		}
		public int getCount() {
			return galleryimagesval.size();
		}
		public Object instantiateItem(View collection, int position) {
			posval=position; 
			final ViewHolder holder;
			View view= inflater.inflate(R.layout.viewcomments_single,null);
			holder = new ViewHolder();

			holder.title=(TextView) view.findViewById(R.id.name);
			holder.description=(TextView) view.findViewById(R.id.comment);
			
			holder.title.setText(galleryimagesval.get(position).getName());
			holder.description.setText(galleryimagesval.get(position).getText());
			
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
		TextView title;
		TextView description;
		
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
			/*case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent homeintent = new Intent(this, DashBoardActivity.class);
			homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeintent);
			return true;*/
		default:
			break; 
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}