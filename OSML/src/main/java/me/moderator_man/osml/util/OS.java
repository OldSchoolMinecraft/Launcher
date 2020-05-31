package me.moderator_man.osml.util;

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
		if (flag.contains("osx") || flag.contains("mac"))
			return OS.Mac;
		if (flag.contains("nix") || flag.contains("nux")) // os.name on Linux is basically "Linux", but
			return OS.Linux;                          // but I'll the unix part stay just to be sure
		return OS.Unsupported;
	}
}
