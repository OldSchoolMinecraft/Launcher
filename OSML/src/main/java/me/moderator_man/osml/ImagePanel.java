package me.moderator_man.osml;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private Image scaledImage;

	public ImagePanel(String path)
	{
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException ex) {}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if (scaledImage == null)
		{
			scaledImage = image.getScaledInstance(this.getWidth(), getHeight(), Image.SCALE_SMOOTH);
		}
		
		g.drawImage(scaledImage, 0, 0, this);
	}
}
