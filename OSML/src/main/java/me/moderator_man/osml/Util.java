package me.moderator_man.osml;

public class Util
{
	public static String getInstallDirectory()
	{
		switch (OS.getOS())
		{
			default:
				return "";
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/";
			case Mac:
				return String.format("~/Library/Application Support/osm/");
			case Linux:
				return "~/.osm/";
		}
	}
}
