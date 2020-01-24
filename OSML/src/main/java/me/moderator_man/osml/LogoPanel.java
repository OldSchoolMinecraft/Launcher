package me.moderator_man.osml;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Image bgImage;

	public LogoPanel()
	{
		setOpaque(true);
		try
		{
			BufferedImage src = ImageIO.read(getClass().getClassLoader().getResource("logo.png"));
			int w = src.getWidth();
			int h = src.getHeight();
			this.bgImage = src.getScaledInstance(256, 49, 16);
			setPreferredSize(new Dimension(246 + 32, 49 + 32));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void paintComponent(Graphics g2)
	{
		g2.drawImage(this.bgImage, 24, 24, null);
	}
}