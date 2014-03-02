package com.rnv.media.bean;

import java.io.Serializable;
 
@SuppressWarnings("serial")
public class QueriesBean implements Serializable{
	private String id;
	private String name;
	private String email;
	private String text;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text=text;
	}
	
	
}
