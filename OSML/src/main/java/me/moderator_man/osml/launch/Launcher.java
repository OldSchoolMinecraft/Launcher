package me.moderator_man.osml.launch;

import me.moderator_man.osml.DownloadSystem;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.ui.MainFrame;
import me.moderator_man.osml.util.StaticData;
import me.moderator_man.osml.util.Util;

public class Launcher
{
	public void launch(String username, String sessionId)
	{
		try
		{
			MainFrame.mainFrame.setDownloadingMessage();
			DownloadSystem dlsys = new DownloadSystem();
			dlsys.start();
			while (!dlsys.isComplete())
				Thread.sleep(500);
			MainFrame.mainFrame.setLaunchingMessage();
			Thread.sleep(500);
			MainFrame.mainFrame.hide();
			
			StringBuilder libsb = new StringBuilder();
			for (String library : StaticData.libraries)
			{
				libsb.append(os_library(library) + ";");
			}
			libsb.append(os_library("minecraft.jar"));
			String libs = libsb.toString();
			
			ProcessBuilder pb = new ProcessBuilder("java", "-Xmx" + Main.config.ramMb + "M", "-Djava.library.path=" + Util.getNativesPath(), "-cp", libs, "net.minecraft.client.Minecraft", username, sessionId);
			GameLogThread glThread = new GameLogThread(pb.start());
			glThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String os_library(String name)
	{
		return Util.getBinPath() + name;
	}
}
