package com.rnv.media.views;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rnv.mediaapp.R;
/** Typeface TextView sets our custom textview fonts & styles when inflating from xml file at runtime.
 *
 *  
 */
public class TypefacedTextView extends TextView {
	String ttfName;
	int mTextStyle;
	// Default constructor when inflating from XML file
	public TypefacedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypefacedText);
		int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.TypefacedText_ttf_tv_name:
				ttfName = a.getString(attr);
				break;
			case R.styleable.TypefacedText_ttf_tv_textStyle:
				mTextStyle = a.getInteger(attr, Typeface.NORMAL);                	
				break;
			}}
		if(ttfName != null)
			init();
	} 
	/**
	 * Set Typeface for textview based on input from xml file 
	 */
	private void init() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + ttfName);
		setTypeface(font, mTextStyle);
	}}
