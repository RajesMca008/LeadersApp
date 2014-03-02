package com.rnv.mediaapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask; 
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;

public class SignUp extends BaseActionBarActivity implements OnClickListener{
	
	private SignUp mContext;
	private EditText Editname, Editmail, Editpwd, Editmobno, Editcity;
	private Button Submit; 
	private RelativeLayout rel=null;
	boolean value = false;
	boolean checkedvalue;
	String storedUname;
	Cursor c;
	public static boolean regflag=true;
	JSONparser jsonParser=new JSONparser();
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		 mContext=this;
		 
		 Editname = (EditText)mContext.findViewById(R.id.EditName);
			Editmail = (EditText)mContext.findViewById(R.id.EditEmail);
			Editpwd = (EditText)mContext.findViewById(R.id.EditPwd);
			Editmobno = (EditText)mContext.findViewById(R.id.EditMobno);
			Editcity=(EditText)mContext.findViewById(R.id.Editplace);
			rel=(RelativeLayout) mContext.findViewById(R.id.rel);
			Submit = (Button)mContext.findViewById(R.id.Submit);


			Submit.setOnClickListener(this);
			rel.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	case R.id.Submit:
		if(validation()){ 
				if(Utils.isOnline(mContext)){
					
					new PostjsonsignUpdata(mContext).execute();
				}
				else{
					mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
				}}
		break;
	case R.id.rel:
		InputMethodManager imm1 = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
		imm1.hideSoftInputFromWindow(rel.getWindowToken(), 0);
		break;
	default:
		break;
		
		
	}
	}
	
	private boolean validation() {
		if (Editname.getText().toString().equals("")) {
			Toast.makeText(mContext, R.string.field_notempty, Toast.LENGTH_LONG).show();
		} 
		else if(Editmail.getText().toString().equals(""))
		{
			Toast.makeText(mContext, R.string.field_notempty, Toast.LENGTH_LONG).show();
		}
		else if (!emailValidator(Editmail.getText().toString())) {

			Toast.makeText(mContext, R.string.email_proper, Toast.LENGTH_LONG).show();
		}
		else if (Editpwd.getText().toString().equals("")) {
			Toast.makeText(mContext, R.string.field_notempty, Toast.LENGTH_LONG).show();
		} 
		else if (Editmobno.getText().toString().equals("")) {
			Toast.makeText(mContext, R.string.field_notempty, Toast.LENGTH_LONG)	.show();
		} 
		else {
			value = true;
		}
		return value;
	}
	private boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	
		private class PostjsonsignUpdata extends AsyncTask<String, String, JSONObject> {
			private ProgressDialog pDialog;
			private Activity act;

			public PostjsonsignUpdata(SignUp mContext) {
				act=mContext;
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(act);
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			@Override
			protected JSONObject doInBackground(String... args) {
				String sName = Editname.getText().toString();
				String sEmail = Editmail.getText().toString();
				String sPwd = Editpwd.getText().toString();
				String sMobno = Editmobno.getText().toString();
				String sPlace=Editcity.getText().toString();
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("sName", sName)); 
				params.add(new BasicNameValuePair("sEmail", sEmail));
				params.add(new BasicNameValuePair("sPwd", sPwd));
				params.add(new BasicNameValuePair("sMobileno", sMobno));
				params.add(new BasicNameValuePair("sCity", sPlace));

				 
				JSONObject json=jsonParser.makeHttpRequest(Constants.SIGNUP_DETAILS, "POST", params);
				Log.d("Create Response", json.toString());

				return json;
			}
			@Override
			protected void onPostExecute(JSONObject json) {
				pDialog.dismiss();
				try {
					int success = 0;
					int failed=0;
					if(json.toString().contains(Constants.TAG_SUCCESS))
					{
						success = json.getInt(Constants.TAG_SUCCESS);
						if (success == 1) {
							Toast.makeText(act, "Registered successfully",Toast.LENGTH_LONG).show();
						} 
						else if(success==0){
							Toast.makeText(act, json.getString("message"),Toast.LENGTH_LONG).show();
						}}
					else if(json.toString().contains(Constants.TAG_FAILURE))
					{
						failed=json.getInt(Constants.TAG_FAILURE);
						if(failed == 1)
						{
							Toast.makeText(act, json.getString("message"),Toast.LENGTH_LONG).show();
						}}
					
					Intent updateIntent = new Intent(act,LoginActivity.class);
					updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(updateIntent);
				} catch (JSONException e) {
							e.printStackTrace();}
			
			}}
	
}
