package com.rnv.media.admin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class PostVideoFragment extends Fragment implements OnClickListener{

	private static final String TAG = "Admin_PostVideoFragment";
	private View mView;
	private PostVideoActivity mContext;  
	private EditText titleedt,descriptionedt;
	private static final int PICK_VIDEO = 1;
	private String image_str,titlevalue,descriptionvalue;
	private Button browsevideo;
	private EditText videotxt;
	JSONparser jsonParser = new JSONparser();
	private RelativeLayout rel;

	private Button post;
 
 
	Newsbean newsobj;
	private String filename;
	private int serverResponseCode;
	private String picturePath;

	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_admin_postvideo, container, false);
		return mView; 
	}    

	@Override   
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);
		mContext = (PostVideoActivity) getActivity();
		titleedt=(EditText) mContext.findViewById(R.id.Edittitle);
		descriptionedt=(EditText) mContext.findViewById(R.id.Editdescription);
		post=(Button) mContext.findViewById(R.id.submit);
		browsevideo=(Button) mContext.findViewById(R.id.browsevideo);
		videotxt=(EditText) mContext.findViewById(R.id.videofiletxt);
		rel=(RelativeLayout) mContext.findViewById(R.id.rel);
		//locationedt=(EditText) mContext.findViewById(R.id.Editlocation);
		//dateedt=(EditText) mContext.findViewById(R.id.Editdate);
		//videofileedt=(EditText) mContext.findViewById(R.id.Editimg);
		//dobimg=(ImageView) mContext.findViewById(R.id.dobimage);
		//choosenimg=(ImageView) mContext.findViewById(R.id.choosenimage);
		//galimg=(ImageView) mContext.findViewById(R.id.galimage);


		/*Calendar c=Calendar.getInstance();
		MyYear=c.get(Calendar.YEAR);
		MyMonth=c.get(Calendar.MONTH);
		MyDay=c.get(Calendar.DAY_OF_MONTH);

		//dobimg.setOnClickListener(this);
		//videofileedt.setOnClickListener(this);
	//	galimg.setOnClickListener(this);*/

		post.setOnClickListener(this);
		browsevideo.setOnClickListener(this);
		rel.setOnClickListener(this);

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
			InputMethodManager imm1 = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(rel.getWindowToken(), 0);
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
				}
			}
			break;

		case R.id.browsevideo:
			selectImageFromGallery();
		default: 
			break;
		}
	}
	private boolean validation() {
		String titleval = titleedt.getText().toString();  
		String desval = descriptionedt.getText().toString();
		boolean validCreds = false;
		if (titleval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.titlevalidator,Toast.LENGTH_LONG).show();
		} 
		else if (desval.trim().equalsIgnoreCase("")) {
			Toast.makeText(mContext, R.string.descriptionvalidator,Toast.LENGTH_LONG).show();
		} 
		else {
			validCreds = true; 
		}
		return validCreds; 
	}
	/*DatePickerDialog.OnDateSetListener myDateListener=new DatePickerDialog.OnDateSetListener(){

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
		}}; 	 */
	private void selectImageFromGallery() {
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Video"),
				PICK_VIDEO);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_VIDEO && null!=data ) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = mContext.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			filename = selectedImage.getLastPathSegment().toString();
			videotxt.setText(filename);
			cursor.close();

			//uploadVideo(picturePath);

			int reponse=upLoad2Server(""+picturePath);
			//Toast.makeText(mContext,+reponse, Toast.LENGTH_LONG).show();

		}

	}






	/*private void uploadVideo(String videoPath)throws ParseException, IOException {

		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(YOUR_URL);

		    FileBody filebodyVideo = new FileBody(new File(videoPath));
		    StringBody title = new StringBody("Filename: " + videoPath);
		    StringBody description = new StringBody("This is a video of the agent");
		    StringBody code = new StringBody(realtorCodeStr);

		    MultipartEntity reqEntity = new MultipartEntity();
		    reqEntity.addPart("videoFile", filebodyVideo);
		    reqEntity.addPart("title", title);
		    reqEntity.addPart("description", description);
		    reqEntity.addPart("code", code);
		    httppost.setEntity(reqEntity);

		    // DEBUG
		    System.out.println( "executing request " + httppost.getRequestLine( ) );
		    HttpResponse response = httpclient.execute( httppost );
		    HttpEntity resEntity = response.getEntity( );

		    // DEBUG
		    System.out.println( response.getStatusLine( ) );
		    if (resEntity != null) {
		      System.out.println( EntityUtils.toString( resEntity ) );
		    } // end if

		    if (resEntity != null) {
		      resEntity.consumeContent( );
		    } // end if

		    httpclient.getConnectionManager( ).shutdown( );
		}



	 */
	private int upLoad2Server(String sourceFileUri) {
		String upLoadServerUri = "your remote server link";
		// String [] string = sourceFileUri;
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String responseFromServer = "";

		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			Log.e("Huzza", "Source File Does not exist");
			return 0;
		}
		try { // open a URL connection to the Servlet
			if(android.os.Build.VERSION.SDK_INT>9)
			{
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(Constants.ADMIN_VIDEOS_POST_DETAILS);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploadedfile", fileName);
			conn.setRequestProperty("Vtitle","titleval");
			conn.setRequestProperty("Vdes","descriptionval");		   

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
			Log.i("Huzza", "Initial .available : " + bytesAvailable);

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("Upload file to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
			// close streams
			Log.i("Upload file to server", fileName + " File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//this block will give the response of upload link
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				Log.i("Huzza", "RES Message: " + line);
			}
			rd.close();
		} catch (IOException ioex) {
			Log.e("Huzza", "error: " + ioex.getMessage(), ioex);
		}
		return serverResponseCode;  

	} 

	private class Postdata extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		private String titleval,descriptionval,locationval,dateval,imagestr;
		private Activity act;

		public Postdata(PostVideoActivity mContext, String titlevalue,
				String descriptionvalue) {
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
			paramdetails.add(new BasicNameValuePair("Vtitle",titleval));
			paramdetails.add(new BasicNameValuePair("Vdes",descriptionval));
			paramdetails.add(new BasicNameValuePair("uploadedfile",picturePath));

			JSONObject json=jsonParser.makeHttpRequest(Constants.ADMIN_VIDEOS_POST_DETAILS,"POST",paramdetails);
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

			}catch(JSONException e)
			{
				e.printStackTrace();
			}

		}}

}
