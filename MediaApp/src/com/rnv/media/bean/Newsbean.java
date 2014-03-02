package com.rnv.media.bean;

import java.io.Serializable;

public class Newsbean implements Serializable{

	private String title;
	private String id;
	private String description;
	private String location;
	private String url;

	public  Newsbean(){
	
	}
	public String getTitle() { 
		return title;
	}
	public String getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public String getLocation() {
		return location; 
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public void setDescription(String description) {
		this.description=description;
	}
	public void setLocation(String location) {
		this.location=location;
	}
	public void setId(String id){
		this.id=id;
	}
	public void setUrls(String url) {
		this.url=url;
	}
	public String getUrls()
	{
		return url;
	}
}
