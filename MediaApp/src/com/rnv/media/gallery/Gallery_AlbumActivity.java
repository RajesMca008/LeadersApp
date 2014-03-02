package com.rnv.media.gallery;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rnv.media.bean.GalleryBean;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.BaseActionBarActivity;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class Gallery_AlbumActivity extends BaseActionBarActivity {
	private Gallery_AlbumActivity mContext;
	private GridView imagegal=null; 
	private String galleryidval;
	private JSONparser jsonParser=new JSONparser();
	private ArrayList<GalleryBean> galbean=new ArrayList<GalleryBean>();
	private GalleryBean galobj;
	private CustomGalleryAdapter adapter; 
 
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_gallery);
		mContext=this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.gallery_text);

		imagegal=(GridView)findViewById(R.id.gridimages);
		adapter=new CustomGalleryAdapter(mContext);
		adapter.imageLoader.clearCache();

 

		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			galleryidval=b.getString("Galleryid");
		}
		// Toast.makeText(mContext, "id iss"+galleryidval, Toast.LENGTH_LONG).show();

		/**
		 * Getting gallery images
		 */ 
		if(Utils.isOnline(mContext))
		{
			new GetImagedata(galleryidval).execute();
		}
		else{ 
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
		imagegal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent in=new Intent(mContext, Gallery_SingleActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle b=new Bundle(); 
				b.putSerializable("items", galbean);
				b.putInt("itempos", arg2);
				in.putExtras(b);
				startActivity(in);
			}
		});
	}

	public class  CustomGalleryAdapter extends BaseAdapter{
		private android.app.Activity activity;
		LayoutInflater li=null;
		public ImageLoader imageLoader; 
		private ArrayList<GalleryBean> gallery_list=new ArrayList<GalleryBean>();

		public CustomGalleryAdapter(Gallery_AlbumActivity mContext,
				ArrayList<GalleryBean> galbean) {
			activity=mContext;
			gallery_list=galbean;
			li=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity);
		}

		public CustomGalleryAdapter(Gallery_AlbumActivity mContext) {
			activity=mContext;
			imageLoader = new ImageLoader(activity);
		}
		@Override
		public int getCount() {
			return gallery_list.size();
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
			ImageView im=new ImageView(activity);
			imageLoader.DisplayImage(gallery_list.get(position).getUrl1(), activity, im);
			return im;
		}}
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
	public class GetImagedata extends AsyncTask<String, String, JSONArray>
	{
		private ProgressDialog pDialog;
		private String idvalue;

		public GetImagedata(String galleryidval) {

			idvalue=galleryidval;
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
		protected JSONArray doInBackground(String... params) {
			List<NameValuePair> paramdetails = new ArrayList<NameValuePair>();
			paramdetails.add(new BasicNameValuePair("Mid",idvalue));

			JSONArray json=jsonParser.makeHttpRequestforJsonarr(Constants.INNERGALLERY_DETAILS, "POST", paramdetails);

			//JSONArray json = jsonParser.getJSONFromUrl(Constants.INNERGALLERY_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
			try {
				//Log.d("Create Response", json.toString());
				//	 Toast.makeText(mContext, "Create Response"+json.toString(), Toast.LENGTH_LONG).show();

				for (int i = 0; i < json.length(); i++) {
					JSONObject stmainobj;

					galobj=new GalleryBean(); 
					stmainobj = json.getJSONObject(i);
					String idval = stmainobj.getString("picid"); 
					galobj.setId(""+idval);
					galobj.setUrl1("http://www.madhavaramkantharao.com/siteadmin/media_images/images/"+idvalue+"/"+idval+".jpg");
					galbean.add(galobj);
				} 
				imagegal.setAdapter(new CustomGalleryAdapter(mContext,galbean));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}}}
