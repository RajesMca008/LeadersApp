package com.rnv.mediaapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.rnv.media.admin.PostNewsActivity;
import com.rnv.media.dashboard.DashBoardActivity;
import com.rnv.media.util.Constants;
import com.rnv.media.util.SharedPreferenceUtil;

public class LoginActivity extends BaseActionBarActivity implements OnClickListener{
	private LoginActivity mContext=null;


	private EditText editmail = null;
	private EditText editpwd = null;
	private Button signinbtn = null;
	private LoginButton facebooklogin=null;
	private Button signupbtn=null;

	private TextView chkRem = null;
	private RelativeLayout signin_relative_layout=null;

	public static boolean adflag_Signin=false;
	public static boolean adminflag=false;
	public static boolean userflag=false;

	static boolean _isChecked = false; 
	String storedpwd, smspwd;
	EditText emailfield; 
	Dialog d;
	JSONparser jsonParser = new JSONparser();

	public static String id; 
	public static String name; 
	public static String email;
	public static String dob;
	public static String passwordvalue;

	//For fb login
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.media:PendingAction";
	private PendingAction pendingAction = PendingAction.NONE;

	private GraphUser user;
	private enum PendingAction {
		NONE,
		POST_PHOTO,
		POST_STATUS_UPDATE 
	}
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		private String fbAccessToken;

