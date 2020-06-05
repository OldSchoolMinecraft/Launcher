package me.moderator_man.osml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.QueryAPI;
import me.moderator_man.osml.util.StaticData;
import me.moderator_man.osml.util.Util;

public class DownloadSystem extends Thread
{
	private boolean complete;
	private boolean hasName;
	private String username;
	
	public DownloadSystem()
	{
		complete = false;
	}
	
	public DownloadSystem(String username)
	{
		hasName = true;
		this.username = username;
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
	
	public void downloadNatives()
	{
		switch (OS.getOS())
		{
			default:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/windows/", StaticData.natives_windows, Util.getNativesPath());
				break;
			case Windows:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/windows/", StaticData.natives_windows, Util.getNativesPath());
				break;
			case Mac:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/mac/", StaticData.natives_mac, Util.getNativesPath());
				break;
			case Linux:
				downloadList("https://www.oldschoolminecraft.com/launcher/natives/linux/", StaticData.natives_linux, Util.getNativesPath());
				break;
		}
	}
	
	public void downloadLibraries()
	{
		downloadList("https://www.oldschoolminecraft.com/launcher/libraries/", StaticData.libraries, Util.getBinPath());
	}
	
	public void downloadClient()
	{
		try
		{
			File file = new File(Util.getBinPath() + "minecraft.jar");
			
			if (Main.config.disableUpdate)
				return;
			
			if (!file.exists())
			{
				if (hasName)
					downloadFile("https://www.oldschoolminecraft.com/launcher/download.php?username=" + username, Util.getBinPath() + "minecraft.jar");
				else
					downloadFile("https://www.oldschoolminecraft.com/launcher/download.php", Util.getBinPath() + "minecraft.jar");
				//downloadList("https://www.oldschoolminecraft.com/launcher/", new String[] { "minecraft.jar" }, Util.getBinPath());
			} else {
				String latestChecksum = "";
				
				if (hasName)
					latestChecksum = QueryAPI.get("https://www.oldschoolminecraft.com/launcher/versioncheck.php?username=" + username);
				else
					latestChecksum = QueryAPI.get("https://www.oldschoolminecraft.com/launcher/versioncheck.php");
				//String latestChecksum = QueryAPI.get("https://www.oldschoolminecraft.com/launcher/versioncheck.php");
				String currentChecksum = Util.getMD5Checksum(Util.getBinPath() + "minecraft.jar");
				
				if (!currentChecksum.equals(latestChecksum))
				{
					file.delete();
					if (hasName)
						downloadFile("https://www.oldschoolminecraft.com/launcher/download.php?username=" + username, Util.getBinPath() + "minecraft.jar");
					else
						downloadFile("https://www.oldschoolminecraft.com/launcher/download.php", Util.getBinPath() + "minecraft.jar");
				} else {
					Logger.log(String.format("No client update available (current=%s, latest=%s)", currentChecksum, latestChecksum));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void downloadList(String base, String[] list, String target_directory)
	{
		for (String item : list)
		{
			item = item.trim();
			
			File file = new File(target_directory + item);
			if (file.exists())
			{
				Logger.log(String.format("Skipping '%s': already exists", item));
				continue;
			}
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
	            
	            Logger.log(String.format("Downloaded file '%s' to '%s'", url, path));
	        } else {
	        	Logger.log(String.format("Download failed ('%s'): HTTP code was '%s'", url, responseCode));
	        }
	        httpConn.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
