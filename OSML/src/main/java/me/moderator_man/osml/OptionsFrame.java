package me.moderator_man.osml;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import java.awt.Color;

public class OptionsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public OptionsFrame()
	{
		setResizable(false);
		setTitle("Options");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 254, 167);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		try
		{
			setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		TexturedPanel backgroundPanel = new TexturedPanel();
		backgroundPanel.setBounds(0, 0, 248, 139);
		contentPane.add(backgroundPanel);
		backgroundPanel.setLayout(null);
		
		JButton btnOpenChangelog = new JButton("Open Changelog");
		btnOpenChangelog.setBounds(10, 105, 113, 23);
		backgroundPanel.add(btnOpenChangelog);
		
		JButton btnSaveClose = new JButton("Save & Close");
		btnSaveClose.setBounds(143, 105, 95, 23);
		backgroundPanel.add(btnSaveClose);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(1024), null, null, new Integer(1)));
		spinner.setBounds(10, 28, 83, 20);
		backgroundPanel.add(spinner);
		
		JLabel lblRam = new JLabel("RAM (mb)");
		lblRam.setForeground(Color.WHITE);
		lblRam.setBounds(10, 11, 60, 14);
		backgroundPanel.add(lblRam);
	}
}
