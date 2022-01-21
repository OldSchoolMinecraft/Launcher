package me.moderator_man.osml;

import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import me.moderator_man.osml.io.FormatReader;
import me.moderator_man.osml.io.FormatWriter;
import me.moderator_man.osml.ui.MainFrame;
import me.moderator_man.osml.ui.legacy.LauncherFrame;
import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.QueryAPI;
import me.moderator_man.osml.util.Util;

public class Main
{
	public static final int VERSION = 17;

	public static Configuration config;
	public static boolean updateAvailable = false;
	public static boolean firstTime = true;
	public static boolean problematicJava = false;
	
	private static String getConfigPath()
	{
		switch (OS.getOS())
		{
			default:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/launcher.cfg".replaceAll("/", "\\\\");
			case Mac:
				return String.format("~/Library/Application Support/osm/launcher.cfg");
			case Linux:
				return Util.linuxHomeDir + "/.osm/launcher.cfg";
		}
	}
	
	public static void saveConfig()
	{
		File cfg = new File(getConfigPath());
		if (cfg.exists())
			cfg.delete();
		FormatWriter<Configuration> writer = new FormatWriter<Configuration>();
		writer.write(config, getConfigPath());
	}
	
	public static void main(String[] args)
	{
		Logger.log("Started");
		Logger.log("Diagnostics (os.name): " + System.getProperty("os.name"));
		Logger.log("Diagnostics (os.version): " + System.getProperty("os.version"));
		Logger.log("Diagnostics (os.arch): " + System.getProperty("os.arch"));
		Logger.log("Diagnostics (java.version): " + System.getProperty("java.version"));
		Logger.log("Diagnostics (java.vendor): " + System.getProperty("java.vendor"));
		Logger.log("Diagnostics (sun.arch.data.model): " + System.getProperty("sun.arch.data.model"));

		try
		{
			// Find user's home directory if running from an Linux OS
			if (OS.getOS() == OS.Linux) Util.findLinuxHomeDirectory();

			String install_directory = Util.getInstallDirectory();
			String logs_directory = install_directory + "logs/";
			String bin_directory = install_directory + "bin/";
			String natives_directory = bin_directory + "natives";
			Logger.log("Install directory: " + install_directory);
			File inst_dir = new File(install_directory);
			if (!inst_dir.exists())
				inst_dir.mkdir();
			File logs_dir = new File(logs_directory);
			if (!logs_dir.exists())
				logs_dir.mkdir();
			File bin_dir = new File(bin_directory);
			if (!bin_dir.exists())
				bin_dir.mkdir();
			File natives_dir = new File(natives_directory);
			if (!natives_dir.exists())
				natives_dir.mkdir();
		} catch (Exception ex) {
			Logger.log("Something went wrong while creating the install directory!");
			System.exit(1);
		}
		
		Logger.log("Config path: " + getConfigPath());
		
		try
		{
			int latestVersion = Integer.parseInt(QueryAPI.get("https://os-mc.net/launcher/lv.php"));
			
			if (latestVersion > VERSION)
				updateAvailable = true;

			/*if (new File(Util.getInstallDirectory(), "ft.eol").exists())
				firstTime = false;
			else
				firstTime = true;*/

			firstTime = !new File(Util.getInstallDirectory(), "ft.lts").exists();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try
		{
			if (firstTime)
				deleteFile(getConfigPath());

		    if (!fileExists(getConfigPath()))
	        {
	            config = getDefaultConfig();
	            FormatWriter<Configuration> writer = new FormatWriter<Configuration>();
	            writer.write(config, getConfigPath());
	            Logger.log("Config was missing, so a new one was created with default values.");
	        } else {
	            FormatReader<Configuration> reader = new FormatReader<Configuration>();
	            config = reader.read(getConfigPath());
	            Logger.log("Finished reading config, no problems.");

	            config.legacyUI = false;

	            Logger.log("Config/disableUpdate: " + config.disableUpdate);
	            Logger.log("Config/rememberPassword: " + config.rememberPassword);
	            Logger.log("Config/legacyUI: " + config.legacyUI);
	            Logger.log("Config/disableJavaCheck: " + config.disableJavaCheck);
	        }
		} catch (Exception ex) {
		    deleteFile(getConfigPath());
		    config = getDefaultConfig();
		}

		if (!config.disableJavaCheck)
			if (!System.getProperty("java.version").contains("1.8"))
				problematicJava = true;

		try
		{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					if (config.legacyUI)
					{
					    LauncherFrame window = new LauncherFrame();
					    window.setVisible(true);
					} else {
					    MainFrame window = new MainFrame();
					    window.frmOldSchoolMinecraft.setVisible(true);
	                    window.frmOldSchoolMinecraft.setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Configuration getDefaultConfig()
	{
	    Configuration config = new Configuration();
        config.disableUpdate = false;
        config.rememberPassword = false;
        config.legacyUI = false;
        config.ramMb = 1024;
        return config;
	}
	
	public static boolean fileExists(String path)
	{
		return new File(path).exists();
	}
	
	public static boolean deleteFile(String path)
	{
		return new File(path).delete();
	}
	
	public static boolean createFile(String path)
	{
		try
		{
			return new File(path).createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void displayNotice()
	{
		JOptionPane.showMessageDialog(null, "Welcome to the OSML End of Life update. This software will not be receiving any updates in the future.\n\n" +
																	"You should be aware that Mojang accounts (or any official accounts) will no longer work.\n" +
																	"You MUST use an account registered on our website in order to play. You can use your email OR username.\n\n" +
																	"A replacement for this launcher is currently in development. Please join our Discord for more information.\n\n" +
																	"We're sorry for the inconvenience.\n" +
																	"- Team Omega", "End of Life", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void displayLTSNotice()
	{
		JOptionPane.showMessageDialog(null, "To buy us development time for Hydra Redux, we are applying some critical bug fixes to address recent issues.\n" +
																	"We aren't making any promises, but we will try our best to keep this launcher in working order.\n\n" +
																	"- Team Omega", "Long Term Service", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void warnJava()
	{
		JOptionPane.showMessageDialog(null, "Warning! A potentially problematic Java version has been detected.\n\n" +
																	">>> " + System.getProperty("java.version") + " <<<\n\n" +
																	"Things may not work properly if you don't have Java 8 (1.8.0) installed.", "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
