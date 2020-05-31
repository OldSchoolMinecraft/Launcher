package me.moderator_man.osml.util;

import java.io.PrintWriter;

public class Logger
{
	private static StringBuilder log = new StringBuilder();
	
	public static void log(String str)
	{
		log(str, true);
	}
	
	public static void log(String str, boolean sysout)
	{
		log.append(str);
		log.append("\n");
		
		if (sysout)
			System.out.println(str);
	}
	
	public static void saveLog()
	{
		try
		{
			try (PrintWriter out = new PrintWriter(Util.getCurrentLogFile()))
			{
			    out.println(log.toString());
			}
			
			System.out.println("Log was saved to: " + Util.getCurrentLogFile());
		} catch (Exception ex) {
			System.out.println("Couldn't save log: " + ex.getMessage());
		}
	}
}