		@SuppressWarnings("deprecation")
		@Override
		public void call(final Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
			updateUI();

			if (session.isOpened()) { 
				fbAccessToken = session.getAccessToken();
				// make request to get facebook user info
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						Log.i("fb", "fb user: "+ response.toString());
						String fbId = user.getId();
						String fbAccessTokenval = fbAccessToken;
						String fbName = user.getName();
						String firstname=user.getFirstName();
						String lastname=user.getLastName();
						adminflag=false;
						userflag=true;
						mContext.finish();
						SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_USERNAME, firstname);
						SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_PASSWORD, fbId);
						Intent in=new Intent(getApplicationContext(),DashBoardActivity.class);
						startActivity(in);
					}});}} 
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
			Log.d("HelloFacebook", "Success!");
		}
	};


	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);


		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}


		setContentView(R.layout.login);


		mContext=this;
		initview();

		setSavedCreds();
		signin_relative_layout.setOnClickListener(this);
		signupbtn.setOnClickListener(this);
		signinbtn.setOnClickListener(this);
		chkRem.setOnClickListener(this);
		passwordvalue=editpwd.getText().toString();

		//Face book user info changelistener
		facebooklogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				LoginActivity.this.user = user;
				//	Toast.makeText(getApplicationContext(), "facebooklogin called", Toast.LENGTH_LONG).show();
				updateUI();
			}
		});	 
	}

	@Override
	public void onBackPressed() {

		showDialogFragment(Constants.EXIT);

	}
	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingAction != PendingAction.NONE &&
				(exception instanceof FacebookOperationCanceledException ||
						exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(LoginActivity.this)
			.setTitle(R.string.cancelled)
			.setMessage(R.string.permission_not_granted)
			.setPositiveButton(R.string.ok, null)
			.show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			
		}
		updateUI();
	}
	protected void updateUI() {

		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		if ( user != null) {
			//Toast.makeText(this, "User is"+user.getFirstName(), Toast.LENGTH_LONG).show();
			System.out.println("User is"+user.getFirstName());
		} 

	}
	private void setSavedCreds() {
		String username=SharedPreferenceUtil.getStringFromSP(mContext, Constants.SP_LOGIN_USERNAME,"");
		String password=SharedPreferenceUtil.getStringFromSP(mContext, Constants.SP_LOGIN_PASSWORD,"");

		if (username.length() > 0 && password.length() > 0) {
			_isChecked = true;
			chkRem.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.chk_checked), null, null, null);
		} else {
			_isChecked = false;
			chkRem.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.chk_unchecked), null, null, null);
		}	


	}

	private void initview() {
		editmail = (EditText)findViewById(R.id.editEmail);
		editpwd = (EditText)findViewById(R.id.editPwd);
		signinbtn = (Button)findViewById(R.id.signinBtn);
		signupbtn=(Button)findViewById(R.id.signupBtn);
		chkRem = (TextView)findViewById(R.id.checkremember);
		facebooklogin=(LoginButton)findViewById(R.id.loginwithfb);
		signin_relative_layout=(RelativeLayout)findViewById(R.id.signin_relative_layout);
		//facebooklogin.setBackgroundResource(R.drawable.facebook);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) 
		{  
		case R.id.signinBtn:
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(signinbtn.getWindowToken(), 0);

			saveCredentials();
			Intent in;
			if (validation()){
				if(editmail.getText().toString().equalsIgnoreCase("admin") && editpwd.getText().toString().equalsIgnoreCase("admin"))
				{
					adminflag=true;
					userflag=false;
					mContext.finish();
					SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_USERNAME, editmail.getText().toString());
					SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_PASSWORD, editpwd.getText().toString());
					in= new Intent(mContext, PostNewsActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
				else if(editmail.getText().toString().equalsIgnoreCase("user") && editpwd.getText().toString().equalsIgnoreCase("user"))
				{
					adminflag=false;
					userflag=true;
					mContext.finish();
					SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_USERNAME, editmail.getText().toString());
					SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_PASSWORD, editpwd.getText().toString());
					in= new Intent(mContext, DashBoardActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
				}
				else{
					adminflag=false;
					userflag=true;

					new GetJsondata().execute();
				}}
			break;

		case R.id.checkremember:
			if (_isChecked) {
				chkRem.setCompoundDrawablesWithIntrinsicBounds(
						getResources()
						.getDrawable(R.drawable.chk_unchecked),
						null, null, null); 
				_isChecked = false;
			} else {
				chkRem.setCompoundDrawablesWithIntrinsicBounds(
						getResources().getDrawable(R.drawable.chk_checked),
						null, null, null);
				_isChecked = true;
			}
			break;
		case R.id.signin_relative_layout:
			InputMethodManager imm1 = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(signin_relative_layout.getWindowToken(), 0);
			break; 
		case R.id.signupBtn:
			/*
			try {
		        PackageInfo info = getPackageManager().getPackageInfo(
		                "com.rnv.mediaapp", 
		                PackageManager.GET_SIGNATURES);
		        for (Signature signature : info.signatures) {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		    		com.rnv.media.util.Trace.d("KEY", Base64.encodeToString(md.digest(), Base64.DEFAULT));
   Toast.makeText(mContext, "KEY"+Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
		        }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }*/
			Intent signupin=new Intent(mContext, SignUp.class);
			signupin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(signupin);

			break;
		default:  
			break;
		}		
	}
	private boolean validation() {
		String userName = editmail.getText().toString();  
		String passWord = editpwd.getText().toString();
		boolean validCreds = false;
		if (userName.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.please_enter_username,
					Toast.LENGTH_LONG).show();
		} else if (passWord.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.please_enter_pwd,
					Toast.LENGTH_LONG).show();
		} else {
			validCreds = true;
		}
		return validCreds; 
	}

	private void saveCredentials() {
		if (_isChecked) {
			SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_USERNAME, editmail.getText().toString());
			SharedPreferenceUtil.saveStringInSP(mContext,Constants.SP_LOGIN_PASSWORD,editpwd.getText().toString());
			String username = SharedPreferenceUtil.getStringFromSP(mContext,Constants.SP_LOGIN_USERNAME, "");
			System.out.println("sharedpreferences uNAmeeeeeeeeee" + username);
		}  
		else{
			SharedPreferenceUtil.clearFromSP(mContext);  
		}
	}
	/**
	 * Getting Login details
	 * 
	 */

	private class GetJsondata extends AsyncTask<String, String, JSONObject> {
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
		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sEmail", editmail
					.getText().toString()));
			params.add(new BasicNameValuePair("sPwd", editpwd.getText()
					.toString()));
			JSONObject json = jsonParser.makeHttpRequest(
					Constants.LOGIN_DETAILS, "POST", params);
			Log.d("Create Response", json.toString());
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				int success = 0;
				if (json.toString().contains(Constants.TAG_SUCCESS)) {
					success = json.getInt(Constants.TAG_SUCCESS);
					if (success == 1) { 
						mContext.finish();
						SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_USERNAME, editmail.getText().toString());
						SharedPreferenceUtil.saveStringInSP(mContext, Constants.SP_LOGIN_UNCHECK_PASSWORD, editpwd.getText().toString());
						Intent updateIntent = new Intent(mContext,DashBoardActivity.class);
						updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(updateIntent);

					} else { 
						Toast.makeText(mContext,"Invalid Email or Password",Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) { 
				e.printStackTrace();
			}}}

}
