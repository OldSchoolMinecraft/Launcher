package me.moderator_man.osml.launch;

import java.io.InputStream;

public class GameThread extends Thread
{
	private Process process;
	
	public GameThread(Process process)
	{
		this.process = process;
	}
	
	public void run()
	{
		try
		{
			GameLogThread logThread = new GameLogThread(this);
			logThread.start();
			process.waitFor();
			logThread.join();
			System.out.println("GameThread finished");
			System.exit(0);
		} catch (InterruptedException e) {
			System.out.println("Game thread interrupted: " + e.getMessage());
			System.exit(0);
		}
	}
	
	public InputStream getInputStream()
	{
		return process.getInputStream();
	}
}
