package me.moderator_man.osml;

import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import me.moderator_man.osml.io.FormatReader;
import me.moderator_man.osml.io.FormatWriter;

public class Main
{
	private static final int VERSION = 0;
	
	public static Configuration config;
	public static boolean updateAvailable = false;
	
	private static void log(String str)
	{
		System.out.println(str);
	}
	
	private static String getConfigPath()
	{
		switch (OS.getOS())
		{
			default:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/launcher.cfg".replaceAll("/", "\\\\");
			case Windows:
				return System.getProperty("user.home") + "/AppData/Roaming/.osm/launcher.cfg".replaceAll("/", "\\\\");
			case Mac:
				return String.format("~/Library/Application Support/osm/launcher.cfg");
			case Linux:
				return "~/.osm/launcher.cfg";
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
		log("Started");
		
		try
		{
			String install_directory = Util.getInstallDirectory();
			String bin_directory = Util.getInstallDirectory() + "bin/";
			String natives_directory = bin_directory + "natives";
			log("Install directory: " + install_directory);
			File inst_dir = new File(install_directory);
			if (!inst_dir.exists())
				inst_dir.mkdir();
			File bin_dir = new File(bin_directory);
			if (!bin_dir.exists())
				bin_dir.mkdir();
			File natives_dir = new File(natives_directory);
			if (!natives_dir.exists())
				natives_dir.mkdir();
		} catch (Exception ex) {
			log("Something went wrong while creating the install directory!");
			System.exit(1);
		}
		
		log("Config path: " + getConfigPath());
		
		try
		{
			int latestVersion = Integer.parseInt(QueryAPI.get("https://www.oldschoolminecraft.com/launcher/lv.php"));
			
			if (latestVersion > VERSION)
			{
				updateAvailable = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (!new File(getConfigPath()).exists())
		{
			config = new Configuration();
			config.keepOpen = false;
			config.openOutput = false;
			config.forceUpdate = false;
			config.rememberPassword = false;
			config.ramMb = 1024;
			FormatWriter<Configuration> writer = new FormatWriter<Configuration>();
			writer.write(config, getConfigPath());
			log("Config was missing, so a new one was created with default values.");
		} else {
			FormatReader<Configuration> reader = new FormatReader<Configuration>();
			config = reader.read(getConfigPath());
			log("Finished reading config, no problems.");
		}
		
		if (config.ramMb > 65536)
			config.ramMb = 65536;
		if (config.ramMb < 1024)
			config.ramMb = 1024;
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainFrame window = new MainFrame();
					window.frmOldSchoolMinecraft.setVisible(true);
					window.frmOldSchoolMinecraft.setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
