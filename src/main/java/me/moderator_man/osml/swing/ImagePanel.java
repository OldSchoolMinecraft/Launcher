package me.moderator_man.osml.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Image img;
	private Image bgImage;

	public ImagePanel(String image)
	{
		this(image, 32, 32);
	}

	public ImagePanel(String image, int width, int height)
	{
		setOpaque(true);
		try
		{
			InputStream resource = getClass().getClassLoader().getResourceAsStream(image);
			BufferedImage preImage = ImageIO.read(resource);
			Image postImage = preImage.getScaledInstance(width, height, 16);
			this.bgImage = postImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void paintComponent(Graphics g2)
	{
		int w = getWidth() / 2 + 1;
		int h = getHeight() / 2 + 1;
		if ((this.img == null) || (this.img.getWidth(null) != w) || (this.img.getHeight(null) != h))
		{
			this.img = createImage(w, h);

			Graphics g = this.img.getGraphics();
			for (int x = 0; x <= w / 32; x++)
			{
				for (int y = 0; y <= h / 32; y++)
					g.drawImage(this.bgImage, x * 32, y * 32, null);
			}
			if ((g instanceof Graphics2D))
			{
				Graphics2D gg = (Graphics2D) g;
				int gh = 1;
				gg.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(553648127, true), new Point2D.Float(0.0F, gh), new Color(0, true)));
				gg.fillRect(0, 0, w, gh);

				gh = h;
				gg.setPaint(new GradientPaint(new Point2D.Float(0.0F, 0.0F), new Color(0, true), new Point2D.Float(0.0F, gh), new Color(1610612736, true)));
				gg.fillRect(0, 0, w, gh);
			}
			g.dispose();
		}
		g2.drawImage(this.img, 0, 0, w * 2, h * 2, null);
	}
}