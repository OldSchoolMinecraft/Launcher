package me.moderator_man.osml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadSystem extends Thread
{
	private String[] natives_windows;
	private String[] natives_mac;
	private String[] natives_linux;
	private String[] libraries;
	private boolean complete;
	
	public DownloadSystem()
	{
		natives_windows = new String[]
		{
			"jinput-dx8.dll",
			"jinput-dx8_64.dll",
			"jinput-raw.dll",
			"jinput-raw_64.dll",
			"jinput-wintab.dll",
			"lwjgl.dll",
			"lwjgl64.dll",
			"OpenAL32.dll",
			"OpenAL64.dll"
		};
		
		natives_mac = new String[]
		{
			"jinput.jnilib",
			"lwjgl.jnilib",
			"openal.dylib"
		};
		
		natives_linux = new String[]
		{
			"jinput.so",
			"jinput64.so",
			"lwjgl.so",
			"lwjgl64.so",
			"openal.so",
			"openal64.so"
		};
		
		libraries = new String[]
		{
			"lwjgl.jar",
			"jinput.jar",
			"lwjgl_util.jar",
			"json.jar"
		};
		
		complete = false;
	}
	
	public void run()
	{
		downloadNatives();
		downloadLibraries();
		downloadClient();
		complete = true;
	}
	
	public boolean isComplete()
	{
		return complete;
	}
	
	private String getNativesPath()
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
	
	private String getBinPath()
	{
		switch (OS.getOS())
		{
			default:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/";
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/bin/";
			case Mac:
				return String.format("~/Library/Application Support/osm/bin/");
			case Linux:
				return "~/.osm/bin/natives/";
		}
	}
	
	public void downloadNatives()
	{
		switch (OS.getOS())
		{
			default:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/windows/", natives_windows, getNativesPath());
				break;
			case Windows:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/windows/", natives_windows, getNativesPath());
				break;
			case Mac:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/mac/", natives_mac, getNativesPath());
				break;
			case Linux:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/linux/", natives_linux, getNativesPath());
				break;
		}
	}
	
	public void downloadLibraries()
	{
		downloadList("https://www.oldschoolminecraft.com/launcher/libraries/", libraries, getBinPath());
	}
	
	public void downloadClient()
	{
		File file = new File(getBinPath() + "minecraft.jar");
		
		if (Main.config.forceUpdate)
		{
			if (file.exists())
				file.delete();
			downloadList("", new String[] { "minecraft.jar" }, getBinPath());
		} else {
			try
			{
				
				if (file.exists())
				{
					String current_hash = Util.getMD5Checksum(getBinPath() + "minecraft.jar");
					String latest_hash = QueryAPI.get("https://www.oldschoolminecraft.com/versioncheck.php");
					if (latest_hash != current_hash)
					{
						if (file.exists())
							file.delete();
						downloadList("https://www.oldschoolminecraft.com/launcher/", new String[] { "minecraft.jar" }, getBinPath());
					}
				} else {
					downloadList("https://www.oldschoolminecraft.com/launcher/", new String[] { "minecraft.jar" }, getBinPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void downloadList(String base, String[] list, String target_directory)
	{
		for (String item : list)
		{
			downloadFile(base + item, target_directory + item);
		}
	}
	
	private void downloadFile(String url, String path)
	{
		downloadFile(url, path, false);
	}
	
	private void downloadFile(String url, String path, boolean deleteIfExists)
	{
		try
		{
			File targetFile = new File(path);
			if (targetFile.exists() && deleteIfExists)
				targetFile.delete();
			
			URL fileURL = new URL(url);
	        HttpURLConnection httpConn = (HttpURLConnection) fileURL.openConnection();
	        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	        int responseCode = httpConn.getResponseCode();
	 
	        if (responseCode == HttpURLConnection.HTTP_OK)
	        {
	            InputStream inputStream = httpConn.getInputStream();
	            String saveFilePath = path;
	            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
	 
	            int bytesRead = -1;
	            byte[] buffer = new byte[4096];
	            while ((bytesRead = inputStream.read(buffer)) != -1)
	                outputStream.write(buffer, 0, bytesRead);
	 
	            outputStream.close();
	            inputStream.close();
	            
	            System.out.println("File downloaded");
	        } else {
	            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
	        }
	        httpConn.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
