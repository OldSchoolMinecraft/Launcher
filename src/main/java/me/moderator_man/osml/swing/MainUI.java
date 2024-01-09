package me.moderator_man.osml.swing;

import com.formdev.flatlaf.FlatClientProperties;
import me.moderator_man.osml.instance.Instance;
import me.moderator_man.osml.swing.tabs.ModsTab;
import me.moderator_man.osml.swing.tabs.OptionsTab;
import me.moderator_man.osml.swing.tabs.ServersTab;
import me.moderator_man.osml.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainUI
{
    private JFrame frame;
    private JRootPane root;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnPlay;
    private JButton btnAddInstance;
    private JButton btnDelInstance;
    private ImagePanel optionsBackgroundPanel;
    private ImagePanel mainBackgroundPanel;
    private JComboBox<String> cmbInstances;
    private JTabbedPane tabOptions;
    private OptionsTab optionsTab;
    private ModsTab modsTab;
    private ServersTab serversTab;
    private JButton btnMinimize;
    private JButton btnClose;
    private FontManager fontManager;

    public void init() throws Exception
    {
        // build, init, show UI
        frame = new JFrame();
        root = new JRootPane();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnPlay = new JButton("Play");
        optionsBackgroundPanel = new ImagePanel("dirt.png");
        mainBackgroundPanel = new ImagePanel("stone.gif");
        cmbInstances = new JComboBox<>();
        tabOptions = new JTabbedPane();
        btnAddInstance = new JButton("New");
        btnDelInstance = new JButton("Delete");
        optionsTab = new OptionsTab();
        modsTab = new ModsTab();
        serversTab = new ServersTab(this);
        btnMinimize = new JButton();
        btnClose = new JButton();
        fontManager = new FontManager("Karla");
//        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/fonts/Karla-VariableFont_wght.ttf")));

        JLabel lblUsername = new JLabel("<html><u>Not logged in!</u></html>");
        JLabel lblPassword = new JLabel("Password: ");
        JToolBar tabToolBar = new JToolBar();

        btnDelInstance.setForeground(Color.RED);

        btnMinimize.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/collapse.png")))));
        btnClose.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/close.png")))));
        btnMinimize.setSize(8, 8);
        btnClose.setSize(8, 8);

        tabOptions.addTab(optionsTab.getTitle(), optionsTab.build());
        tabOptions.addTab(modsTab.getTitle(), modsTab.build());
        tabOptions.addTab(serversTab.getTitle(), serversTab.build());

        tabToolBar.setFloatable(false);
        tabToolBar.setBorder(null);
        tabToolBar.add(Box.createHorizontalStrut(60));
        tabToolBar.add(lblUsername);
        String avatarUsername = "MHF_Steve"; //TODO: use actual player name
        ImageIcon steveFace = new ImageIcon(ImageIO.read(new URL("https://mc-heads.net/avatar/" + avatarUsername + "/24")));
        JLabel lblSteve = new JLabel(steveFace);
        //tabToolBar.add(Box.createHorizontalStrut(5));
        tabToolBar.add(lblSteve);
        tabToolBar.add(Box.createHorizontalStrut(5));
        //tabToolBar.add(btnMinimize);
        //tabToolBar.add(btnClose);

        tabOptions.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, tabToolBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make sure it exits on close button pressed
        frame.setSize(400, 600);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // center the window
        frame.setTitle("OSML v" + OSML.VERSION);
        frame.setUndecorated(false);

        root.setLayout(null);

        // add & position UI components
        mainBackgroundPanel.setLayout(null);

        //tabOptions.setOpaque(false);
        //tabOptions.putClientProperty("JTabbedPane.tabType", "card");

        tabOptions.repaint();

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int optionsBackgroundHeight = 185;

        root.setBounds(0, 0, frameWidth, frameHeight);
        tabOptions.setBounds(0, 0, frameWidth, frameHeight - 100);
        btnAddInstance.setBounds(5, frameHeight - 85, 80, 25);
        btnDelInstance.setBounds(85, frameHeight - 85, 80, 25);
        cmbInstances.setBounds(5, frameHeight - 60, 160, 20);
        mainBackgroundPanel.setBounds(0, 0, frameWidth, frameHeight - optionsBackgroundHeight);
        //lblUsername.setBounds(frameWidth - (120 + 64), frameHeight - 20, 120, 20);
        txtUsername.setBounds(frameWidth - (160 + 90), frameHeight - 60, 150, 20);
        lblPassword.setBounds(frameWidth - (120 + 150), frameHeight - 80, 120, 20);
        txtPassword.setBounds(frameWidth - (160), frameHeight - 80, 150, 20);
        btnPlay.setBounds(frameWidth - (120 + 5), frameHeight - (60 + 25), 120, 45);
        optionsBackgroundPanel.setBounds(0, frameHeight - optionsBackgroundHeight, frameWidth, optionsBackgroundHeight);

        JSeparator separator = new JSeparator();
        separator.setBounds((frameWidth / 2) - (400 / 2), frameHeight - 100, 400, 5);

        frame.add(root);
        root.add(tabOptions);
        root.add(separator);
        root.add(cmbInstances);
        root.add(btnAddInstance);
        root.add(btnDelInstance);
        //root.add(txtUsername);
        root.add(btnPlay);
        root.add(optionsBackgroundPanel);

        String username = OSML.getInstance().getConfig().username;
        btnPlay.setEnabled(!(username == null || username.isBlank() || username.isEmpty()));

        txtUsername.addKeyListener(new KeyListener()
        {
            public void keyTyped(KeyEvent keyEvent)
            {
                String username = txtUsername.getText();
                if (username.length() == 16) keyEvent.consume();
            }
            public void keyPressed(KeyEvent keyEvent) {}
            public void keyReleased(KeyEvent keyEvent)
            {
                String username = txtUsername.getText();
                btnPlay.setEnabled(!(username == null || username.isBlank() || username.isEmpty()) && (txtUsername.getText().length() >= 3 && txtUsername.getText().length() <= 16));
            }
        });

        for (Instance inst : OSML.getInstance().getInstanceManager().getInstances())
        {
            cmbInstances.addItem(inst.getName() + " (" + inst.getGameVersion() + ")");

            if (inst.name.equals(OSML.getInstance().getConfig().selectedInstance))
            {
                txtUsername.setText(username);
                optionsTab.txtInstanceName.setText(inst.name);
                optionsTab.txtJavaExecutable.setText(inst.javaExecutable);
                optionsTab.txtJVMArguments.setText(inst.jvmArguments);
                optionsTab.chkProxy.setSelected(inst.enableProxy);
            }
        }

        Util.changeFont(root, fontManager.getNormalFont().deriveFont(Font.PLAIN, 14));
        btnPlay.setFont(fontManager.getBoldFont().deriveFont(Font.BOLD, 20));

        frame.setVisible(true); // show the UI
    }

    public JFrame getFrame()
    {
        return frame;
    }
}
