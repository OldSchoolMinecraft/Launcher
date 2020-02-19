package me.moderator_man.osml.util;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util
{
	/*
	 * Thank you JuliusVan for the most retarded term known to man (netpage).
	 */
	public static void openNetpage(String url)
	{
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
		{
		    try
			{
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String getNativesPath()
	{
		switch (OS.getOS())
		{
			default:
				System.out.println("Unknown operating system (assuming Windows).");
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/");
			case Windows:
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/");
			case Mac:
				return String.format("~/Library/Application Support/osm/bin/natives/");
			case Linux:
				return getLinuxHomeDir() + "/.osm/bin/natives/";
			case Unsupported:
				System.out.println("Unsupported operating system (assuming Linux).");
				return getLinuxHomeDir() + "/.osm/bin/natives/";
		}
	}
	
	private static String backslashes(String input)
	{
		return input.replaceAll("/", "\\\\");
	}

	public static String getLinuxHomeDir()
	{
		// $HOME environment variable should exist, but handle the situation when
		// it doesn't exist and/or the user executes program as root.
		String linux_home = System.getenv("HOME");
		if (linux_home == null)
		{
			String linux_user = System.getenv("USER");
			if (linux_user == "root")
				return "/root";
			else
				return "/home/" + linux_user;
		}
		else
			return linux_home;

	}
	
	public static String getBinPath()
	{
		switch (OS.getOS())
		{
			default:
				System.out.println("Unknown operating system (assuming Windows).");
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/");
			case Windows:
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/");
			case Mac:
				return String.format("~/Library/Application Support/osm/bin/");
			case Linux:
				return getLinuxHomeDir() + "/.osm/bin/";
			case Unsupported:
				System.out.println("Unsupported operating system (assuming Linux).");
				return getLinuxHomeDir() + "/.osm/bin/";
		}
	}
	
	public static String getInstallDirectory()
	{
		switch (OS.getOS())
		{
			default:
				System.out.println("Unknown operating system (assuming Windows).");
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/");
			case Windows:
				return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.osm/");
			case Mac:
				return String.format("~/Library/Application Support/osm/");
			case Linux:
				return getLinuxHomeDir() + "/.osm/";
			case Unsupported:
				System.out.println("Unsupported operating system (assuming Linux).");
				return getLinuxHomeDir() + "/.osm/";
		}
	}
	
	public static String getCurrentLogFile()
	{
		switch (OS.getOS())
		{
			default:
				System.out.println("Unknown operating system (assuming Windows).");
				return backslashes(getInstallDirectory() + "/logs/" + getCurrentTimestamp()) + ".log";
			case Windows:
				return backslashes(getInstallDirectory() + "/logs/" + getCurrentTimestamp()) + ".log";
			case Mac:
				return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
			case Linux:
				return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
			case Unsupported:
				System.out.println("Unsupported operating system (assuming Linux).");
				return getInstallDirectory() + "/logs/" + getCurrentTimestamp() + ".log";
		}
	}
	
	public static String getCurrentTimestamp()
	{
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH-mm-ss z");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	public static byte[] createChecksum(String filename) throws Exception
	{
		InputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do
		{
			numRead = fis.read(buffer);
			if (numRead > 0)
				complete.update(buffer, 0, numRead);
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	public static String getMD5Checksum(String filename) throws Exception
	{
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i = 0; i < b.length; i++)
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		return result;
	}
}
