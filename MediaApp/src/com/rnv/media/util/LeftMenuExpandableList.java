package com.rnv.media.util;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rnv.mediaapp.R;

/**Inorder to select any option for sliding menu Or Left navigation menu drawer 
 * user can select using this list adapter.
 * 
 * 
 */
public class LeftMenuExpandableList extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> mGroupsData; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> mChildsData;
	LayoutInflater infalInflater = null;

	public LeftMenuExpandableList(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this.mGroupsData = listDataHeader;
		this.mChildsData = listChildData;
		infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.mChildsData.get(this.mGroupsData.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//Inflate Child View
		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			convertView = infalInflater.inflate(R.layout.left_menu_row_child_list, null);
		}
		TextView txtListChild = (TextView) convertView.findViewById(R.id.left_menu_titlte);
		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.mChildsData.get(this.mGroupsData.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.mGroupsData.get(groupPosition);
	}

	@Override
	public int getGroupCount() { 
		return this.mGroupsData.size();
	} 

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		//Inflate Header Group View
		if(convertView == null){
			convertView = infalInflater.inflate(R.layout.left_menu_row_group_header, null);
		}
		prepareGroupView(convertView, groupPosition, headerTitle);
		
		return convertView;
	}



	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private void prepareGroupView(View convertView, int groupPosition, String headerTitle) {
		TextView listHeader = (TextView) convertView.findViewById(R.id.group_title);
		listHeader.setTypeface(null, Typeface.BOLD);
		listHeader.setText(headerTitle);
		listHeader.setTextColor(Color.WHITE);
		listHeader.setTextSize(23);
	}
} 
