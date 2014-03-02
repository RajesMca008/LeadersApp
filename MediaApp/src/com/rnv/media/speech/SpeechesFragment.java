package com.rnv.media.speech;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rnv.media.bean.GalleryBean;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.gallery.GalleryActivity;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class SpeechesFragment extends Fragment implements OnItemClickListener{

	private static final String TAG = "SpeechesFragment";
	private View mView;
	private SpeechesActivity mContext;  
	private ListView lv=null;
	private JSONparser jsonParser=new JSONparser();
	private ArrayList<GalleryBean> galbean=new ArrayList<GalleryBean>();
	private GalleryBean galobj;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		return mView; 
	}     
	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (SpeechesActivity) getActivity();
		lv=(ListView) mContext.findViewById(R.id.newslist);
		lv.setOnItemClickListener(this);
		if(Utils.isOnline(mContext))
		{
			new GetSpeechData().execute();
		}
		else{
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
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
	public class GetSpeechData extends AsyncTask<String, String, JSONArray>
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
			JSONArray json = jsonParser.getJSONFromUrl(Constants.GET_SPEECH_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
			try {
				for (int i = 0; i < json.length(); i++) {
				JSONObject stmainobj;
				
					Log.d("Create Response", json.toString());
					galobj=new GalleryBean(); 
					stmainobj = json.getJSONObject(i);
					String id = stmainobj.getString("id");
					String title = stmainobj.getString("title");
					String description = stmainobj.getString("des");
					String file=stmainobj.getString("file");

					galobj.setId(""+id);
					galobj.settitle(title);
					galobj.setdescription(description);
					galobj.setSpeechUrl("http://appasanihealthcare.com/media/media_images/speech/voice/"+file);
					galbean.add(galobj);
					
				} 
				lv.setAdapter(new CustomGalleryAdapter(mContext,galbean));
			}catch (JSONException e) {
					e.printStackTrace();
			}
		}
		public class  CustomGalleryAdapter extends BaseAdapter{
			private android.app.Activity activity;
			LayoutInflater li=null;
			public ImageLoader imageLoader; 
			private ArrayList<GalleryBean> gallery_list=new ArrayList<GalleryBean>();

			public CustomGalleryAdapter(SpeechesActivity mContext,
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
				TextView destext=null;
				TextView titletext=null;
				if(v==null)
				{
					v = li.inflate(R.layout.fragment_speeches, parent, false);
					v.setTag(R.id.destext, v.findViewById(R.id.destext));
					v.setTag(R.id.titletext, v.findViewById(R.id.titletext));
				}
				destext=(TextView) v.findViewById(R.id.destext);
				titletext=(TextView) v.findViewById(R.id.titletext);

				destext.setText(gallery_list.get(position).getDescription());
				titletext.setText(gallery_list.get(position).getTitle());

				return v;
			}}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Bundle b=new Bundle(); 
		b.putString("url", galbean.get(arg2).getSpeechUrl());
		b.putString("title", galbean.get(arg2).getTitle());
		b.putString("id",galbean.get(arg2).getId());
		Intent in=new Intent(mContext, Speech_SingleActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		in.putExtras(b);
		startActivity(in); 

	}

}