package me.moderator_man.osml.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ErrorFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ErrorFrame()
	{
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Oh noes! An oopsie has occurred.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 856, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		contentPane.setBackground(new Color(46, 52, 68, 255));
		
		JTextArea txtError = new JTextArea();
		txtError.setText("double_fault");
		txtError.setBounds(90, 85, 670, 338);
		contentPane.add(txtError);
		
		ImagePanel logoPanel = new ImagePanel("/logo.png");
		logoPanel.setBounds(324, 11, 201, 63);
		contentPane.add(logoPanel);
	}
}
