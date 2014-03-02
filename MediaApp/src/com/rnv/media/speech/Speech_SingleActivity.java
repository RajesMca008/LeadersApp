package com.rnv.media.speech;

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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.rnv.media.bean.CommentsBean;
import com.rnv.media.util.Constants;
import com.rnv.media.util.SharedPreferenceUtil;
import com.rnv.mediaapp.BaseActionBarActivity;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class Speech_SingleActivity extends BaseActionBarActivity implements OnClickListener,OnTouchListener, OnCompletionListener, OnBufferingUpdateListener{
	 
	private ImageButton buttonPlayPause; 
	private SeekBar seekBarProgress;
	private MediaPlayer mediaPlayer;
	private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
	private final Handler handler = new Handler();
	private TextView titletext=null;
	private String url_speech,title_text,id;
	private EditText edtcomment=null;
	private Button postbtn=null;
	private LinearLayout commentslayout=null;
	private Speech_SingleActivity mContext=null;
	JSONparser jsonParser = new JSONparser();
	private RelativeLayout header;
	ArrayList<CommentsBean> commentsbean=new ArrayList<CommentsBean>();
	CommentsBean commentsobj; 
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_customspeech);
		mContext=this; 
		initView();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.speeches_text);

		Bundle b=getIntent().getExtras();
		if(b!=null)
		{
			url_speech=b.getString("url");
			title_text=b.getString("title");
			id=b.getString("id");
		}
		titletext.setText(title_text);
		
		new GetCommentsDetails(id).execute();
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        try {
	        	mediaPlayer.stop();
	        } catch (Exception e) {

	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	private void initView() {
		buttonPlayPause = (ImageButton)findViewById(R.id.play_pausebtn);
		edtcomment=(EditText) findViewById(R.id.edtcomment);
		postbtn=(Button) findViewById(R.id.post);
		buttonPlayPause.setOnClickListener(this);
		postbtn.setOnClickListener(this);
		titletext=(TextView) findViewById(R.id.titletxt);
		seekBarProgress = (SeekBar)findViewById(R.id.seekBar1);
		commentslayout=(LinearLayout) findViewById(R.id.linear);
		header=(RelativeLayout) findViewById(R.id.header);
		
		
		seekBarProgress.setMax(99);  
		seekBarProgress.setOnTouchListener(this);
		header.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				InputMethodManager imm1 = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
				imm1.hideSoftInputFromWindow(header.getWindowToken(), 0);
			}
		});
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		
	}
	/** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
    	seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
		if (mediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    }; 
		    handler.postDelayed(notification,1000);
    	}
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.seekBar1){
			/** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
			if(mediaPlayer.isPlaying()){
		    	SeekBar sb = (SeekBar)v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_pausebtn:
			 /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
			try {
				mediaPlayer.setDataSource(url_speech); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
				mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer. 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
			
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				buttonPlayPause.setImageResource(R.drawable.ic_action_pause);
				//buttonPlayPause.setText("Pause");
			}else {
				mediaPlayer.pause();
				//buttonPlayPause.setText("Play");
				buttonPlayPause.setImageResource(R.drawable.ic_action_play);
			}
			primarySeekBarProgressUpdater();
			break;
		case R.id.post:
			String comment=edtcomment.getText().toString();
			
			if(comment.trim().equalsIgnoreCase(""))
			{
				Toast.makeText(mContext, R.string.comment_validator, Toast.LENGTH_SHORT).show();
			}
			else{
				String username=SharedPreferenceUtil.getStringFromSP(mContext, Constants.SP_LOGIN_UNCHECK_USERNAME);
				if(username!=null)
				{
					new Postdata(mContext,username,comment,id).execute();
					System.out.println("value of comment iss"+comment + "ID iss"+id);
					edtcomment.setText("");
				}}
				InputMethodManager imm=(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(postbtn.getWindowToken(), 0);
			
			break;

		default:
			break;
		}}
	
	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		seekBarProgress.setSecondaryProgress(arg1);
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		//buttonPlayPause.setText("Play");		
		buttonPlayPause.setImageResource(R.drawable.ic_action_play);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		return true; 
	}  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			if(mediaPlayer.isPlaying()){
			 try {
		        	mediaPlayer.stop();
		        } catch (Exception e) {
		        }}
			return true;
		default:
			break; 
		}
		return super.onOptionsItemSelected(item);
	}
	/** 
	 * Getting comment details for each individual speech post
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
			paramdetails.add(new BasicNameValuePair("id",idvalue));
			JSONArray json = jsonParser.makeHttpRequestforJsonarr(Constants.GET_COMMENT_SPEECH_DETAILS, "POST", paramdetails);
			return json;
		}
		@Override
		protected void onPostExecute(JSONArray json) {
			pDialog.dismiss();
			try {
				if(json!=null){
					Log.d("Create Response", json.toString());

					for (int i = 0; i < json.length(); i++) {
						commentsobj=new CommentsBean();
						JSONObject stmainobj;
						stmainobj = json.getJSONObject(i);
						String id = stmainobj.getString("id");
						String name = stmainobj.getString("name");
						String comment = stmainobj.getString("comment");
						commentsobj.setId(id);
						commentsobj.setUserName(name);
						commentsobj.setcomment(comment);
						commentsbean.add(commentsobj);
					}
				}
				 
				if(commentsbean.size()>0)
				{
					LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					TextView commentheadertxt=new TextView(mContext);
					commentheadertxt.setTextSize(18);
					commentheadertxt.setLayoutParams(lp);
					commentheadertxt.setPadding(10, 10, 10, 0);
					commentheadertxt.setHint("Comments");
					commentheadertxt.setTextColor(mContext.getResources().getColor(R.color.app_title_text));
					commentslayout.addView(commentheadertxt);
					
					for(int i=0;i<commentsbean.size();i++)
					{
						TextView nametxt=new TextView(mContext);
						nametxt.setTextSize(15);
						nametxt.setLayoutParams(lp);
						nametxt.setPadding(15, 10, 10, 0);
						nametxt.setText(commentsbean.get(i).getUserName());
						nametxt.setTextColor(mContext.getResources().getColor(R.color.app_title_text));
						commentslayout.addView(nametxt);

						TextView commenttxt=new TextView(mContext);
						commenttxt.setTextSize(12);
						commenttxt.setLayoutParams(lp);
						commenttxt.setPadding(30, 10, 10, 5);
						commenttxt.setText(commentsbean.get(i).getComment());
						commentslayout.addView(commenttxt);
						
						View border=new View(mContext);
						border.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, 1));
						border.setPadding(5, 10, 5, 5);
						border.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
						commentslayout.addView(border);
						
					}
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}}
	private class Postdata extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private String username,comment,id;
		private Activity act;

		public Postdata(Activity activity, String nameval, String commentval,
				String newsidval) {
			act=activity;
			username=nameval;
			comment=commentval;
			id=newsidval;
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
			paramdetails.add(new BasicNameValuePair("Sname",username));
			paramdetails.add(new BasicNameValuePair("SComment",comment));
			paramdetails.add(new BasicNameValuePair("id",id));
			JSONObject json=jsonParser.makeHttpRequest(Constants.POST_COMMENT_SPEECH_DETAILS,"POST",paramdetails);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try{
				int success = 0;
				if (json.toString().contains("success")) {
					success = json.getInt("success");
					if (success == 1) {
						Toast.makeText(act, "Message sent successfully", Toast.LENGTH_LONG).show();
						
						//adding new comment after posting..
						LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						
							TextView nametxt=new TextView(mContext);
							nametxt.setTextSize(15);
							nametxt.setLayoutParams(lp);
							nametxt.setPadding(15, 10, 10, 0);
							nametxt.setText(username);
							nametxt.setTextColor(mContext.getResources().getColor(R.color.app_title_text));
							commentslayout.addView(nametxt);

							TextView commenttxt=new TextView(mContext);
							commenttxt.setTextSize(12);
							commenttxt.setLayoutParams(lp);
							commenttxt.setPadding(30, 10, 10, 5);
							commenttxt.setText(comment);
							commentslayout.addView(commenttxt);
							
							View border=new View(mContext);
							border.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, 1));
							border.setPadding(5, 10, 5, 5);
							border.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
							commentslayout.addView(border);
							
						
						
					}
				}
			}catch(JSONException e)
			{
				e.printStackTrace();
			}

		}}}


	
	
	
	
	
	
	
	
	
	
	
	
	
