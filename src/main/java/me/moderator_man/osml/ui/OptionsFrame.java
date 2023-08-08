package me.moderator_man.osml.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
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
    private boolean nameOverrideOnCreated;

    /**
     * Create the dialog.
     */
    public OptionsFrame()
    {
        legacyOnCreated = Main.config.legacyUI;
        nameOverrideOnCreated = Main.config.disableJavaCheck;

        setModalityType(ModalityType.APPLICATION_MODAL);

        try
        {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
        } catch (Exception ex)
        {
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
            ramAllocation.setModel(new SpinnerNumberModel(1024, 1024, null, 1));
            ramAllocation.setBounds(10, 28, 55, 20);
            backgroundPanel.add(ramAllocation);

            TransparentButton btnDisplayNotice = new TransparentButton("LTS Notice");
            btnDisplayNotice.setBounds(190, 70, 95, 23);
            backgroundPanel.add(btnDisplayNotice);

            TransparentButton btnSaveClose = new TransparentButton("Save & Close");
            btnSaveClose.setBounds(190, 97, 95, 23);
            backgroundPanel.add(btnSaveClose);

            JCheckBox cbDisableUpdate = new JCheckBox("Disable updates");
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
            cbLegacyUI.setEnabled(false);
            cbLegacyUI.setVisible(false);
            backgroundPanel.add(cbLegacyUI);

            // set all the values from the config
            ramAllocation.setValue(Main.config.ramMb);
            cbDisableUpdate.setSelected(Main.config.disableUpdate);
            cbLegacyUI.setSelected(Main.config.legacyUI);

            JCheckBox cbDisableJavaCheck = new JCheckBox("Disable Java version check");
            cbDisableJavaCheck.setOpaque(false);
            cbDisableJavaCheck.setForeground(Color.WHITE);
            cbDisableJavaCheck.setBounds(10, 80, 200, 23);
            cbDisableJavaCheck.setSelected(Main.config.disableJavaCheck);
            backgroundPanel.add(cbDisableJavaCheck);

            JCheckBox cbDisableBitDepthFix = new JCheckBox("Disable BitDepthFix");
            cbDisableBitDepthFix.setOpaque(false);
            cbDisableBitDepthFix.setForeground(Color.WHITE);
            cbDisableBitDepthFix.setBounds(10, 63, 200, 23);
            cbDisableBitDepthFix.setSelected(Main.config.disableBitDepthFix);
            backgroundPanel.add(cbDisableBitDepthFix);

            TransparentButton btnCosmetics = new TransparentButton("Cosmetics");
            btnCosmetics.setEnabled(cbDisableJavaCheck.isSelected());
            btnCosmetics.setBounds(190, 65, 95, 23);
            btnCosmetics.setVisible(false);
            backgroundPanel.add(btnCosmetics);

            JLabel lblVersion = new JLabel("OSML v00");
            lblVersion.setForeground(Color.WHITE);
            lblVersion.setBounds(200, 10, 100, 14);
            backgroundPanel.add(lblVersion);

            lblVersion.setText("OSML v" + Main.VERSION + " (LTS)");

            // cosmetics button action
            btnCosmetics.addActionListener((event) -> new CosmeticsManager().setVisible(true));

            // EoL notice button -- REPURPOSED FOR LTS NOTICE
            btnDisplayNotice.addActionListener((event) -> Main.displayLTSNotice());

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
                            Main.config.legacyUI = false;
                            Main.config.ramMb = (int) ramAllocation.getValue();
                            Main.config.disableJavaCheck = cbDisableJavaCheck.isSelected();
                            Main.config.disableBitDepthFix = cbDisableBitDepthFix.isSelected();

                            saveAndClose();
                            System.exit(0);
                            return;
                        } else {
                            cbLegacyUI.setSelected(false);
                        }
                    }

                    Main.config.disableUpdate = cbDisableUpdate.isSelected();
                    Main.config.legacyUI = false;
                    Main.config.ramMb = (int) ramAllocation.getValue();
                    Main.config.disableJavaCheck = cbDisableJavaCheck.isSelected();
                    Main.config.disableBitDepthFix = cbDisableBitDepthFix.isSelected();

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
