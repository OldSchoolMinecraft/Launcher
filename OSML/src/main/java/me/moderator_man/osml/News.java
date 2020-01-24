package me.moderator_man.osml;

public class News
{
	private String title;
	private String body;
	
	public News(String title, String body)
	{
		this.title = title;
		this.body = body;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getBody()
	{
		return body;
	}
}
