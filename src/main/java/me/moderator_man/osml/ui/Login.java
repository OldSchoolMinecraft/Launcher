package me.moderator_man.osml.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import me.moderator_man.osml.LoginPipe;
import me.moderator_man.osml.auth.LegacyAuthenticator;
import me.moderator_man.osml.ui.legacy.TransparentLabel;

public class Login extends JDialog
{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JButton btnLogin;
    
    /**
     * Create the frame.
     */
    public Login(LoginPipe pipe)
    {
        setTitle("Login");
        setType(Type.NORMAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 250, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        TexturedPanel backgroundPanel = new TexturedPanel();
        backgroundPanel.setBounds(0, 0, 234, 261);
        contentPane.add(backgroundPanel);
        backgroundPanel.setLayout(null);
        
        TransparentLabel lblInfo = new TransparentLabel("Please login with your OSM account.");
        lblInfo.setBounds(30, 38, 174, 14);
        backgroundPanel.add(lblInfo);
        
        TransparentLabel lblInfo2 = new TransparentLabel("Please login with your OSM account.");
        lblInfo2.setText("Do NOT use your Mojang account.");
        lblInfo2.setBounds(34, 49, 165, 14);
        backgroundPanel.add(lblInfo2);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(85, 113, 119, 20);
        backgroundPanel.add(txtUsername);
        txtUsername.setColumns(10);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(85, 134, 119, 20);
        backgroundPanel.add(txtPassword);
        txtPassword.setColumns(10);
        
        btnLogin = new TransparentButton("Login");
        btnLogin.setBounds(131, 158, 73, 23);
        backgroundPanel.add(btnLogin);
        
        lblUsername = new TransparentLabel("Username:");
        lblUsername.setBounds(30, 116, 54, 14);
        backgroundPanel.add(lblUsername);
        
        lblPassword = new TransparentLabel("Password:");
        lblPassword.setBounds(30, 137, 54, 14);
        backgroundPanel.add(lblPassword);
        
        btnLogin.addActionListener((event) ->
        {
            LegacyAuthenticator auth = new LegacyAuthenticator();
            auth.tryAuth(txtUsername.getText(), new String(txtPassword.getPassword()));
            if (auth.isAuthenticated())
            {
                setVisible(false);
                pipe.onLogin(auth.getSessionID());
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!", "Oh noes!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        TransparentLabel accountLink = new TransparentLabel("Need account?")
        {
            private static final long serialVersionUID = 0L;

            public void paint(Graphics g)
            {
                super.paint(g);

                int x = 0;
                int y = 0;

                FontMetrics fm = g.getFontMetrics();
                int width = fm.stringWidth(getText());
                int height = fm.getHeight();

                if (getAlignmentX() == 2.0F)
                    x = 0;
                else if (getAlignmentX() == 0.0F)
                    x = getBounds().width / 2 - width / 2;
                else if (getAlignmentX() == 4.0F)
                    x = getBounds().width - width;
                y = getBounds().height / 2 + height / 2 - 1;

                g.drawLine(x + 2, y, x + width - 2, y);
            }

            public void update(Graphics g)
            {
                paint(g);
            }
        };
        accountLink.setCursor(Cursor.getPredefinedCursor(12));
        accountLink.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent arg0)
            {
                try
                {
                    Desktop.getDesktop().browse(new URL("https://os-mc.net/register").toURI());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        accountLink.setForeground(new Color(8421631));
        accountLink.setText("Need account?");
        accountLink.setBounds(30, 162, 73, 14);
        backgroundPanel.add(accountLink);
        
        setLocationRelativeTo(null);
        requestFocus();
        setModalityType(ModalityType.APPLICATION_MODAL);
    }
}
