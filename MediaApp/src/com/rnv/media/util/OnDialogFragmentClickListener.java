/**
 * 
 */
package com.rnv.media.util;


/**
 * Interface to register callback's from {@link MediaDialogFragment}
 *
 */
public interface OnDialogFragmentClickListener {
 
	/**
	 * Call back for positive button click.
	 * @param id  The ID of the Dialog 
	 */
	void onPositiveButtonClicked(int id);

	/**
	 * Call back for Negative button click.
	 * @param id  The ID of the Dialog 
	 */
	void onNegativeButtonClicked(int id);
}
