package me.moderator_man.osml;

public enum OS
{
	Windows,
	Mac,
	Linux,
	Unsupported;
	
	public static OS getOS()
	{
		String flag = System.getProperty("os.name").toLowerCase();
		
		if (flag.contains("win"))
			return OS.Windows;
		if (flag.contains("osx"))
			return OS.Mac;
		if (flag.contains("unix"))
			return OS.Linux;
		return OS.Unsupported;
	}
}
