package com.rnv.media.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Constants class used for entire static varibles, 
 * constant values can access over here. 
 * 
 */ 
public class Constants
{ 
	//http://www.madhavaramkantharao.com/siteadmin
	
	public static String GET_NEWS_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/news.php";
	public static String GET_GALLERY_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/images.php";
	public static String GET_VIDEOS_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/video.php";
	public static String GET_SPEECH_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/speech.php";
	public static String POST_SUGGESTION_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/queries.php";
	public static String GET_SUGGESTION_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/getqueries.php";

	public static String POST_COMMENT_NEWS_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/comments.php";
	public static String GET_COMMENT_NEWS_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/getcomments.php";
	public static String POST_COMMENT_SPEECH_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/speechcomments.php";
	public static String GET_COMMENT_SPEECH_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/getspeechcomments.php";
	public static String POST_COMMENT_GALLERY_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/imagecomment.php";
	public static String GET_COMMENT_GALLERY_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/getimagecomments.php";

	
	public static String ADMIN_NEWS_POST_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/mobile_newspost.php";
	public static String ADMIN_VIDEOS_POST_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/mobile_videopost.php";
	public static String ADMIN_GALLERY_POST_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/mobile_imagespost.php";
	public static String ADMIN_SPEECH_POST_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/mobile_speechpost.php";
	public static String SIGNUP_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/signup.php";
	public static String LOGIN_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/userlogin.php";
	public static String INNERGALLERY_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/gallery.php";
	public static String FACEBOOK_DETAILS="http://www.madhavaramkantharao.com/siteadmin/services/facebooklogin.php";

	
	// reminder time for optional update 
	public static final int OPTIONAL_UPDATE_REMINDER_TIME			= 60 * 60 * 48 * 1000;

	//THE BELOW VARIABLES ARE APP SPECIFIC
	public static final String APP_NAME								= "Calvary Mission";

	public static final String APP_DIRECTORY_PATH					= "app_directory_path";

	//Animation
	public static final int SLIDE_ANIMATION_DURATION 				= 250;

	//	Shared Preferences
	public static final String SP_NAME				 				= "SharedPrefMedia";
	public static final String UUID 								= "uuid";

	//Sample Video URL link
	public static final String CALVARY_VIDEO_URL 					= "http://CalvaryMission";

	// Facebook features
	public static final int FACEBOOK_AUTHORIZE_ACTIVITY_RESULT_CODE	= 501;

	//Settings
	public static String VERSION_NUMBER								= "versinonumber";
	public static  String VERSION_CODE								= "versincode";

	public static String CONST_VERSION_CODE							= "0.1";
	public static String CONST_VERSION_NUMBER						= "1.0";

	// Facebook
	protected static final String FB_SHARE_NAME 					= "share_name";
	protected static final String FB_SHARE_CAPTION 					= "share_caption";
	protected static final String FB_SHARE_DESCRIPTION 				= "share_description";
	protected static final String FB_SHARE_LINK 					= "share_link";

	//Twitter
	public static final String TWITTER_ACCESS_TOKEN_URL 			= "http://api.twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORZE_URL 				= "https://api.twitter.com/oauth/authorize";
	public static final String TWITTER_REQUEST_URL 					= "https://api.twitter.com/oauth/request_token";

	public static String TAG_SUCCESS = "success";
	public static String TAG_FAILURE = "failed";
	//Intent Data in CalvaryMission
	public static final String FROM_NAVIGATION 						= "navigation";
	public static String SP_LOGIN_USERNAME="username";
	public static String SP_LOGIN_PASSWORD="password";
	
	public static String SP_LOGIN_UNCHECK_USERNAME="usernamevalue";
	public static String SP_LOGIN_UNCHECK_PASSWORD="passwordvalue";
	

	
	
	//Orientation Constants
	public static final int ORIENTATION_PORTRAIT					= 0;
	public static final int ORIENTATION_LANDSCAPE					= 1;

	//location access
	public static boolean IS_LOCATION_ENABLED 						= false;

	//E-mail constants
	public static String MSG_BODY 									= null;
	public static String MSG_SENDTO 								= "calvarymission@calvary.com";    //email will be sent to this account
	public static String MSG_SUBJECT_TAG 							= null;

	//Dialogs id
	public static final int ERROR_NO_NETWORK_DIALOG					= 200;
	
	public static final int EXIT     								= 300;

	//Logout Actions
	public static final String ACTION_LOGOUT_BROADCAST      		= "com.example.media.LOGOUT"; 



	//For displaying videos
	
	
	
	public  Bitmap loadImage(String URL, BitmapFactory.Options options)
	{       
		Bitmap bitmap = null;
		InputStream in = null;       
		try {
			in = OpenHttpConnection(URL);

			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();  
		}
		catch (IOException e1) 
		{
		}
		catch (NullPointerException e1) 
		{
		}
		catch (OutOfMemoryError e1) 
		{
		}
		return bitmap;               
	}
	private static InputStream OpenHttpConnection(String strURL) throws IOException{
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try{
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				inputStream = httpConn.getInputStream();
			}
		}
		catch (Exception ex)
		{

		}
		return inputStream;
	}


//Push
	public static  String DEVICETOKEN = "";
	public static boolean C2DM_ACTIVITY_FOCUS_CHECK=false; 
	public static final String EXTRA_ALERT_DATA="alertData";
	public static final int LOG_LEVEL = Log.VERBOSE;
	public static boolean PUSH_ENABLED = true;
	
		






}