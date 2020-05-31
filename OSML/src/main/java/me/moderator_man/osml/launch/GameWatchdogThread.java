package me.moderator_man.osml.launch;

import me.moderator_man.osml.util.Logger;

public class GameWatchdogThread extends Thread
{
	private Process process;
	
	public GameWatchdogThread(Process process)
	{
		this.process = process;
	}
	
	public void run()
	{
		try
		{
			GameLogThread glThread = new GameLogThread(process);
			glThread.start();
			
			while (true)
			{
				int exit = process.waitFor();
				Logger.log("Process exited with status: " + exit);
				glThread.interrupt();
				Logger.saveLog();
				System.exit(exit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
