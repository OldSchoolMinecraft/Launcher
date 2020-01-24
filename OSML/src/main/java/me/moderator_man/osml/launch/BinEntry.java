package me.moderator_man.osml.launch;

import me.moderator_man.osml.Util;

public class BinEntry
{
	private String base_directory;
	private String path;
	
	public BinEntry(String base_directory, String path)
	{
		this.base_directory = "bin/" + base_directory;
		this.path = path;
	}
	
	public String getPath()
	{
		String install_directory = Util.getInstallDirectory();
		return install_directory + base_directory + path;
	}
}
