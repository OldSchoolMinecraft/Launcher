package me.moderator_man.osml;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainFrame
{
	public JFrame frmOldSchoolMinecraft;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	/**
	 * Create the application.
	 */
	public MainFrame()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmOldSchoolMinecraft = new JFrame();
		frmOldSchoolMinecraft.setResizable(false);
		frmOldSchoolMinecraft.setTitle("Old School Minecraft Launcher");
		frmOldSchoolMinecraft.setBounds(100, 100, 446, 209);
		frmOldSchoolMinecraft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOldSchoolMinecraft.setLocationRelativeTo(null);
		frmOldSchoolMinecraft.getContentPane().setLayout(null);
		
		TexturedPanel backgroundPanel = new TexturedPanel();
		backgroundPanel.setBounds(0, 0, 440, 180);
		frmOldSchoolMinecraft.getContentPane().add(backgroundPanel);
		backgroundPanel.setLayout(null);
		
		LogoPanel logoPanel = new LogoPanel();
		logoPanel.setBounds(-30, 99, 268, 81);
		backgroundPanel.add(logoPanel);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(203, 119, 52, 14);
		backgroundPanel.add(lblUsername);
		lblUsername.setForeground(Color.WHITE);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(203, 150, 52, 14);
		backgroundPanel.add(lblPassword);
		lblPassword.setForeground(Color.WHITE);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(265, 147, 86, 20);
		backgroundPanel.add(txtPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(265, 116, 86, 20);
		backgroundPanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		TransparentButton btnOptions = new TransparentButton("Options");
		btnOptions.setBounds(361, 115, 69, 23);
		backgroundPanel.add(btnOptions);
		
		TransparentButton btnLogin = new TransparentButton("Login");
		btnLogin.setBounds(361, 146, 69, 23);
		backgroundPanel.add(btnLogin);
		
		ImagePanel websiteLogo = new ImagePanel("/favicon.png");
		websiteLogo.setBackground(new Color(0, 0, 0, 0));
		websiteLogo.setBounds(10, 11, 64, 64);
		backgroundPanel.add(websiteLogo);
		
		ImagePanel discordLogo = new ImagePanel("/disc.png");
		discordLogo.setBackground(new Color(0, 0, 0, 0));
		discordLogo.setBounds(84, 11, 64, 64);
		backgroundPanel.add(discordLogo);
		
		ImagePanel youtubeLogo = new ImagePanel("/youtube2.png");
		youtubeLogo.setBackground(new Color(0, 0, 0, 0));
		youtubeLogo.setBounds(158, 11, 64, 64);
		backgroundPanel.add(youtubeLogo);
		
		frmOldSchoolMinecraft.setFocusTraversalPolicy(new SimpleFocusTraversalPolicy
		(
            txtUsername,
            txtPassword,
            btnLogin,
            btnOptions
        ));
		
		//News news = QueryAPI.getNews();
		
		// set the tab order of the elements
		frmOldSchoolMinecraft.setFocusTraversalPolicyProvider(true);
	}
}
