package me.moderator_man.osml.ui;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CosmeticsManager extends JDialog
{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public CosmeticsManager()
    {
        try
        {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        setTitle("Manage Cosmetics");
        setType(Type.NORMAL);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 240, 148);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        TexturedPanel backgroundPanel = new TexturedPanel();
        backgroundPanel.setBounds(0, 0, 234, 119);
        contentPane.add(backgroundPanel);
        backgroundPanel.setLayout(null);
        
        TransparentButton btnCloakGallery = new TransparentButton("Cloak Gallery");
        btnCloakGallery.setBounds(10, 11, 95, 23);
        backgroundPanel.add(btnCloakGallery);
        
        TransparentButton btnUploadCloak = new TransparentButton("Upload Cloak");
        btnUploadCloak.setBounds(129, 11, 95, 23);
        backgroundPanel.add(btnUploadCloak);
        
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
    }
}
