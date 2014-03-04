package com.rnv.media.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rnv.media.bean.CommentsBean;
import com.rnv.media.bean.Newsbean;
import com.rnv.media.util.Constants;
import com.rnv.media.util.SharedPreferenceUtil;
import com.rnv.media.util.Trace;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.BaseActionBarActivity;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class DashBoard_SingleActivity extends BaseActionBarActivity{

	private ViewPager vp;
	private ArrayList<Newsbean> newsitems=null;
	private DashBoard_SingleActivity mContext;
	private int posval=0;
	int success=0;
	JSONparser jsonParser = new JSONparser();
	ViewPagerAdapter adapter;
	ArrayList<CommentsBean> commentsbean=new ArrayList<CommentsBean>();
	CommentsBean commentsobj;
	@SuppressWarnings("unchecked")
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_viewpage);
		mContext=this;
		vp=(ViewPager) findViewById(R.id.vp);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.news_text);
		
		Bundle b=getIntent().getExtras(); 
		if(b!=null)
		{
			newsitems=(ArrayList<Newsbean>) b.getSerializable("items");
			posval=b.getInt("itempos"); 
		}
		adapter = new ViewPagerAdapter(mContext, newsitems);
		vp.setAdapter(adapter);
		vp.setCurrentItem(posval); 

		adapter.imageLoader.clearCache();
		if(Utils.isOnline(mContext))
		{
			new GetCommentsDetails(newsitems.get(posval).getId()).execute();
		}
		else{
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if(Utils.isOnline(mContext))
				{
					new GetCommentsDetails(newsitems.get(arg0).getId()).execute();
				}
				else{
					mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}   

	public class ViewPagerAdapter extends PagerAdapter {

		private android.app.Activity activity;
		private ArrayList<Newsbean> newsitemsval;
		LayoutInflater inflater=null;
		public ImageLoader imageLoader ;
		
		@SuppressWarnings("static-access")
		public ViewPagerAdapter(DashBoard_SingleActivity act, ArrayList<Newsbean> galleryimages) {
			activity = act;
			newsitemsval = galleryimages; 
			imageLoader = new ImageLoader(activity);
			inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		}  
 
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		public int getCount() { 
			return newsitemsval.size(); 
		}
		public Object instantiateItem(View collection, final int position) {
			final ViewHolder holder;
 
			View view= inflater.inflate(R.layout.fragment_customlist_viewpage_news,null);
			holder = new ViewHolder();
			holder.title=(TextView) view.findViewById(R.id.titletext);
			holder.description=(TextView) view.findViewById(R.id.newstext);
			holder.img=(ImageView) view.findViewById(R.id.img);
			holder.edtcomment=(EditText)view.findViewById(R.id.edtcomment);
			holder.submit=(Button)view.findViewById(R.id.post);
			//holder.footer=(RelativeLayout)view.findViewById(R.id.footer);
			holder.header=(RelativeLayout)view.findViewById(R.id.header);
			holder.commentslayout=(LinearLayout) view.findViewById(R.id.linear);

			if(commentsbean.size()>0)
			{
				LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				TextView commentheadertxt=new TextView(mContext);
				commentheadertxt.setTextSize(18);
				commentheadertxt.setLayoutParams(lp);
				commentheadertxt.setPadding(10, 10, 10, 0);
				commentheadertxt.setHint("Comments");
				commentheadertxt.setTextColor(mContext.getResources().getColor(R.color.app_title_text));
				holder.commentslayout.addView(commentheadertxt);

				for(int i=0;i<commentsbean.size();i++)
				{
					TextView nametxt=new TextView(mContext);
					nametxt.setTextSize(15);
					nametxt.setLayoutParams(lp);
					nametxt.setPadding(15, 10, 10, 0);
					nametxt.setText(commentsbean.get(i).getUserName());
					nametxt.setTextColor(mContext.getResources().getColor(R.color.app_title_text));
					holder.commentslayout.addView(nametxt);

					
					TextView commenttxt=new TextView(mContext);
					commenttxt.setTextSize(12);
					commenttxt.setLayoutParams(lp);
					commenttxt.setPadding(30, 10, 10, 5);
					commenttxt.setText(commentsbean.get(i).getComment());
					holder.commentslayout.addView(commenttxt);
					LayoutParams lp1 = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

					
					TextView datetxt=new TextView(mContext);
					datetxt.setTextSize(12);
					lp1.gravity=Gravity.RIGHT;
					datetxt.setPadding(10, 10, 10, 5);
				//	datetxt.setGravity(Gravity.RIGHT);
					datetxt.setLayoutParams(lp1);
					datetxt.setText(commentsbean.get(i).getDate());
					holder.commentslayout.addView(datetxt);
					

					View border=new View(mContext);
					border.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, 1));
					border.setPadding(5, 10, 5, 5);
					border.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
					holder.commentslayout.addView(border);
				}
			}
			holder.title.setText(newsitemsval.get(position).getTitle());
			holder.description.setText(newsitemsval.get(position).getDescription());
			imageLoader.DisplayImage(newsitemsval.get(position).getUrls(), activity, holder.img);
			holder.header.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager im = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
					im.hideSoftInputFromWindow(holder.header.getWindowToken(),0);
				}
			});
			holder.submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String commentvaluee=holder.edtcomment.getText().toString();
					if(commentvaluee.trim().equalsIgnoreCase(""))
					{
						Toast.makeText(mContext, R.string.comment_validator, Toast.LENGTH_LONG).show();
					}
					else{
						String username=SharedPreferenceUtil.getStringFromSP(activity, Constants.SP_LOGIN_UNCHECK_USERNAME);
						if(username!=null)
						{
							new Postdata(activity,username,commentvaluee,newsitemsval.get(position).getId()).execute();
							System.out.println("value of comment iss"+commentvaluee + "ID iss"+newsitemsval.get(posval).getId());
						}}
					InputMethodManager imm=(InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(holder.submit.getWindowToken(), 0);
				} 
			});
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
		ImageView img;
		EditText edtcomment;
		Button submit; 
		RelativeLayout header;
		LinearLayout commentslayout;
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		return true; 
	}  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

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
	/**
	 * Posting comments on to server side..
	 */ 
	private class Postdata extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
		private String username,comment,newsid;
		private Activity act;

		public Postdata(Activity activity, String nameval, String commentval,
				String newsidval) {
			act=activity;
			username=nameval;
			comment=commentval;
			newsid=newsidval;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(act);
			pDialog.setMessage("Sending...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> paramdetails = new ArrayList<NameValuePair>();
			paramdetails.add(new BasicNameValuePair("Cname",username));
			paramdetails.add(new BasicNameValuePair("Comment",comment));
			paramdetails.add(new BasicNameValuePair("Newsid",newsid)); // RAJ here need to validate this 
			JSONObject json=jsonParser.makeHttpRequest(Constants.POST_COMMENT_NEWS_DETAILS,"POST",paramdetails);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try{
				if (json.toString().contains("success")) {
					success = json.getInt("success");
					new GetCommentsDetails(newsid).execute();

				}	}catch(JSONException e)
				{
					e.printStackTrace();
				}}
	}
	/** 
	 * Getting comment details for each individual news post
	 *
	 */
	private class GetCommentsDetails extends AsyncTask<String, String, JSONArray> {
		private ProgressDialog pDialog;
		private String idvalue;

		public GetCommentsDetails(String id) {
			idvalue=id;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override 
		protected JSONArray doInBackground(String... args) {
			if(commentsbean.size()>0)
			{
				commentsbean.clear();
			}
			List<NameValuePair> paramdetails = new ArrayList<NameValuePair>();
			paramdetails.add(new BasicNameValuePair("Newsid",idvalue));
			JSONArray json = jsonParser.makeHttpRequestforJsonarr(Constants.GET_COMMENT_NEWS_DETAILS, "POST", paramdetails);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
			try {
				if(json!=null){
					//Log.d("Create Response", json.toString());

					for (int i = 0; i < json.length(); i++) {
						commentsobj=new CommentsBean();
						JSONObject stmainobj;
						stmainobj = json.getJSONObject(i);
						String id = stmainobj.getString("id");
						String name = stmainobj.getString("name");
						String comment = stmainobj.getString("comment");
						String dateval=stmainobj.getString("date");
						
						Trace.d("Date", dateval);
						
						commentsobj.setId(id);
						commentsobj.setUserName(name);
						commentsobj.setcomment(comment);
						commentsobj.setDate(dateval);
						commentsbean.add(commentsobj);
					}
				}
				vp.getAdapter().notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}}
	}}