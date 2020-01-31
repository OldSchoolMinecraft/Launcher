package me.moderator_man.osml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Util
{
	public static String getNativesPath()
	{
		switch (OS.getOS())
		{
			default:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/";
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/natives/";
			case Mac:
				return String.format("~/Library/Application Support/osm/bin/natives/");
			case Linux:
				return "~/.osm/bin/natives/";
		}
	}
	
	public static String getInstallDirectory()
	{
		switch (OS.getOS())
		{
			default:
				System.out.println("Unknown operating system.");
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/";
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/";
			case Mac:
				return String.format("~/Library/Application Support/osm/");
			case Linux:
				return "~/.osm/";
			case Unsupported:
				System.out.println("Unsupported operating system.");
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/";
		}
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
