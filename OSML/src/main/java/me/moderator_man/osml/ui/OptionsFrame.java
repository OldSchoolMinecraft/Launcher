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
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import me.moderator_man.osml.Main;

public class OptionsFrame extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private boolean legacyOnCreated;
	
	/**
	 * Create the dialog.
	 */
	public OptionsFrame()
	{
	    legacyOnCreated = Main.config.legacyUI;
	    
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		try
		{
			setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		setTitle("Options");
		setType(Type.NORMAL);
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
			
			TransparentButton btnSaveClose = new TransparentButton("Save & Close");
			btnSaveClose.setBounds(190, 97, 95, 23);
			backgroundPanel.add(btnSaveClose);
			
			JCheckBox cbDisableUpdate = new JCheckBox("Disable update");
			cbDisableUpdate.setOpaque(false);
			cbDisableUpdate.setForeground(Color.WHITE);
			cbDisableUpdate.setBounds(10, 97, 113, 23);
			backgroundPanel.add(cbDisableUpdate);
			
			// update layout to prevent bugs
			update(getGraphics());
			
			JCheckBox cbLegacyUI = new JCheckBox("Legacy UI");
			cbLegacyUI.setOpaque(false);
			cbLegacyUI.setForeground(Color.WHITE);
			cbLegacyUI.setBounds(10, 77, 111, 23);
			backgroundPanel.add(cbLegacyUI);
			
			// set all the values from the config
            ramAllocation.setValue(Main.config.ramMb);
            cbDisableUpdate.setSelected(Main.config.disableUpdate);
            cbLegacyUI.setSelected(Main.config.legacyUI);
			
			// save button action
			btnSaveClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (cbLegacyUI.isSelected() != legacyOnCreated)
					{
					    int n = JOptionPane.showConfirmDialog(
					            null,
					            "A restart is required to change this option! Would you like to continue?\nYou will have to start the launcher again manually.",
					            "Wait!",
					            JOptionPane.YES_NO_OPTION);
					    if (n == JOptionPane.YES_OPTION)
					    {
					        Main.config.disableUpdate = cbDisableUpdate.isSelected();
					        Main.config.legacyUI = cbLegacyUI.isSelected();
					        Main.config.ramMb = (int) ramAllocation.getValue();
					        
					        saveAndClose();
					        System.exit(0);
					        return;
					    } else {
					        cbLegacyUI.setSelected(false);
					    }
					}
					
					Main.config.disableUpdate = cbDisableUpdate.isSelected();
                    Main.config.legacyUI = cbLegacyUI.isSelected();
                    Main.config.ramMb = (int)ramAllocation.getValue();
                    
                    saveAndClose();
				}
			});
		}
	}
	
	private void saveAndClose()
	{
	    Main.saveConfig();
	    dispose();
	}
}
