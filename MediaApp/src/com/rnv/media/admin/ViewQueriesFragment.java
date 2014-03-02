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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.rnv.media.bean.QueriesBean;
import com.rnv.media.dashboard.DashBoardActivity;
import com.rnv.media.dashboard.ImageLoader;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class ViewQueriesFragment extends Fragment implements OnItemClickListener{

	private static final String TAG = "Admin_ViewQueriesFragment";
	private View mView;
	private ViewQueriesActivity mContext;  
	private ListView listview=null;
	JSONparser jsonParser = new JSONparser();

	ArrayList<QueriesBean> querybean=new ArrayList<QueriesBean>();

	QueriesBean obj;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		return mView; 
	}    

	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (ViewQueriesActivity) getActivity();
		listview=(ListView) mContext.findViewById(R.id.newslist);
		//listview.setFocusable(false);
		//listview.setFocusableInTouchMode(false);
		//Checking Availability of Internet
		if(Utils.isOnline(mContext))
		{
		new GetQueriesData().execute();
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
	public class GetQueriesData extends AsyncTask<String, String, JSONArray>
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
			JSONArray json = jsonParser.getJSONFromUrl(Constants.GET_SUGGESTION_DETAILS);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();

			for (int i = 0; i < json.length(); i++) {
				JSONObject stmainobj;
				try {
					Log.d("Create Response", json.toString());
					obj=new QueriesBean();
					stmainobj = json.getJSONObject(i);
					String id = stmainobj.getString("id");
					String name = stmainobj.getString("name");
					String email = stmainobj.getString("email");
					String text=stmainobj.getString("tetx");
					obj.setId(""+id);
					obj.setName(name);
					obj.setEmail(email);
					obj.setText(text);
					querybean.add(obj);
					/*System.out.println("Json responce is....");
					System.out.println("id" + id);
					System.out.println("name" + name);
					System.out.println("email" + email);
					System.out.println("text" + text);
*/
					listview.setAdapter(new CustomAdapter(mContext,querybean));

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
			private ArrayList<QueriesBean> qlist=new ArrayList<QueriesBean>();
			private LayoutInflater li=null;
			public ImageLoader imageLoader; 
			public CustomAdapter(ViewQueriesActivity mContext, ArrayList<QueriesBean> querybean) {
				activity=mContext;
				qlist=querybean;
				li=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
				imageLoader = new ImageLoader(activity);
			}

			public CustomAdapter(DashBoardActivity mContext) {
				activity=mContext;
				imageLoader = new ImageLoader(activity);
			}

			@Override
			public int getCount() {
				return qlist.size();
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
				RelativeLayout rel=null;
				ImageView icon,rightarrow=null;
				if(v==null) 
				{
					v = li.inflate(R.layout.fragment_customlist, parent, false);
					v.setTag(R.id.newstext, v.findViewById(R.id.newstext));
					v.setTag(R.id.titletext, v.findViewById(R.id.titletext));
					v.setTag(R.id.img, v.findViewById(R.id.img));
					v.setTag(R.id.linear, v.findViewById(R.id.linear));
					v.setTag(R.id.rightArrow, v.findViewById(R.id.icon));

				}   
				newstext=(TextView) v.findViewById(R.id.newstext);
				titletext=(TextView) v.findViewById(R.id.titletext);
				icon=(ImageView) v.findViewById(R.id.img);
				rightarrow=(ImageView) v.findViewById(R.id.icon); 
				
				rel=(RelativeLayout) v.findViewById(R.id.linear);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				lp.setMargins(0, 0, 23, 0);
				rel.setLayoutParams(lp);
				
				icon.setVisibility(View.GONE);
				//rightarrow.setVisibility(View.GONE);
	            titletext.setText(qlist.get(position).getName());
	    		titletext.setTextColor(activity.getResources().getColor(R.color.app_title_text));
	    		titletext.setTextSize(16);
	    		newstext.setText(qlist.get(position).getText());
	    		newstext.setTextColor(Color.BLACK);
	    		newstext.setTextSize(12);
	    		
	    		/*//for disabling click event for view
	    		v.setEnabled(false);
	    		v.setOnClickListener(null);*/
				return v; 
			}
		}	
	} 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in=new Intent(mContext, ViewComments_SingleActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle b=new Bundle(); 
		b.putSerializable("items", querybean);
	    b.putInt("itempos", arg2);
		in.putExtras(b);
		startActivity(in); 
	}

}