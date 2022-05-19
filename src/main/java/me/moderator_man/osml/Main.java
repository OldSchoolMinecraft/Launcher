package me.moderator_man.osml;

import java.io.File;
import javax.swing.*;

import javafx.application.Application;
import javafx.stage.Stage;
import me.moderator_man.osml.io.FormatWriter;
import me.moderator_man.osml.redux.Redux;
import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.Util;

public class Main extends Application
{
	public static final int VERSION = 18;

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
				return Util.getLinuxHomeDirectory() + "/.osm/launcher.cfg";
		}
	}
	
	public static void saveConfig()
	{
		File cfg = new File(getConfigPath());
		if (cfg.exists()) cfg.delete();
		FormatWriter<Configuration> writer = new FormatWriter<>();
		writer.write(config, getConfigPath());
	}

	@Override
	public void start(Stage primaryStage)
	{
		new Redux().init(primaryStage);
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

		launch();
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

	public static void displayLTSNotice()
	{
		JOptionPane.showMessageDialog(null, "Old School Minecraft is now offering extended support for OSML. Expect gradual improvements & tweaks.\n" +
																	"We aren't making any promises, but we will try our best to keep this launcher in working order.\n\n" +
																	"- moderator_man", "Long Term Service", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void warnJava()
	{
		JOptionPane.showMessageDialog(null, "Warning! A potentially problematic Java version has been detected.\n\n" +
																	">>> " + System.getProperty("java.version") + " <<<\n\n" +
																	"Things may not work properly if you don't have Java 8 (1.8.0) installed.", "Warning", JOptionPane.WARNING_MESSAGE);
	}
}
