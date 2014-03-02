package com.rnv.mediaapp;

public interface Config {

	// CONSTANTS
	static final String YOUR_SERVER_URL = "http://appasanihealthcare.com/gcm/register.php";

	// Google project id 
	static final String GOOGLE_SENDER_ID = "674154769071";
 
	//API KEY
	static final String GOOGLE_API_KEY="AIzaSyA-kgMdOVaaa_YHt1V9rDVtldr7s6fW_pM";
	
	static String regId="";
	
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Android Example";
	static final String DISPLAY_MESSAGE_ACTION = "com.androidexample.gcm.DISPLAY_MESSAGE";
	static final String EXTRA_MESSAGE = "message";

}
