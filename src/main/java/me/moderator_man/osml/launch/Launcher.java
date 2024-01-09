package me.moderator_man.osml.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.moderator_man.osml.DownloadSystem;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.ui.MainFrame;
import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.StaticData;
import me.moderator_man.osml.util.Util;

public class Launcher
{
    public void legacyLaunch(String username, String sessionId, String uuid, boolean mojang)
    {
        StringBuilder libsb = new StringBuilder();
        for (int i = 0; i < StaticData.libraries.length; i++)
            libsb.append(os_library(StaticData.libraries[i]) + File.pathSeparator);
        libsb.append(os_library("minecraft.jar"));
        String libs = libsb.toString();
        
        boolean windows = OS.getOS() == OS.Windows;
        String java_path = windows ? "javaw" : "java";
        
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
        if (uuid != null)
            params.add(uuid);
        params.add("--enable-auth");
        if (mojang)
            params.add("--mojang-auth");
        
        launch(username, params, true);
    }
    
    public void launch(String username, ArrayList<String> parameters, boolean legacy)
    {
        new Thread(() ->
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
                Process proc = pb.start();
                // proc.waitFor() won't return if the input stream isn't being read
                while ((new BufferedReader(new InputStreamReader(proc.getInputStream())).readLine()) != null) {}
                int code = proc.waitFor();
                System.exit(code);
                //new GameWatchdogThread(pb.start()).start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
	public void launch(String username, String sessionId, String uuid, boolean mojang)
	{
	    StringBuilder libsb = new StringBuilder();
        for (int i = 0; i < StaticData.libraries.length; i++)
            libsb.append(os_library(StaticData.libraries[i]) + File.pathSeparator);
        libsb.append(os_library("minecraft.jar"));
        String libs = libsb.toString();
        
        boolean windows = OS.getOS() == OS.Windows;
        String java_path = windows ? "javaw" : "java";
	    
        Logger.log("Launcher/Path: " + java_path);
        Logger.log("Launcher/Natives: " + new File(Util.getNativesPath()).getAbsolutePath());
        
        System.setProperty("java.library.path", new File(Util.getNativesPath()).getAbsolutePath());
        System.setProperty("org.lwjgl.librarypath", new File(Util.getNativesPath()).getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", new File(Util.getNativesPath()).getAbsolutePath());
        
	    ArrayList<String> params = new ArrayList<String>();
        
        params.add(java_path);
        params.add("-Xmx" + Main.config.ramMb + "M");
        params.add("-Dsun.java2d.noddraw=true");
        params.add("-Dsun.java2d.d3d=false");
        params.add("-Dsun.java2d.opengl=false");
        params.add("-Dsun.java2d.pmoffscreen=false");
        params.add("-Djava.library.path=" + new File(Util.getNativesPath()).getAbsolutePath());
        params.add("-Dorg.lwjgl.librarypath=" + new File(Util.getNativesPath()).getAbsolutePath());
        params.add("-Dnet.java.games.input.librarypath" + new File(Util.getNativesPath()).getAbsolutePath());
        params.add("-classpath");
        params.add(libs);
        params.add("net.minecraft.client.Minecraft");
        params.add(username);
        params.add(sessionId);
        if (Main.config.disableBitDepthFix) // add ability to disable bitdepthfix for integrated graphics compatibility
            params.add("--noDepthFix");
        /*if (uuid != null)
            params.add(uuid);
        params.add("--enable-auth");*/

        launch(username, params, false);
	}
	
	private String os_library(String name)
	{
		return Util.getBinPath() + name;
	}
}
