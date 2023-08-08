package me.moderator_man.osml.swing.tabs;

import me.moderator_man.osml.swing.ImagePanel;

import javax.swing.*;
import java.awt.*;

public class OptionsTab extends AbstractTab
{
    public JPanel root = new ImagePanel("stone.gif");
    public JTextField txtInstanceName = new JTextField();
    public JComboBox<String> cmbGameVersion = new JComboBox<>();
    public JTextField txtJavaExecutable = new JTextField();
    public JTextField txtJVMArguments = new JTextField();
    public JCheckBox chkProxy = new JCheckBox("Enable OSM skin proxy");
    public JButton btnSaveChanges = new JButton("Save Changes");
    public JLabel lblUnsavedChanges = new JLabel("You have unsaved changes!");
    public JLabel lblInstanceName = new JLabel("Instance Name");
    public JLabel lblJavaExecutable = new JLabel("Java Executable");
    public JLabel lblJVMArguments = new JLabel("JVM Arguments");

    public OptionsTab()
    {
        super("Options");
    }

    public JPanel build()
    {
        root.setLayout(null);

        lblInstanceName.setBounds(10, 5, 150, 20);
        txtInstanceName.setBounds(10, 25, 150, 25);
        lblJavaExecutable.setBounds(10, 65, 150, 20);
        txtJavaExecutable.setBounds(10, 85, 250, 25);
        lblJVMArguments.setBounds(10, 110, 150, 20);
        txtJVMArguments.setBounds(10, 130, 250, 25);

        chkProxy.setBounds(10, 300 - 80, 180, 20);
        lblUnsavedChanges.setForeground(Color.RED);
        lblUnsavedChanges.setBounds(5, 440, 200, 20);

        btnSaveChanges.setBounds(400 - 145 - 5, 450 - 10, 145, 20);

        root.add(chkProxy);
        root.add(txtInstanceName);
        root.add(txtJavaExecutable);
        root.add(txtJVMArguments);
        root.add(btnSaveChanges);
        root.add(lblUnsavedChanges);
        root.add(lblInstanceName);
        root.add(lblJavaExecutable);
        root.add(lblJVMArguments);

        return root;
    }
}
