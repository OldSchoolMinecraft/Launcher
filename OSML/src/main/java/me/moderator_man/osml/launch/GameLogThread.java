package me.moderator_man.osml.launch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.moderator_man.osml.util.Logger;

public class GameLogThread extends Thread
{
	private Process process;
	
	public GameLogThread(Process process)
	{
		this.process = process;
	}
	
	public void run()
	{
		try
		{
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String s = null;
			while ((s = stdInput.readLine()) != null)
			{
				Logger.log(s);
			}
			
			Logger.log("GameLogThread finished");
			
			if (!process.isAlive() && process.exitValue() != 0)
			{
				//TODO: display error window?
			}
			
			Logger.saveLog();
			
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
