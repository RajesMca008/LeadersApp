package com.rnv.media.bean;

import java.io.Serializable;

public class CommentsBean implements Serializable{

	private String id;
	private String username;
	private String comment;
	
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
	
	
	
	
	
	
	
	
	
}
