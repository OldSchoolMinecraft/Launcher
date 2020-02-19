package me.moderator_man.osml.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import me.moderator_man.osml.Main;

public class OptionsFrame extends JDialog
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create the dialog.
	 */
	public OptionsFrame()
	{
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		try
		{
			setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		setTitle("Options");
		setType(Type.POPUP);
		setResizable(false);
		setBounds(100, 100, 301, 160);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		{
			TexturedPanel backgroundPanel = new TexturedPanel();
			backgroundPanel.setBounds(0, 0, 295, 131);
			getContentPane().add(backgroundPanel);
			backgroundPanel.setLayout(null);
			
			JLabel lblRammb = new JLabel("RAM (mb)");
			lblRammb.setForeground(Color.WHITE);
			lblRammb.setBounds(10, 11, 55, 14);
			backgroundPanel.add(lblRammb);
			
			JSpinner ramAllocation = new JSpinner();
			ramAllocation.setModel(new SpinnerNumberModel(new Integer(1024), new Integer(1024), null, new Integer(1)));
			ramAllocation.setBounds(10, 28, 55, 20);
			backgroundPanel.add(ramAllocation);
			
			JButton btnOpenChangelog = new JButton("Open Changelog");
			btnOpenChangelog.setEnabled(false);
			btnOpenChangelog.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
					{
					    try
						{
							Desktop.getDesktop().browse(new URI("https://www.oldschoolminecraft.com/changelog"));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			btnOpenChangelog.setBounds(10, 97, 113, 23);
			backgroundPanel.add(btnOpenChangelog);
			
			JButton btnSaveClose = new JButton("Save & Close");
			btnSaveClose.setBounds(190, 97, 95, 23);
			backgroundPanel.add(btnSaveClose);
			
			JCheckBox cbKeepOpen = new JCheckBox("Keep launcher open");
			cbKeepOpen.setForeground(Color.WHITE);
			cbKeepOpen.setOpaque(false);
			cbKeepOpen.setBounds(164, 7, 121, 23);
			backgroundPanel.add(cbKeepOpen);
			
			JCheckBox cbOpenOutput = new JCheckBox("Open output log");
			cbOpenOutput.setForeground(Color.WHITE);
			cbOpenOutput.setOpaque(false);
			cbOpenOutput.setBounds(164, 27, 121, 23);
			backgroundPanel.add(cbOpenOutput);
			
			// set all the values from the config
			ramAllocation.setValue(Main.config.ramMb);
			cbKeepOpen.setSelected(Main.config.keepOpen);
			cbOpenOutput.setSelected(Main.config.openOutput);
			
			JCheckBox cbDisableUpdate = new JCheckBox("Disable update");
			cbDisableUpdate.setOpaque(false);
			cbDisableUpdate.setForeground(Color.WHITE);
			cbDisableUpdate.setBounds(164, 47, 121, 23);
			backgroundPanel.add(cbDisableUpdate);
			
			// save button action
			btnSaveClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Main.config.keepOpen = cbKeepOpen.isSelected();
					Main.config.openOutput = cbOpenOutput.isSelected();
					Main.config.disableUpdate = cbDisableUpdate.isSelected();
					Main.config.ramMb = (int)ramAllocation.getValue();
					
					Main.saveConfig();
					
					dispose();
				}
			});
		}
	}
}
