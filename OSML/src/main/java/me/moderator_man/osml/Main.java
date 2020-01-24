package me.moderator_man.osml;

import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

public class Main
{
	private static void log(String str)
	{
		System.out.println(str);
	}
	
	public static void main(String[] args)
	{
		log("Started");
		
		try
		{
			String install_directory = Util.getInstallDirectory();
			File inst_dir = new File(install_directory);
			if (!inst_dir.exists())
				inst_dir.mkdir();
		} catch (Exception ex) {
			log("Something went wrong while creating the install directory!");
			System.exit(1);
		}
		
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
