package com.rnv.media.dashboard;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.SearchView;

import com.rnv.media.util.Constants;
import com.rnv.mediaapp.BaseNavDrawerActivity;
import com.rnv.mediaapp.R;

/**DashBoard Screen is the home Page for this application.
 * We can construct all left navigation list data and initialize required 
 * UI components and listeners for this screen.
 *     
 */     
@SuppressLint("NewApi")
public class DashBoardActivity  extends BaseNavDrawerActivity{
	DashBoardActivity mContext;
	DashBoardFragment mFragment;
	private SearchView mSearchView;

	private static ArrayList<String> mResults;
	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//Enable Action bar title and Navigation icons
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setCustomView(R.layout.actionbar_title);
		getSupportActionBar().setHomeButtonEnabled(true);
		//getSupportActionBar().setIcon(R.drawable.ic_abcrop_logo_inner);
		getSupportActionBar().setTitle(R.string.news_text);
		mFragment = new DashBoardFragment();
		FragmentManager fragManager = getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.add(R.id.content_frame, mFragment);
		fragTransaction.commit();
		mContext=this; 
	} 
	@Override  
	public void onBackPressed() {
		//super.onBackPressed();
		showDialogFragment(Constants.EXIT);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*getMenuInflater().inflate(R.menu.main,menu);
		MenuItem item = menu.findItem(R.id.action_share);
		View actionView = item.getActionView().findViewById(R.id.imgbtnShare);*/
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	protected void onResume() {
		super.onResume();
	} 
	private boolean isAlwaysExpanded() {
		return false;
	}}
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
		default:
			break; 
		}
		return super.onOptionsItemSelected(item); 
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		*//**
		 * For search action.....
		 * 
		 * *//*
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		int id1 = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) mSearchView.findViewById(id1);
		textView.setTextColor(Color.BLACK);

		mSearchView.setSuggestionsAdapter(new SearchSuggestionsAdapter(this));
		mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
		{
			@Override
			public boolean onSuggestionClick(int position)
			{ 
				int c = 0; 
				for(int i=0;i<searchitemarray.length;i++)
				{
					if(mResults.get(position).equals(searchitemarray[i])) 
					{
						System.out.println(""+searchitemarray[position].matches(searchitemarray[i]) +""+i+""+searchitemarray[position]+searchitemarray[i]);
						c=i;
					}  
				} 				
				SharedPreferenceUtil.saveIntInSP(mContext, Constants.KEY_FORGRIDITEMCLICK, c+1);
				DashBoardFragment.adflag_dashboard=true;
				SignInFragment.adflag_Signin=false;

				Intent in=new Intent(mContext, AdActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//in.putExtras(b);
				startActivity(in);  

				mSearchView.clearFocus(); 
				return true; 
			} 
			@Override
			public boolean onSuggestionSelect(int position)
			{
				return false;
			}
		});
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				mSearchView.clearFocus();
				return true;
			}
			@Override
			public boolean onQueryTextChange(String newText)
			{
				return false;
			}
		});

		*//**
		 *  
		 * For Share item
		 * *//*
		MenuItem item = menu.findItem(R.id.action_share);
		final View actionView = (View) MenuItemCompat.getActionView(item);
		sadapter.enable(actionView);

		return super.onPrepareOptionsMenu(menu);
	}
	public static class SearchSuggestionsAdapter extends SimpleCursorAdapter
	{
		private static final String[] mFields  = { "_id", "result" };
		private static final String[] mVisible = { "result" };
		private static final int[]    mViewIds = { android.R.id.text1 };
		public SearchSuggestionsAdapter(Context context)
		{
			super(context, android.R.layout.simple_list_item_1, null, mVisible, mViewIds, 0);
		}
		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint)
		{
			return new SuggestionsCursor(constraint);
		}
		private static class SuggestionsCursor extends AbstractCursor
		{
			public SuggestionsCursor(CharSequence constraint)
			{
				mResults = new ArrayList<String>(searchitemarray.length);
				for (String s : searchitemarray) {  
					mResults.add(s);  
				}  
				if(!TextUtils.isEmpty(constraint)){
					String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
					Iterator<String> iter = mResults.iterator();
					while(iter.hasNext()){
						if(!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))
						{
							iter.remove();
						}}}}
			@Override
			public int getCount()
			{
				return mResults.size();
			}
			@Override
			public String[] getColumnNames()
			{
				return mFields;
			}
			@Override
			public long getLong(int column)
			{
				if(column == 0){
					return mPos;
				}
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public String getString(int column)
			{
				if(column == 1){
					return mResults.get(mPos);
				}
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public short getShort(int column)
			{
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public int getInt(int column)
			{
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public float getFloat(int column)
			{
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public double getDouble(int column)
			{
				throw new UnsupportedOperationException("unimplemented");
			}
			@Override
			public boolean isNull(int column)
			{
				return false;
			}}*/
