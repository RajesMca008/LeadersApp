package com.rnv.media.admin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rnv.media.bean.Newsbean;
import com.rnv.media.dashboard.DashBoardActivity;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class View_NewsCommentsFragment extends Fragment implements OnItemClickListener{

	private static final String TAG = "Admin_ViewCommentsFragment";
	private View mView;
	private View_NewsComments mContext;  
	private ListView listview=null;
	JSONparser jsonParser = new JSONparser();

	ArrayList<Newsbean> newsbean=new ArrayList<Newsbean>();

	Newsbean newsobj;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		return mView; 
	}    

	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (View_NewsComments) getActivity();
		listview=(ListView) mContext.findViewById(R.id.newslist);
		
		//Checking Availability of Internet
		if(Utils.isOnline(mContext))
		{
		new GetNewsData().execute();
		}
		else{
			mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
		}
		listview.setOnItemClickListener(this);
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
	
	/**
	 * Getting news data from server which is posted bu admin.
	 * @author Administrator
	 *
	 */
	public class GetNewsData extends AsyncTask<String, String, JSONArray>
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
		protected JSONArray doInBackground(String... args) {
			JSONArray json = jsonParser.getJSONFromUrl(Constants.GET_NEWS_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();

			for (int i = 0; i < json.length(); i++) {
				JSONObject stmainobj;
				try {
					Log.d("Create Response", json.toString());
					newsobj=new Newsbean();
					stmainobj = json.getJSONObject(i);
					String id = stmainobj.getString("id");
					String title = stmainobj.getString("title");
					String description = stmainobj.getString("des");
					String location=stmainobj.getString("location");
					newsobj.setId(""+id);
					//newsobj.setDescription(description);
					//newsobj.setLocation(location);
					newsobj.setTitle(title);
					//String url="http://appasanihealthcare.com/media/media_images/news/"+id+".jpg";
					//newsobj.setUrls("http://appasanihealthcare.com/media/media_images/news/"+id+".jpg");
					
					newsbean.add(newsobj);
					//System.out.println("Json responce is....");
					//System.out.println("id" + id);
					//System.out.println("title" + title);
					//System.out.println("urls" + url);
				
					listview.setAdapter(new CustomAdapter(mContext,newsbean));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}}
		/**
		 * Adapter for Listview
		 * @author Administrator
		 *
		 */
		public class CustomAdapter extends BaseAdapter{

			private android.app.Activity activity;
			private ArrayList<Newsbean> newslist=new ArrayList<Newsbean>();
			private LayoutInflater li=null;
			public ImageLoader imageLoader; 
			public CustomAdapter(View_NewsComments mContext, ArrayList<Newsbean> newsbean) {
				activity=mContext;
				newslist=newsbean;
				li=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
				imageLoader = new ImageLoader(activity);
			}

			public CustomAdapter(DashBoardActivity mContext) {
				activity=mContext;
				imageLoader = new ImageLoader(activity);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return newslist.size();
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
				TextView newstext=null;
				TextView titletext=null;
				ImageView icon=null;
				if(v==null)
				{
					v = li.inflate(R.layout.fragment_customlist, parent, false);
					v.setTag(R.id.newstext, v.findViewById(R.id.newstext));
					v.setTag(R.id.titletext, v.findViewById(R.id.titletext));
					v.setTag(R.id.img, v.findViewById(R.id.img));
				}
				newstext=(TextView) v.findViewById(R.id.newstext);
				titletext=(TextView) v.findViewById(R.id.titletext);
				icon=(ImageView) v.findViewById(R.id.img);

				newstext.setVisibility(View.GONE);
				icon.setVisibility(View.GONE);
	            titletext.setText(newslist.get(position).getTitle());
	    		titletext.setTextColor(Color.BLACK);
				return v; 
			}
		}	
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in=new Intent(mContext, ViewComments_SingleActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle b=new Bundle(); 
		b.putSerializable("items", newsbean);
	    b.putInt("itempos", arg2);
		in.putExtras(b);
		startActivity(in); 
	}

}