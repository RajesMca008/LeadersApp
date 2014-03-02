package com.rnv.media.admin;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rnv.media.bean.Newsbean;
import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class PostSpeechesFragment extends Fragment implements OnClickListener {

	private static final String TAG = "Admin_PostSpeechesFragment";
	private View mView;
	private PostSpeechesActivity mContext;  
	private EditText titleedt,descriptionedt;
	private Button post,browseimage,browseaudio;
	private String image_str,titlevalue,descriptionvalue;
	private EditText imagetext,audiotext;
	private static final int PICK_IMAGE = 1; 
	private static final int PICK_AUDIO = 2; 

	private RelativeLayout rel;

	JSONparser jsonParser = new JSONparser();


	Newsbean newsobj;
	private Bitmap bitmap;
	private String filename;
	private String selectedPath;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.admin_postspeech, container, false);
		return mView; 
	}    
	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (PostSpeechesActivity) getActivity();
		titleedt=(EditText) mContext.findViewById(R.id.Edittitle);
		descriptionedt=(EditText) mContext.findViewById(R.id.Editdescription);
		post=(Button) mContext.findViewById(R.id.submit);
		browseaudio=(Button) mContext.findViewById(R.id.browseaudio);
		browseimage=(Button) mContext.findViewById(R.id.browseimage);
		imagetext=(EditText) mContext.findViewById(R.id.Textimg);
		audiotext=(EditText) mContext.findViewById(R.id.Textaudio);
		rel=(RelativeLayout) mContext.findViewById(R.id.rel);

		/*locationedt=(EditText) mContext.findViewById(R.id.Editlocation);
		dateedt=(EditText) mContext.findViewById(R.id.Editdate);
		imagefileedt=(EditText) mContext.findViewById(R.id.Editimg);
		dobimg=(ImageView) mContext.findViewById(R.id.dobimage);
		//choosenimg=(ImageView) mContext.findViewById(R.id.choosenimage);
		galimg=(ImageView) mContext.findViewById(R.id.galimage);
		MyYear=c.get(Calendar.YEAR);
		MyMonth=c.get(Calendar.MONTH);
		MyDay=c.get(Calendar.DAY_OF_MONTH);

		dobimg.setOnClickListener(this);
		imagefileedt.setOnClickListener(this);
		galimg.setOnClickListener(this);*/

		rel.setOnClickListener(this);
		post.setOnClickListener(this);
		browseaudio.setOnClickListener(this);
		browseimage.setOnClickListener(this);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel:
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(rel.getWindowToken(), 0);
			break; 
		case R.id.browseimage:
			selectImageFromGallery();
			break;
		case R.id.browseaudio:
			selectAudioFromGallery();
			break;
		case R.id.submit:
			if (validation()){
				titlevalue=titleedt.getText().toString();
				descriptionvalue=descriptionedt.getText().toString();
				if(Utils.isOnline(mContext))
				{
					new Postdata(mContext,titlevalue,descriptionvalue).execute();
				}
				else{ 
					mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
				}}
			break;
		default:
			break;
		}}
	private void selectAudioFromGallery() {

		Intent intent = new Intent();
		intent.setType("audio/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_AUDIO);
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
		else if(requestCode==PICK_AUDIO && null!=data)
		{
			System.out.println("SELECT_AUDIO");
			Uri selectedImageUri = data.getData();
			selectedPath = getPath(selectedImageUri);
			System.out.println("SELECT_AUDIO Path : " + selectedPath);
		}



	}
	private String getPath(Uri selectedImageUri) {
		// TODO Auto-generated method stub
		return null;
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);
		imagetext.setText(filename);

	}
	private class Postdata extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private String titleval,descriptionval;
		private Activity act;
		public Postdata(PostSpeechesActivity mContext, String titlevalue,
				String descriptionvalue)
		{   
			act=mContext;
			titleval=titlevalue;
			descriptionval=descriptionvalue;
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
			paramdetails.add(new BasicNameValuePair("Stitle",titleval));
			paramdetails.add(new BasicNameValuePair("Sdes",descriptionval));
			paramdetails.add(new BasicNameValuePair("Simage",image_str));

			JSONObject json=jsonParser.makeHttpRequest(Constants.ADMIN_SPEECH_POST_DETAILS,"POST",paramdetails);
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
					}
				}
				titleedt.setText("");
				descriptionedt.setText("");

				imagetext.setText("");
				audiotext.setText("");
			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean validation() {
		String titleval = titleedt.getText().toString();  
		String desval = descriptionedt.getText().toString();

		String imageval=imagetext.getText().toString();
		String audio=audiotext.getText().toString();

		boolean validCreds = false;
		if (titleval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.titlevalidator,Toast.LENGTH_LONG).show();
		} 
		else if (desval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.descriptionvalidator,Toast.LENGTH_LONG).show();
		} 
		else if (imageval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.imagevalidator,Toast.LENGTH_LONG).show();
		} 
		/*else if (audio.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.audiovalidator,Toast.LENGTH_LONG).show();
		} */
		else {
			validCreds = true;
		}
		return validCreds; 
	}




}