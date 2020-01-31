package me.moderator_man.osml.launch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameLogThread extends Thread
{
	private GameThread gameThread;
	
	public GameLogThread(GameThread gameThread)
	{
		this.gameThread = gameThread;
	}
	
	public void run()
	{
		try
		{
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(gameThread.getInputStream()));
			
			String s = null;
			while ((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
			}
			
			System.out.println("GameLogThread finished");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
