package com.rnv.media.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.rnv.mediaapp.R;
 
/** Typeface ButtonView sets our custom Button text fonts & styles when inflating from xml file at runtime.
 * 
 *   
 *
 */
public class TypefacedButton extends Button 
{
	String ttfName;
	int mTextStyle;

	// Default constructor when inflating from XML file
	public TypefacedButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypefacedButton);

		int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.TypefacedButton_ttf_btn_name:
				ttfName = a.getString(attr);
				break;
			case R.styleable.TypefacedButton_ttf_btn_textStyle:
				mTextStyle = a.getInteger(attr, Typeface.NORMAL);                	
				break;
			}
		}

		if(ttfName != null)
			init();
	}

	/**
	 * Set Typeface for text on Button view based on input from xml file 
	 */
	private void init() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + ttfName);
		setTypeface(font, mTextStyle);
	}

}