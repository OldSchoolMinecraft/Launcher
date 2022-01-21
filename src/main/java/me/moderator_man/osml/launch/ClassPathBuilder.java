package me.moderator_man.osml.launch;

public class ClassPathBuilder
{
	private StringBuilder sb;
	
	public ClassPathBuilder()
	{
		sb = new StringBuilder();
	}
	
	public void addToClasspath(String path)
	{
		sb.append(path + ";");
	}
	
	public String getFinal()
	{
		String prestr = sb.toString();
		return prestr.substring(0, prestr.length() - 1);
	}
}
