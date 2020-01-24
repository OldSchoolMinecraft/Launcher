package me.moderator_man.osml;

public enum OS
{
	Windows,
	Mac,
	Linux,
	Unsupported;
	
	public static OS getOS()
	{
		String flag = System.getProperty("os.name");
		
		if (flag.contains("win"))
			return OS.Windows;
		if (flag.contains("mac"))
			return OS.Mac;
		if (flag.contains("unix"))
			return OS.Linux;
		return OS.Unsupported;
	}
}
