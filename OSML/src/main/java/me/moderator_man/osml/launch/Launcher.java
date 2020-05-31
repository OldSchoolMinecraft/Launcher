package me.moderator_man.osml.launch;

import java.io.File;
import java.util.ArrayList;

import me.moderator_man.osml.DownloadSystem;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.ui.MainFrame;
import me.moderator_man.osml.util.StaticData;
import me.moderator_man.osml.util.Util;

public class Launcher
{
	public void launch(String username, String sessionId)
	{
		new Thread(new Runnable()
		{
			public void run()
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
					// ensure the libraries are loaded in order
					for (int i = 0; i < StaticData.libraries.length; i++)
						libsb.append(os_library(StaticData.libraries[i]) + File.pathSeparator);
					/*for (String library : StaticData.libraries)
						libsb.append(os_library(library) + File.pathSeparator);*/
					String libs = libsb.toString();
					
					String java_path = "javaw";
					String main_class = "net.minecraft.client.Minecraft";
					String tweak_class = "net.minecraft.launchwrapper.VanillaTweaker";
					String wrap_args = String.format("");
					String launcher_brand = "osml";
					int launcher_version = Main.VERSION;
					String client_jar = "not_found";
					for (String lib : StaticData.libraries)
						if (lib.contains("minecraft.jar"))
							client_jar = os_library(lib);
					
					ArrayList<String> params = new ArrayList<String>();
					
					params.add(java_path);
					params.add("-Xmx" + Main.config.ramMb + "M");
					params.add("-Dsun.java2d.noddraw=true");
	                params.add("-Dsun.java2d.d3d=false");
	                params.add("-Dsun.java2d.opengl=false");
	                params.add("-Dsun.java2d.pmoffscreen=false");
	                params.add("-Djava.library.path=" + Util.getNativesPath());
	                params.add("-cp");
	                params.add(libs);
	                params.add(main_class);
	                params.add(username);
	                params.add(sessionId);
	                params.add("--enable-auth");
	                
	                ProcessBuilder pb = new ProcessBuilder(params);
	                
					/*ProcessBuilder pb = new ProcessBuilder(java_path,
														   "-Xmx" + Main.config.ramMb + "M",
														   "-XX:+UseConcMarkSweepGC",
														   "-XX:+CMSIncrementalMode",
														   "-XX:-UseAdaptiveSizePolicy",
														   "-Xmn128M",
														   "-Djava.library.path=" + Util.getNativesPath(),
														   "-Dminecraft.launcher.brand=" + launcher_brand,
														   "-Dminecraft.launcher.version=" + launcher_version,
														   "-Dminecraft.client.jar=" + client_jar,
														   "-cp",
														   libs,
														   main_class,
														   username,
														   sessionId,
														   "--enable-auth");*/
					pb.inheritIO();
					new GameWatchdogThread(pb.start()).start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
	
	private String os_library(String name)
	{
		return Util.getBinPath() + name;
	}
}
