package com.rnv.media.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.rnv.mediaapp.R;
 
/** 
 * Generic Dialog Fragment to display the dialogs in the application. The
 * calling Activity must implement {@link OnDialogFragmentClickListener}
 * interface.
 * 
 */
@SuppressLint("ValidFragment")
public class MediaDialogFragment extends DialogFragment implements android.content.DialogInterface.OnClickListener {

	private int mState;
	private String mTitle;
	private String mMessage;
	private String mNegativeText;
	private String mPositiveText;
	private boolean mIsSwitcher;
	private OnDialogFragmentClickListener mContext;

	/**
	 * Constructor with title and message for the dialog. Calls
	 * {@link #base(String, String, String, String, boolean)}
	 * with bool param as false.
	 * @param title Title resID of the dialog.
	 * @param message Message resID of the dialog.
	 * @param negativeButtonText The text resID of Negative button. pass null to skip the negative button.
	 * @param positiveButtonText The text resID of positive button. pass null to skip the positive button.
	 * @param state The unique id for the Dialog.
	 */
	public MediaDialogFragment(String title, String message, String negativeButtonText, String positiveButtonText,
			int state) {
		this(title, message, negativeButtonText, positiveButtonText, state, false);
	}

	/**
	 * Constructor with title and message for the dialog.
	 * 
	 * @param title Title resID of the dialog.
	 * @param message Message resID of the dialog.
	 * @param negativeButtonText The text resID of Negative button. pass null to skip the
	 *            negative button.
	 * @param positiveButtonText The text resID of positive button. pass null to skip the
	 *            positive button.
	 * @param isSwitcher boolean to change the UI of the Dialog Fragment. If true will
	 *            raise Switcher dialog.
	 * @param state The unique id for the Dialog.
	 */
	public MediaDialogFragment(String title, String message, String negativeButtonText, String positiveButtonText,
			int state, boolean isSwitcher) {
		mTitle = title;
		mState = state;
		mMessage = message;
		mIsSwitcher = isSwitcher;
		mNegativeText = negativeButtonText;
		mPositiveText = positiveButtonText;
		setStyle(android.R.style.Theme_DeviceDefault_Light_Dialog, getTheme());
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		mContext = (OnDialogFragmentClickListener) getActivity();
		setCancelable(false);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return createAlertDialog();

	} // end onCreateDialog()

	/**
	 * Creates the alert dialog for the Fragment
	 * @return the Alert dialog.
	 */
	private AlertDialog createAlertDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (mTitle != null)
			builder.setTitle(mTitle);
		builder.setInverseBackgroundForced(true); // To inverse the background
													// colo

		// Null check before creating Positive Button
		if (mPositiveText != null) {
			builder.setPositiveButton(mPositiveText, this);
		}

		// Null check before creating Negative Button
		if (mNegativeText != null) {
			builder.setNegativeButton(mNegativeText, this);
		}
			return createGeneralAlertDialog(builder);
	}

	/**
	 * Creates a general Alert Dialog with Title, message, positive button and
	 * negative button.
	 * @return The created alert dialog.
	 */
	private AlertDialog createGeneralAlertDialog(AlertDialog.Builder builder) {

		TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_text_view, null);
		textView.setText(mMessage);
		builder.setView(textView);
		return builder.create();
	} // end createGeneralAlertDialog()


	@Override
	public void onClick(DialogInterface dialog, int which) {
		Trace.d("Positive button ciclked", "true");
		switch (which) {
		case Dialog.BUTTON_NEGATIVE:
			mContext.onNegativeButtonClicked(mState);
			break;
		case Dialog.BUTTON_POSITIVE:
			mContext.onPositiveButtonClicked(mState);
			break;
		default:
			break;
		}
	}
}