package com.rnv.media.suggestion;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rnv.media.bean.Newsbean;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;
public class SuggestFragment extends Fragment implements OnClickListener{
	private static final String TAG = "SuggestFragment";
	private View mView; 
	private SuggestActivity mContext; 
	private EditText name,email,suggestions,mobile;
	private Button submit,upload;
	private TextView image;
	private RelativeLayout rel=null;
	JSONparser jsonParser = new JSONparser();
	Newsbean newsobj;
	private Bitmap bitmap;
	private String image_str;
	private String filename; 
	private static final int PICK_IMAGE = 1; 

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_suggestion, container, false);
		return mView; 
	}     
	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (SuggestActivity) getActivity();
		name=(EditText) mContext.findViewById(R.id.EditName);
		email=(EditText) mContext.findViewById(R.id.Editemail);
		upload=(Button) mContext.findViewById(R.id.browse);
		mobile=(EditText) mContext.findViewById(R.id.Editmobile);
		image=(TextView) mContext.findViewById(R.id.Textimg);
		suggestions=(EditText) mContext.findViewById(R.id.Editsuggest);
		submit=(Button) mContext.findViewById(R.id.submit);
		rel=(RelativeLayout) mContext.findViewById(R.id.rel);
		rel.setOnClickListener(this);
		submit.setOnClickListener(this);
		upload.setOnClickListener(this);
	} 
	private boolean validatecredentials() {
		String nameval = name.getText().toString();
		String emailval = email.getText().toString();
		String mobilenum=mobile.getText().toString();
		String suggestval=suggestions.getText().toString();
		boolean validCreds = false;
		if (nameval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.please_enter_username,
					Toast.LENGTH_LONG).show();
		} 
		else if (emailval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.please_enter_email,
					Toast.LENGTH_LONG).show();
		}
		else if (!emailValidator(email.getText().toString())) {
			Toast.makeText(mContext, R.string.email_proper, Toast.LENGTH_LONG).show();
		}
		else if(mobilenum.trim().equalsIgnoreCase("")){
			Toast.makeText(mContext, R.string.please_enter_mobile,
					Toast.LENGTH_LONG).show();
		}
		else if (suggestval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.please_enter_suggestion,
					Toast.LENGTH_LONG).show();
		} 
		else {
			validCreds = true;
		}
		return validCreds;
	}
	private boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
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
	private class PostSuggestionData extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private String username,useremail,usersuggestions,mobilevalue;
		public PostSuggestionData(String nameval, String emailval,
				String suggestval, String mobilenumberval) {
			username=nameval;
			useremail=emailval;
			usersuggestions=suggestval;
			mobilevalue=mobilenumberval;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Sending...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected JSONObject doInBackground(String... args) {
			List<NameValuePair> paramdetails = new ArrayList<NameValuePair>();
			paramdetails.add(new BasicNameValuePair("Qname",username));
			paramdetails.add(new BasicNameValuePair("Qemail",useremail));
			paramdetails.add(new BasicNameValuePair("Qtext",usersuggestions));
			paramdetails.add(new BasicNameValuePair("Qnumber",mobilevalue));
			
			paramdetails.add(new BasicNameValuePair("Qimage",image_str));
			JSONObject json=jsonParser.makeHttpRequest(Constants.POST_SUGGESTION_DETAILS,"POST",paramdetails);
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
						Toast.makeText(mContext, "Message has been sent..", Toast.LENGTH_LONG).show();
					}}
				name.setText("");
				email.setText("");
				suggestions.setText("");
				mobile.setText("");
				image.setText("");

			}catch(JSONException e)
			{
				e.printStackTrace();
			}}}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if(Utils.isOnline(mContext))
			{
				if(validatecredentials())
				{
					String nameval=name.getText().toString();
					String emailval=email.getText().toString();
					String suggestval=suggestions.getText().toString();
					String mobilenumberval=mobile.getText().toString();
					new PostSuggestionData(nameval,emailval,suggestval,mobilenumberval).execute();
				}}
			else{
				mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
			}
			break;
		case R.id.rel:
			InputMethodManager imm=(InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(rel.getWindowToken(),0);
			break;
		case R.id.browse:
			selectImageFromGallery();
			break;
		default:
			break;
		} 
	}
	private void selectImageFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_IMAGE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE && null!=data ) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = mContext.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			filename = selectedImage.getLastPathSegment().toString();
			cursor.close();
			decodeFile(picturePath);
		}

	}
	private void decodeFile(String filePath) {

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		//choosenimg.setVisibility(View.VISIBLE);
		//choosenimg.setImageBitmap(bitmap);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
		image.setText(filename+".jpg");
		System.out.println("Base 64"+image_str);
		//Toast.makeText(mContext,filename, Toast.LENGTH_LONG).show();

	}




}