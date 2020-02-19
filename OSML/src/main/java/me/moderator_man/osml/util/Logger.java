package me.moderator_man.osml.util;

import java.io.PrintWriter;

public class Logger
{
	private static StringBuilder log = new StringBuilder();
	
	public static void log(String str)
	{
		log.append(str);
		log.append("\n");
		
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
		} catch (Exception ex) {
			System.out.println("Couldn't save log: " + ex.getMessage());
		}
	}
}
