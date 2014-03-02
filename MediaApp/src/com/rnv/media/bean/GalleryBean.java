package com.rnv.media.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GalleryBean implements Serializable{

	private String title;
	private String description;
	private String url1;
	private String url2;
	private String id;
	private String video_url;
	private String speech_url;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getSpeechUrl()
	{
		return speech_url;
	}
	public void setSpeechUrl(String speech_url)
	{
		this.speech_url=speech_url;
	}
	public String getVideoUrl()
	{
		return video_url;
	}
	public void setVideoUrl(String video_url)
	{
		this.video_url=video_url;
	}
	
	public String getTitle()
	{
		return title;
	}
	public String getDescription()
	{
		return description;
	}
	public String getUrl1()
	{
		return url1;
	}
	public String getUrl2()
	{
		return url2;
	}

	public void settitle(String title)
	{
		this.title=title;
	}
	public void setdescription(String description)
	{
		this.description=description;
	}
	public void setUrl1(String url1)
	{
		this.url1=url1;
	}
	public void setUrl2(String url2)
	{
		this.url2=url2;
	}


}
