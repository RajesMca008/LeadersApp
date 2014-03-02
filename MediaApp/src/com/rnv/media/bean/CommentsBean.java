package com.rnv.media.bean;

import java.io.Serializable;

public class CommentsBean implements Serializable{

	private String id;
	private String username;
	private String comment;
	private String date;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getUserName()
	{
		return username;
	}
	public void setUserName(String username)
	{
		this.username=username;
	}
	public String getComment()
	{
		return comment;
	}
	public void setcomment(String comment)
	{
		this.comment=comment;
	}
	public void setDate(String date) {
		this.date=date;
	}
	public String getDate()
	{
		return date;
	}
	
	
	
	
	
	
	
}
