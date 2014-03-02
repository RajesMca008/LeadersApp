package com.rnv.media.gallery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rnv.media.bean.GalleryBean;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class GalleryFragment extends Fragment implements android.widget.AdapterView.OnItemClickListener{

	private static final String TAG = "GalleryFragment";
	private View mView;
	private GalleryActivity mContext;  
	private GridView imagegal=null;
	private JSONparser jsonParser=new JSONparser();
	private ArrayList<GalleryBean> galbean=new ArrayList<GalleryBean>();
	private GalleryBean galobj;
	private CustomGalleryAdapter adapter;
	private String galleryidval;
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_gallery, container, false);
		return mView; 
	}    
	@Override    
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (GalleryActivity) getActivity();
		imagegal=(GridView) mContext.findViewById(R.id.gridimages);
		if(Utils.isOnline(mContext))
		{
			new GetImagedata().execute();
		}
		else{ 
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
		imagegal.setOnItemClickListener(this);
		adapter=new CustomGalleryAdapter(mContext);
		adapter.imageLoader.clearCache();

	} 
	/**When dialog prompt enables click positive button events
	 * @param id dialog id
	 */
	public void onPositiveButtonClicked(int id) {
		switch (id) {
		case 0:
			break;
		default:
			break;
		}  
	} 
	/**When dialog prompt enables click Negative button events
	 * @param id dialog id
	 */
	public void onNegativeButtonClicked(int id) {
		switch (id) {
		case 0:
			break; 
		default: 
			break;
		}
	}
	public class GetImagedata extends AsyncTask<String, String, JSONArray>
	{
		private ProgressDialog pDialog;
		
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
			JSONArray json = jsonParser.getJSONFromUrl(Constants.GET_GALLERY_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) { 
			pDialog.dismiss();
			try {
 
				for (int i = 0; i < json.length(); i++) {
					JSONObject stmainobj;
					
					galobj=new GalleryBean(); 
					stmainobj = json.getJSONObject(i);
					galleryidval = stmainobj.getString("id");
					String title = stmainobj.getString("title");
					String description = stmainobj.getString("des");
					String subidval=stmainobj.getString("subid");

					galobj.setId(""+galleryidval);
					galobj.settitle(title);
					galobj.setdescription(description);
					galobj.setUrl1("http://www.madhavaramkantharao.com/siteadmin/media_images/images/"+galleryidval+"/"+subidval+".jpg");
					galbean.add(galobj);


				} 
				imagegal.setAdapter(new CustomGalleryAdapter(mContext,galbean));
			}catch (JSONException e) {
				e.printStackTrace();
			}

		}}
	public class  CustomGalleryAdapter extends BaseAdapter{
		private android.app.Activity activity;
		LayoutInflater li=null;
		public ImageLoader imageLoader; 
		private ArrayList<GalleryBean> gallery_list=new ArrayList<GalleryBean>();

		public CustomGalleryAdapter(GalleryActivity mContext,
				ArrayList<GalleryBean> galbean) {
			activity=mContext;
			gallery_list=galbean;
			li=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity);
		}

		public CustomGalleryAdapter(GalleryActivity mContext) {
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

			View v=convertView;
			if(v==null)
		 	v=li.inflate(R.layout.album, null);
			
			ImageView im=(ImageView) v.findViewById(R.id.galleryimage);
			TextView text=(TextView) v.findViewById(R.id.gallerytext);
			text.setText(gallery_list.get(position).getTitle());
			imageLoader.DisplayImage(gallery_list.get(position).getUrl1(), activity, im);
		 	return v;
		}}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Intent in=new Intent(mContext, Gallery_AlbumActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle b=new Bundle(); 
		b.putSerializable("items", galbean);
		b.putInt("itempos", arg2);
		b.putString("Galleryid", galbean.get(arg2).getId());
		in.putExtras(b);
		startActivity(in); 

	}}