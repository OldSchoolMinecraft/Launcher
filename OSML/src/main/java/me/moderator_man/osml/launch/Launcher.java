package me.moderator_man.osml.launch;

import me.moderator_man.osml.DownloadSystem;
import me.moderator_man.osml.MainFrame;
import me.moderator_man.osml.OS;
import me.moderator_man.osml.Util;

public class Launcher
{
	public Launcher() {}
	
	public void launch(String[] parameters)
	{
		boolean debug = false;
		
		if (!debug)
		{
			DownloadSystem dlsys = new DownloadSystem();
			dlsys.start();
			
			MainFrame.mainFrame.setDownloadingMessage();
			
			while (!dlsys.isComplete()) { try { Thread.sleep(1000); } catch (Exception ex) {} }
			
			ClassPathBuilder cpb = new ClassPathBuilder();
			
			String[] libs = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "json.jar", "minecraft.jar" };
			
			for (String lib : libs)
				cpb.addToClasspath(getInstallDirectory(OS.getOS()) + "\\bin\\" + lib);
			
			StringBuilder arguments = new StringBuilder();
			arguments.append("net.minecraft.client.Minecraft ");
			for (String arg : parameters)
				arguments.append(arg + " ");
			
			try
			{
				MainFrame.mainFrame.setLaunchingMessage();
				
				String javaPath = System.getProperty("java.home") + "/bin/java";
				String classpath = cpb.getFinal() + " " + arguments.toString().trim();
				System.out.println("Final classpath: " + classpath);
				//ProcessBuilder pb = new ProcessBuilder("\"" + javaPath + "\" -Djava.library.path=" + Util.getNativesPath() + " -cp " + classpath);
				ProcessBuilder pb = new ProcessBuilder(javaPath, "-Djava.library.path=" + Util.getNativesPath(), "-cp", classpath);
				GameThread gameThread = new GameThread(pb.start());
				gameThread.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			MainFrame.mainFrame.setLaunchingMessage();
		}
	}
	
	private String getInstallDirectory(OS operating_system)
	{
		switch (operating_system)
		{
			default:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm".replaceAll("/", "\\\\");
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm".replaceAll("/", "\\\\");
			case Mac:
				return String.format("~/Library/Application Support/osm");
			case Linux:
				return "~/.osm";
		}
	}
}
