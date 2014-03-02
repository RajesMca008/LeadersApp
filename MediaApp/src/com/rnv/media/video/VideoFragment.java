package com.rnv.media.video;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.rnv.media.adapter.YoutubeADapter;
import com.rnv.media.bean.GalleryBean;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Trace;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class VideoFragment extends Fragment{

	private static final String TAG = "VideoFragment";
	private View mView;
	private VideoActivity mContext;   
	private GridView video_grid=null;
	private JSONparser jsonParser=new JSONparser();
	private ArrayList<GalleryBean> galbean=new ArrayList<GalleryBean>();
	private GalleryBean galobj;
	private YoutubeADapter youtubeadd;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_video, container, false);
		return mView; 
	}    
	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (VideoActivity) getActivity();
		video_grid=(GridView) mContext.findViewById(R.id.video_grid);
		if(Utils.isOnline(mContext))
		{
			new GetVideodata().execute();
		}
		else{ 
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
		//adapter=new CustomGalleryAdapter(mContext);

		//
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
	public class GetVideodata extends AsyncTask<String, String, JSONArray>
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
			JSONArray json = jsonParser.getJSONFromUrl(Constants.GET_VIDEOS_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
			for (int i = 0; i < json.length(); i++) {
				JSONObject stmainobj;
				try { 
					Log.d("Create Response", json.toString());
					galobj=new GalleryBean(); 
					stmainobj = json.getJSONObject(i);
					String id = stmainobj.getString("id");
					String title = stmainobj.getString("title");
					String description = stmainobj.getString("des");
					String file=stmainobj.getString("file");

					galobj.setId(""+id);
					galobj.settitle(title);
					galobj.setVideoUrl(file);
					galobj.setdescription(description);
					galbean.add(galobj);

					youtubeadd = new YoutubeADapter(mContext,galbean);
					video_grid.setAdapter(youtubeadd);
					video_grid.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							displayVideo(arg2);
						}
					});
					video_grid.invalidateViews();

				} catch (JSONException e) {
				e.printStackTrace();
				}		
			}
		}
		protected void displayVideo(int position) {
			try {
				String videoUrl = galbean.get(position).getVideoUrl();
				if(videoUrl != null && videoUrl.length() > 0){
					String[] videoValues = videoUrl.split("=");
					String videoId = videoValues[1];
					Trace.d(TAG, "Test Video Url: "+videoUrl + "Video Id "+videoId); 
					if (videoId == null || videoId.trim().equals("")) {
						return;
					}
					Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + videoId),mContext, OpenYouTubePlayerActivity.class);
					startActivity(lVideoIntent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}}}
