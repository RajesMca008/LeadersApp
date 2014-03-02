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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rnv.media.util.Constants;
import com.rnv.media.util.Utils;
import com.rnv.mediaapp.JSONparser;
import com.rnv.mediaapp.R;

public class PostGalleryFragment extends Fragment implements OnClickListener{

	private static final String TAG = "PostGalleryFragment";
	private View mView;
	private PostGalleryActivity mContext;  
	private EditText titleedt,descriptionedt,locationedt,dateedt;
	private ImageButton dobimg;

	private EditText imagetext1,imagetext2;
	private String image1_base64,image2_base64,titlevalue,descriptionvalue,locationvalue,datevalue;
	private Button post,browse1,browse2;
	private JSONparser jsonParser = new JSONparser();
	private RelativeLayout rel; 
	private String filename;
	private Bitmap bitmap;
	private static final int PICK_IMAGE = 1; 
	private boolean browseflag1,browseflag2=false;
	int MyYear,MyMonth,MyDay,MyHour,MyMinute;
 
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_admin_postgallery, container, false);
		return mView; 
	}    

	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (PostGalleryActivity) getActivity();
		titleedt=(EditText) mContext.findViewById(R.id.Edittitle);
		descriptionedt=(EditText) mContext.findViewById(R.id.Editdescription);
		locationedt=(EditText) mContext.findViewById(R.id.Editlocation);
		dateedt=(EditText) mContext.findViewById(R.id.Editdate);
		dobimg=(ImageButton) mContext.findViewById(R.id.dobimage);
		
		post=(Button) mContext.findViewById(R.id.submit);
		browse1=(Button) mContext.findViewById(R.id.browseimg1);
		browse2=(Button) mContext.findViewById(R.id.browseimg2);
		imagetext1=(EditText) mContext.findViewById(R.id.Textimg1);
		imagetext2=(EditText) mContext.findViewById(R.id.Textimg2);
		rel=(RelativeLayout) mContext.findViewById(R.id.rel);

		browse1.setOnClickListener(this);
		post.setOnClickListener(this);
		rel.setOnClickListener(this);
		browse2.setOnClickListener(this);
		dobimg.setOnClickListener(this);
		dateedt.setOnClickListener(this);

		Calendar c=Calendar.getInstance();
		MyYear=c.get(Calendar.YEAR);
		MyMonth=c.get(Calendar.MONTH);
		MyDay=c.get(Calendar.DAY_OF_MONTH);
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
		case R.id.dobimage:
			DatePickerDialog dpd=new DatePickerDialog(mContext,myDateListener,MyYear,MyMonth,MyDay);
			dpd.show();
			break;
		case R.id.rel:
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(rel.getWindowToken(), 0);
			break; 

		case R.id.browseimg1:
			browseflag1=true;
			browseflag2=false;
			selectImageFromGallery();
			break;
		case R.id.browseimg2:
			browseflag1=false;
			browseflag2=true;
			selectImageFromGallery();
			break;
		case R.id.submit:
			if (validation()){
				titlevalue=titleedt.getText().toString();
				descriptionvalue=descriptionedt.getText().toString();
				locationvalue=locationedt.getText().toString();
				datevalue=dateedt.getText().toString();
				if(Utils.isOnline(mContext))
				{
					new Postdata(mContext,titlevalue,descriptionvalue,locationvalue,datevalue).execute();
				}
				else{
					mContext.showDialogFragment(Constants.ERROR_NO_NETWORK_DIALOG);
				}
			}
			break;
		case R.id.Editdate:
			InputMethodManager imm1 = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(dateedt.getWindowToken(), 0);
			DatePickerDialog dpd1=new DatePickerDialog(mContext,myDateListener,MyYear,MyMonth,MyDay);
			dpd1.show();
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
			if(browseflag1){
				imagetext1.setText(filename+".jpg");
			}
			else if(browseflag2)
			{
				imagetext2.setText(filename+".jpg");
			}
			cursor.close();
			decodeFile(picturePath);
		}

	}
	private class Postdata extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private String titleval,descriptionval,locationval,dateval;
		private Activity act;


		public Postdata(PostGalleryActivity mContext, String titlevalue,
				String descriptionvalue, String locationvalue, String datevalue)
		{    act=mContext;
		titleval=titlevalue;
		descriptionval=descriptionvalue;
		locationval=locationvalue;
		dateval=datevalue;
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
			paramdetails.add(new BasicNameValuePair("Ititle",titleval));
			paramdetails.add(new BasicNameValuePair("Ides",descriptionval));
			paramdetails.add(new BasicNameValuePair("Ilocation",locationval));
			paramdetails.add(new BasicNameValuePair("Idate",dateval));
			paramdetails.add(new BasicNameValuePair("Iimage1",image1_base64));
			paramdetails.add(new BasicNameValuePair("Iimage2",image2_base64));
		
			JSONObject json=jsonParser.makeHttpRequest(Constants.ADMIN_GALLERY_POST_DETAILS,"POST",paramdetails);
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
				imagetext1.setText("");
				imagetext2.setText("");
				locationedt.setText("");
				dateedt.setText("");

			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}}
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
		if(browseflag1)
		{
			image1_base64 = Base64.encodeToString(byte_arr,Base64.DEFAULT);
		}
		else if(browseflag2)
		{
			image2_base64 = Base64.encodeToString(byte_arr,Base64.DEFAULT);
		}
		//Toast.makeText(mContext,filename, Toast.LENGTH_LONG).show();
	}

	private boolean validation() {
		String titleval = titleedt.getText().toString();  
		String desval = descriptionedt.getText().toString();
		String imageval1=imagetext1.getText().toString();
		String imageval2=imagetext2.getText().toString();
		String locaval=locationedt.getText().toString();
		String dateval=dateedt.getText().toString();
		
		boolean validCreds = false;
		if (titleval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.titlevalidator,Toast.LENGTH_LONG).show();
		} 
		else if (locaval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.locationvalidator,Toast.LENGTH_LONG).show();
		} 
		else if (dateval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.datevalidator,Toast.LENGTH_LONG).show();
		} 
		else if (desval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.descriptionvalidator,Toast.LENGTH_LONG).show();
		} 
		else if (imageval1.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.imagevalidator,Toast.LENGTH_LONG).show();
		} 
		else if (imageval2.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.imagevalidator,Toast.LENGTH_LONG).show();
		}
		else {
			validCreds = true;
		}
		return validCreds; 
	}
	DatePickerDialog.OnDateSetListener myDateListener=new DatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			MyYear=year;
			MyMonth=monthOfYear;
			MyDay=dayOfMonth;
			updateDisplay();
		}

		private void updateDisplay() {
			StringBuilder sb=new StringBuilder();
			sb.append(MyYear).append("-").append(MyMonth +1).append("-").append(MyDay);
			String str=sb.toString();
			dateedt.setText(str);				
		}}; 	 
	

}