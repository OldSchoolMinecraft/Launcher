package me.moderator_man.osml.ui;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import me.moderator_man.osml.ui.legacy.TransparentCheckbox;
import me.moderator_man.osml.ui.legacy.TransparentLabel;

public class CosmeticsManager extends JDialog
{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNametagHex;
    
    private String sessionId;

    /**
     * Create the frame.
     */
    public CosmeticsManager(String sessionId)
    {
        this.sessionId = sessionId;
        
        try
        {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if (sessionId == null)
        {
            JOptionPane.showMessageDialog(null, "SessionID is null", "Oh noes!", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            return;
        }
        
        setTitle("Manage Cosmetics");
        setType(Type.NORMAL);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 240, 234);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        TexturedPanel backgroundPanel = new TexturedPanel();
        backgroundPanel.setBounds(0, 0, 234, 205);
        contentPane.add(backgroundPanel);
        backgroundPanel.setLayout(null);
        
        TransparentButton btnColorPicker = new TransparentButton("Color Picker");
        btnColorPicker.setBounds(116, 73, 107, 23);
        backgroundPanel.add(btnColorPicker);
        
        txtNametagHex = new JTextField();
        txtNametagHex.setBounds(10, 74, 96, 20);
        backgroundPanel.add(txtNametagHex);
        txtNametagHex.setColumns(10);
        
        TransparentCheckbox chckbxRainbow = new TransparentCheckbox("Rainbow");
        chckbxRainbow.setBounds(116, 43, 67, 23);
        backgroundPanel.add(chckbxRainbow);
        
        TransparentLabel lblNametagColor = new TransparentLabel("Nametag Color:");
        lblNametagColor.setBounds(10, 49, 78, 14);
        backgroundPanel.add(lblNametagColor);
        
        TransparentButton btnSave = new TransparentButton("Save");
        btnSave.setBounds(10, 175, 91, 23);
        backgroundPanel.add(btnSave);
        
        TransparentCheckbox chkbxEnableCloak = new TransparentCheckbox("Rainbow");
        chkbxEnableCloak.setText("Enable Cloak");
        chkbxEnableCloak.setBounds(10, 145, 89, 23);
        backgroundPanel.add(chkbxEnableCloak);
        
        TransparentButton btnChooseSkin = new TransparentButton("Color Picker");
        btnChooseSkin.setText("Choose Skin");
        btnChooseSkin.setBounds(10, 11, 96, 23);
        backgroundPanel.add(btnChooseSkin);
        
        TransparentButton btnChooseCloak = new TransparentButton("Color Picker");
        btnChooseCloak.setText("Choose Cloak");
        btnChooseCloak.setBounds(116, 11, 107, 23);
        backgroundPanel.add(btnChooseCloak);
        
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
    }
}
