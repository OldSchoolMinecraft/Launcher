package me.moderator_man.osml.launch;

import java.io.BufferedReader;
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
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in));
			
			String s = null;
			while ((s = stdInput.readLine()) != null)
				Logger.log(s, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
