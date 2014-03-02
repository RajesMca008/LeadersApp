package com.rnv.media.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility method for shared preferences.
 * 
 * 
 *
 */
public class SharedPreferenceUtil
{
	private static final String TAG = "SharedPreferenceUtil";

	/**
	 * Method to get the value for the specified key
	 * @param context	The context
	 * @param key	Key for the preference.
	 * @return	The Value for specified key, "" by default.
	 */
	public static String getStringFromSP (Context context, String key )
	{
		return getStringFromSP(context, key, "");
	}
	
	
	/**
	 * Retrieve boolean value from SharedPreference for the given key
	 */
	public static void saveBooleanInSP(Context context, String key, boolean value) {
		Trace.i(TAG, "Save boolean to SP");
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Retrieve boolean value from SharedPreference for the given key
	 */
	public static boolean getBooleanFromSP(Context context, String key, boolean defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		return preferences.getBoolean(key, defaultValue);
	}
	
	
	/**
	 * Retrieve string value from SharedPreference for the given key
	 * @param defValue  Value to return if this preference does not exist. 
	 */
	public static String getStringFromSP(Context context, String key, String defaultvalue) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		return preferences.getString(key, defaultvalue);
	}
	
	/**
	 * Save String value from SharedPreference for the given key with default value
	 */
	public static void saveStringInSP(Context context, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	 
	/**
	 * Retrieve int value from SharedPreference for the given key with default value
	 */
	public static int getIntFromSP(Context context, String key, int defaultvalue) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		return preferences.getInt(key, defaultvalue);
	}
	
	/**
	 * Save int value from SharedPreference for the given key with default value
	 */
	public static void saveIntInSP(Context context, String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * Delete key from SharedPreference for the given key
	 */
	public static void deleteKeyFromSP(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}
	
	/**
	 * Clear shared preferences
	 */
	public static void clearFromSP(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences(
				Constants.SP_NAME,
				android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}