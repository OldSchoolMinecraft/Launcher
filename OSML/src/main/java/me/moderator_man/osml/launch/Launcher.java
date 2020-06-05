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
    public void legacyLaunch(String username, String sessionId)
    {
        StringBuilder libsb = new StringBuilder();
        for (int i = 0; i < StaticData.libraries.length; i++)
            libsb.append(os_library(StaticData.libraries[i]) + File.pathSeparator);
        libsb.append(os_library("minecraft.jar"));
        String libs = libsb.toString();
        
        String java_path = "javaw";
        
        ArrayList<String> params = new ArrayList<String>();
        
        params.add(java_path);
        params.add("-Xmx" + Main.config.ramMb + "M");
        params.add("-Dsun.java2d.noddraw=true");
        params.add("-Dsun.java2d.d3d=false");
        params.add("-Dsun.java2d.opengl=false");
        params.add("-Dsun.java2d.pmoffscreen=false");
        params.add("-Djava.library.path=" + Util.getNativesPath());
        params.add("-classpath");
        params.add(libs);
        params.add("net.minecraft.client.Minecraft");
        params.add(username);
        params.add(sessionId);
        params.add("--enable-auth");
        
        launch(username, params, true);
    }
    
    public void launch(String username, ArrayList<String> parameters, boolean legacy)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    if (!legacy)
                        MainFrame.mainFrame.setDownloadingMessage();
                    DownloadSystem dlsys = new DownloadSystem(username);
                    dlsys.start();
                    while (!dlsys.isComplete())
                        Thread.sleep(500);
                    if (!legacy)
                        MainFrame.mainFrame.setLaunchingMessage();
                    Thread.sleep(500);
                    if (!legacy)
                        MainFrame.mainFrame.hide();
                    
                    ProcessBuilder pb = new ProcessBuilder(parameters);
                    
                    pb.inheritIO();
                    new GameWatchdogThread(pb.start()).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
	public void launch(String username, String sessionId)
	{
	    StringBuilder libsb = new StringBuilder();
        for (int i = 0; i < StaticData.libraries.length; i++)
            libsb.append(os_library(StaticData.libraries[i]) + File.pathSeparator);
        libsb.append(os_library("minecraft.jar"));
        String libs = libsb.toString();
        
        String java_path = "javaw";
	    
	    ArrayList<String> params = new ArrayList<String>();
        
        params.add(java_path);
        params.add("-Xmx" + Main.config.ramMb + "M");
        params.add("-Dsun.java2d.noddraw=true");
        params.add("-Dsun.java2d.d3d=false");
        params.add("-Dsun.java2d.opengl=false");
        params.add("-Dsun.java2d.pmoffscreen=false");
        params.add("-Djava.library.path=" + Util.getNativesPath());
        params.add("-classpath");
        params.add(libs);
        params.add("net.minecraft.client.Minecraft");
        params.add(username);
        params.add(sessionId);
        params.add("--enable-auth");
        
        launch(username, params, false);
	}
	
	private String os_library(String name)
	{
		return Util.getBinPath() + name;
	}
}
