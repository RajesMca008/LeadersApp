package com.rnv.media.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rnv.media.bean.GalleryBean;
import com.rnv.media.video.VideoActivity;
import com.rnv.mediaapp.R;
public class YoutubeADapter extends BaseAdapter{
	int mGalleryItemBackground;
	/*ArrayList<String> thumbnailImage;
	ArrayList<String> duration;
	ArrayList<String> description;*/
	ArrayList<GalleryBean> gallerylist;
	public VideosLoader downloader;
	public YoutubeADapter(Context c) {
		mContext = c; 
	} 
	
	public YoutubeADapter(VideoActivity mContext2,ArrayList<GalleryBean> galbean) {
		
		mContext = mContext2;
		gallerylist=galbean; 
		
	}
	public int getCount()  
	{
		return gallerylist.size();
	}
	public Object getItem(int position) {
		return position;
	}
	public long getItemId(int position) {
		return position;
	}
	public static class ViewHolder
	{
		//public ImageView i;
		public TextView des,title,url;
	}
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v=convertView;
		ViewHolder holder; 
		try
		{
			if (convertView == null) 
			{
				LayoutInflater li=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v=li.inflate(R.layout.fragment_video_grid,null);
			} 
			holder=new ViewHolder();
			//holder.des = (TextView)v.findViewById(R.id.video_description);
			//holder.i=(ImageView)v.findViewById(R.id.video_grid_img_id);
			holder.title = (TextView)v.findViewById(R.id.video_title);
			holder.url = (TextView)v.findViewById(R.id.video_url);

			v.setTag(holder);			
			holder=(ViewHolder)v.getTag();
			
			//holder.des.setText(gallerylist.get(position).getDescription().toString());
			holder.title.setText(gallerylist.get(position).getTitle().toString());
			holder.url.setText(gallerylist.get(position).getVideoUrl());
			//holder.i.setScaleType(ImageView.ScaleType.FIT_XY);
			//holder.i.setTag(gallerylist.get(position).getVideoUrl());
			//holder.i.setScaleType(ImageView.ScaleType.FIT_START);
			//holder.i.setPadding(8, 8, 8, 8);
			//holder.i.setBackgroundColor(Color.TRANSPARENT);
			//downloader = new VideosLoader(holder.i);
			//downloader.execute(gallerylist.get(position).getVideoUrl());
		}
		catch(OutOfMemoryError e) 
		{
			System.gc();

		}
		return v;
	} 
	private Context mContext;
}
