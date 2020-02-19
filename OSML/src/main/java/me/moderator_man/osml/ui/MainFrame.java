package me.moderator_man.osml.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import me.moderator_man.osml.Main;
import me.moderator_man.osml.launch.Launcher;
import me.moderator_man.osml.util.QueryAPI;
import me.moderator_man.osml.util.Util;

public class MainFrame
{
	public static MainFrame mainFrame;
	
	public JFrame frmOldSchoolMinecraft;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	/**
	 * Create the application.
	 */
	public MainFrame()
	{
		mainFrame = this;
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmOldSchoolMinecraft = new JFrame();
		frmOldSchoolMinecraft.setTitle("Old School Minecraft Launcher");
		frmOldSchoolMinecraft.setResizable(false);
		frmOldSchoolMinecraft.setBounds(100, 100, 468, 250);
		frmOldSchoolMinecraft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOldSchoolMinecraft.getContentPane().setLayout(null);
		frmOldSchoolMinecraft.setLocationRelativeTo(null);
		
		try
		{
			frmOldSchoolMinecraft.setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		TexturedPanel backgroundPanel = new TexturedPanel();
		backgroundPanel.setBounds(0, 0, 457, 221);
		frmOldSchoolMinecraft.getContentPane().add(backgroundPanel);
		backgroundPanel.setLayout(null);
		
		ImagePanel logoPanel = new ImagePanel("/logo.png");
		logoPanel.setBackground(new Color(0, 0, 0, 0));
		logoPanel.setBounds(0, 133, 201, 63);
		backgroundPanel.add(logoPanel);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(269, 133, 86, 20);
		backgroundPanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(269, 167, 86, 20);
		backgroundPanel.add(txtPassword);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					OptionsFrame dialog = new OptionsFrame();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnOptions.setBounds(365, 132, 79, 23);
		backgroundPanel.add(btnOptions);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(365, 166, 79, 23);
		backgroundPanel.add(btnLogin);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(211, 136, 58, 14);
		backgroundPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(211, 170, 58, 14);
		backgroundPanel.add(lblPassword);
		
		ImagePanel websitePanel = new ImagePanel("/favicon.png");
		websitePanel.setBackground(new Color(0, 0, 0, 0));
		websitePanel.setBounds(10, 11, 64, 64);
		backgroundPanel.add(websitePanel);
		
		ImagePanel discordPanel = new ImagePanel("/disc.png");
		discordPanel.setBackground(new Color(0, 0, 0, 0));
		discordPanel.setBounds(84, 11, 64, 64);
		backgroundPanel.add(discordPanel);
		
		ImagePanel youtubePanel = new ImagePanel("/youtube2.png");
		youtubePanel.setBackground(new Color(0, 0, 0, 0));
		youtubePanel.setBounds(158, 11, 64, 64);
		backgroundPanel.add(youtubePanel);
		
		JCheckBox cbRememberPassword = new JCheckBox("Remember password?");
		cbRememberPassword.setForeground(Color.WHITE);
		cbRememberPassword.setOpaque(false);
		cbRememberPassword.setBounds(317, 188, 134, 23);
		backgroundPanel.add(cbRememberPassword);
		
		JLabel lblUpdateRequired = new JLabel("There is a launcher update available!");
		lblUpdateRequired.setForeground(Color.GREEN);
		lblUpdateRequired.setBounds(257, 11, 177, 14);
		if (Main.updateAvailable)
			backgroundPanel.add(lblUpdateRequired);
		
		JLabel lblDownloading = new JLabel("Downloading...");
		lblDownloading.setVisible(false);
		lblDownloading.setForeground(Color.GREEN);
		lblDownloading.setBounds(211, 192, 81, 14);
		backgroundPanel.add(lblDownloading);
		
		frmOldSchoolMinecraft.setFocusTraversalPolicy(new SimpleFocusTraversalPolicy(txtUsername, txtPassword, btnLogin, btnOptions));
		
		if (Main.config.rememberPassword)
		{
			cbRememberPassword.setSelected(true);
			txtUsername.setText(Main.config.username);
			txtPassword.setText(Main.config.password);
		}
		
		websitePanel.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
					Util.openNetpage("https://www.oldschoolminecraft.com");
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		discordPanel.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
					Util.openNetpage("https://www.oldschoolminecraft.com/discord");
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		youtubePanel.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
					Util.openNetpage("https://www.youtube.com/channel/UCLL91k_YC7J9E_bey3IX3sw");
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		btnLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (cbRememberPassword.isSelected())
				{
					Main.config.rememberPassword = true;
					Main.config.username = txtUsername.getText();
					Main.config.password = String.valueOf(txtPassword.getPassword());
					Main.saveConfig();
				}
				
				new Launcher().launch(txtUsername.getText().trim(), QueryAPI.getNewSession(txtUsername.getText(), String.valueOf(txtPassword.getPassword())));
			}
		});
		
		if (Main.updateAvailable)
		{
			int dialogResult = JOptionPane.showConfirmDialog(null, "An update is available! Would you like to download it?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(dialogResult == JOptionPane.YES_OPTION)
			{
				Util.openNetpage("https://www.oldschoolminecraft.com/launcher/new/Minecraft%20Launcher.jar");
				System.exit(0);
			}
		}
	}
	
	public void hide()
	{
		frmOldSchoolMinecraft.setVisible(false);
	}
	
	public void show()
	{
		frmOldSchoolMinecraft.setVisible(true);
	}
	
	public void setDownloadingMessage()
	{
		JPanel root = (JPanel) frmOldSchoolMinecraft.getContentPane();
		JPanel bgp = (JPanel) root.getComponent(0);
		JLabel dl = (JLabel) bgp.getComponent(Main.updateAvailable ? 12 : 11);
		dl.setText("Downloading...");
		dl.setVisible(true);
	}
	
	public void setLaunchingMessage()
	{
		JPanel root = (JPanel) frmOldSchoolMinecraft.getContentPane();
		JPanel bgp = (JPanel) root.getComponent(0);
		JLabel dl = (JLabel) bgp.getComponent(Main.updateAvailable ? 12 : 11);
		
		dl.setText("Launching...");
		dl.setVisible(true);
	}
}
