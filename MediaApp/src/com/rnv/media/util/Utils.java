package com.rnv.media.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rnv.mediaapp.MediaApplication;
import com.rnv.mediaapp.SplashActivity;

public class Utils {
	private final String TAG = "Utils";
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	public static ArrayList<String> startDates = new ArrayList<String>();
	public static ArrayList<String> endDates = new ArrayList<String>();
	public static ArrayList<String> descriptions = new ArrayList<String>();
	/**Get application version name using package manger when app in live
	 * OR show dummy app version if app in testing
	 * @param context app reference
	 * @param cls class package manager
	 * @return app version
	 */
	public static String getVersionName(Context context, Class<?> cls) {
		try {
			ComponentName comp = new ComponentName(context, cls);
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			return pinfo.versionName;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	/**Share any content to gmail then invoke this method and pass
	 * @param message content
	 * @param subject text
	 * @param recipient text
	 * @param context refernce
	 */
	private static void launchGmail(String message, String subject, String recipient, Context context) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");

		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(message));

		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	/**Check weather online connection avilable or not(Not used at present)
	 * @param context
	 * @return if aviable return true else false
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}


	/**If we have any list of array with commas then use this method to segregate all list content
	 * @param arr array List 
	 * @return string
	 */
	public static String createCommaSeperatedStringFromArray(ArrayList<String> arr) {
		String finalString = "";
		if (arr != null && arr.size() > 0) {
			StringBuilder commaString = new StringBuilder();
			for (String tmp : arr) {
				commaString.append(tmp + ",");
			}
			if (commaString.toString().length() > 0) {
				// remove the last comma
				finalString = commaString.toString().substring(0, commaString.toString().length() - 1);
			}
		}
		return finalString;
	}

	/**
	 * Creates an array list by splitting the string with "," as delimiter
	 * @param commaSperatedString
	 * @return	The array list of strings.
	 */
	public static ArrayList<String> createALFromCommaSeperatedString(String commaSperatedString) {
		String[] allergies = commaSperatedString.split(",");
		ArrayList<String> arrayList = new ArrayList<String>();
		for(String allergy : allergies){
			arrayList.add(allergy);
		}
		return arrayList;
	}

	/**Generate random device UUID for each user
	 * @return uuid string value
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	/**Get milliseconds from time stamp string 
	 * @param ts timestamp string
	 * @return milliseconds value
	 */
	public static String getMillisecondsFromTimeStampString(String ts) {
		if (ts == null)
			ts = "";
		if (ts.length() < 13) // this is assuming that the ts's length is 9 or
			// 13, what if its not? need to check
		{
			return ts + "0000";
		}
		return ts;
	}

	/**
	 * Get Device Resolutions at runtime
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static String getDeviceResolution(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		Point dimns = new Point();
		String deviceRes;
		if (android.os.Build.VERSION.SDK_INT >= 13) {
			// getSize method available only above API Level 13
			display.getSize(dimns);
			deviceRes = dimns.x + "X" + dimns.y;
		} else {
			deviceRes = display.getWidth() + "X" + display.getHeight();
		}
		return deviceRes;
	}

	/**
	 * Get Screen Orientation
	 */
	public static int getOrientation(Context context) {
		// TODO: Currently this works only for mobile devices, need to implement
		// for Tablets
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = display.getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			// Normal Portrait
			return Constants.ORIENTATION_PORTRAIT;
		case Surface.ROTATION_90:
			// Normal Landscape
			return Constants.ORIENTATION_LANDSCAPE;
		case Surface.ROTATION_180:
			// Reverse Portrait
			return Constants.ORIENTATION_PORTRAIT;
		default:
			// Reverse Landscape
			return Constants.ORIENTATION_LANDSCAPE;
		}
	}

	/**
	 * Get Version Details for production if not then get constant values
	 * @param activity
	 */
	public static String getVersionDetails(Activity activity) {
		// If it is production getting versionDetails from Manifest file.
		try {
			Constants.VERSION_NUMBER = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
			Constants.VERSION_CODE = String.valueOf(activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0).versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return Constants.VERSION_NUMBER;
	}

	/**
	 * Send mail & Tell about Allergy application to friends
	 * @param context
	 * @param url
	 */
	public static void sendEmailVideoTourUrl(Activity context, String title, String url) {

		String subject = "Calvary Link Sent by a Friend > " + title;
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(
				Intent.EXTRA_TEXT,
				Html.fromHtml("<br>Hi base"));
		// need this to prompts email client only
		email.setType("message/rfc822");
		context.startActivity(Intent.createChooser(email, "Choose an Email client :"));

	}
	/**
	 * Creates the file/folder
	 * @param path path of the folder/file to create
	 * @param isFolder true for folder,false for file.
	 * @return true if file/folder is successfully created,else false.
	 */
	public static boolean createFolderOrFile(String path, boolean isFolder) {

		File file = new File(path);
		// File/Folder doesnt exist on disk
		if (!file.exists()) {
			try {
				if (isFolder) { // Create folder
					return file.mkdirs();
				} else { // Create file
					file.getParentFile().mkdirs();
					return file.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // File/Folder already exist
			return true;
		}
		return false;
	}

	/**
	 * Copy Files using src to destination inbetween files
	 * @param src file path
	 * @param dst file path
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();

	}

	/**
	 * Copy Files
	 * @param src file path
	 * @param dst file path
	 * @throws IOException
	 */
	public static void copyStreamData(InputStream src, OutputStream dst) throws IOException {
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = src.read(buf)) > 0) {
			dst.write(buf, 0, len);
		}
		src.close();
		dst.close();
	}

	/**
	 * Copies the UserDatabase to SDCARD .
	 *  // TODO Method should be used only when build released to QA.
	 * @param fromPath From the Db path
	 * @param toFileName FileName . A file will be creted on SDCard with this filename.
	 */
	public static void copyDBtoSDCard(String fromPath, String toFileName) {
		try {
			String sdCardPath = Environment.getExternalStorageDirectory() + File.separator + toFileName;
			createFolderOrFile(sdCardPath, false);
			File file = new File(sdCardPath);
			File dbFile = new File(fromPath);

			copy(dbFile, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the cookies of the application.
	 */
	public static void clearCookies() {

		CookieSyncManager.createInstance(MediaApplication.getInstance());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * Shows the splash Screen i.e {@link SplashActivity}
	 * @param activity The context to start the Activity.
	 */
	public static void showSpalshScreen(Activity activity) {
		Intent intent = new Intent(activity, SplashActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * Sends the logout broadcast which causes all the Activities with parent
	 * BaseActionBarActivity to finish().
	 * @param activity The context to send broadcast.
	 */
	public static void sendLogoutBroadcast(Activity activity) {
		Intent intent = new Intent(Constants.ACTION_LOGOUT_BROADCAST);
		activity.sendBroadcast(intent);
		activity.finish();
	}


	public static ArrayList<String> readCalendarEvent(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(Uri.parse("content://com.android.calendar/events"),
						new String[] { "calendar_id", "title", "description",
								"dtstart", "dtend", "eventLocation" }, null,
						null, null);
		cursor.moveToFirst();
		// fetching calendars name
		String CNames[] = new String[cursor.getCount()];

		// fetching calendars id
		if(nameOfEvent!=null){
		nameOfEvent.clear();}
		if(startDates!=null){
		startDates.clear();}
		if(endDates!=null){
		endDates.clear();}
		if(descriptions!=null){
		descriptions.clear();}
		for (int i = 0; i < CNames.length; i++) {

			nameOfEvent.add(cursor.getString(1));
			startDates.add(getDate(Long.parseLong(cursor.getString(3))));
			endDates.add(getDate(Long.parseLong(cursor.getString(4))));
			descriptions.add(cursor.getString(2));
			CNames[i] = cursor.getString(1);
			cursor.moveToNext();

		}
		return nameOfEvent;
	}



	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	
	
	public static void shareToGMail(Activity activity,String subject, String content) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
		final PackageManager pm = activity.getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
		ResolveInfo best = null;
		for(final ResolveInfo info : matches)
			if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
				best = info;
		if (best != null)
			emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		activity.startActivity(emailIntent);
	}
	 public static void CopyStream(InputStream is, OutputStream os)
	    {
	        final int buffer_size=1024;
	        try
	        {
	            byte[] bytes=new byte[buffer_size];
	            for(;;)
	            {
	              int count=is.read(bytes, 0, buffer_size);
	              if(count==-1)
	                  break;
	              os.write(bytes, 0, count);
	            }
	        }
	        catch(Exception ex){}
	    }

}
