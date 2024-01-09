package me.moderator_man.osml.swing.tabs;

import me.moderator_man.osml.swing.FontManager;
import me.moderator_man.osml.swing.ImagePanel;
import me.moderator_man.osml.swing.MainUI;
import me.moderator_man.osml.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseListener;

public class ServersTab extends AbstractTab
{
    private JPanel root = new ImagePanel("stone.gif");
    private DefaultListModel<ServerListItem> listModel;
    private JList<ServerListItem> serverList;
    private FontManager fontManager;
    private MainUI mainUI;

    public ServersTab(MainUI mainUI)
    {
        super("Servers");
        this.mainUI = mainUI;
        fontManager = new FontManager("Karla");
    }

    @Override
    public JPanel build()
    {
        listModel = new DefaultListModel<>();

        listModel.addElement(new ServerListItem("Old School Minecraft", "Undeniably the #1 vanilla Beta 1.7.3 server", "os-mc.net", 0, 20));
        listModel.addElement(new ServerListItem("AlphaPlace", "Minecraft Alpha Server! Reasonings on https://alpha.place/", "alphaplace.net", 0, 20));

        serverList = new JList<>(listModel);
        serverList.setCellRenderer(new CustomListRenderer());
        serverList.setOpaque(false);
        serverList.setBackground(new Color(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(serverList);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0, 0, 0, 0));

        root.add(scrollPane);

        return root;
    }

    public class CustomListRenderer extends JPanel implements ListCellRenderer<ServerListItem>
    {
        private JLabel lblServerName;
        private JTextPane lblServerDescription;
        private JLabel lblServerIP;
        private JLabel lblPlayerCount;
        private JButton btnJoin;

        public CustomListRenderer()
        {
            setLayout(null);
            setBorder(new EmptyBorder(5, 5, 5, 5));

            lblServerName = new JLabel();
            lblServerDescription = new JTextPane();
            lblServerIP = new JLabel();
            lblPlayerCount = new JLabel();
            btnJoin = new JButton("Join");

            btnJoin.addActionListener((event) -> System.out.println("Join button pressed"));

            add(lblServerName);
            add(lblServerDescription);
            //add(lblServerIP);
            add(lblPlayerCount);
            add(btnJoin);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ServerListItem> list, ServerListItem value, int index, boolean isSelected, boolean cellHasFocus)
        {
            lblServerName.setText(value.getServerName());
            lblServerDescription.setText(value.getServerDescription());
            lblServerIP.setText(value.getServerIP());
            String playerCountString = "(" + value.getPlayerCount() + "/" + value.getMaxPlayerSlots() + ")";
            lblPlayerCount.setText(playerCountString);

            Util.changeFont(root, fontManager.getNormalFont().deriveFont(Font.PLAIN, 14));
            lblServerName.setFont(fontManager.getBoldFont().deriveFont(Font.BOLD, 14));

            FontMetrics rootFontMetrics = root.getFontMetrics(root.getFont());
            FontMetrics serverNameMetrics = lblServerName.getFontMetrics(lblServerName.getFont());

            lblServerName.setBounds(5, 15, serverNameMetrics.stringWidth(value.getServerName()), 20);
            lblServerDescription.setBounds(0, 30, 270, 40);
            lblServerIP.setBounds(5, 45, rootFontMetrics.stringWidth(value.getServerIP()), 20);
            lblPlayerCount.setBounds(serverNameMetrics.stringWidth(value.getServerName()) + 10, 15, rootFontMetrics.stringWidth(playerCountString), 20);
            btnJoin.setBounds(400 - 130, 15, 125, 30);

            lblServerDescription.setFont(fontManager.getItalicFont().deriveFont(Font.ITALIC, 11));
            lblServerDescription.setBackground(new Color(0, 0, 0, 0));
            lblServerDescription.setOpaque(false);

            setPreferredSize(new Dimension(400, 80));

            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }

        @Override
        public void addMouseListener(MouseListener listener)
        {
            super.addMouseListener(listener);
            btnJoin.addMouseListener(listener);
        }

        @Override
        public void removeMouseListener(MouseListener listener)
        {
            super.removeMouseListener(listener);
            btnJoin.removeMouseListener(listener);
        }
    }

    public static class ServerListItem
    {
        private String serverName;
        private String serverDescription;
        private String serverIP;
        private int playerCount;
        private int maxPlayerSlots;

        public ServerListItem(String serverName, String serverDescription, String serverIP, int playerCount, int maxPlayerSlots)
        {
            this.serverName = serverName;
            this.serverDescription = serverDescription;
            this.serverIP = serverIP;
            this.playerCount = playerCount;
            this.maxPlayerSlots = maxPlayerSlots;
        }

        public String getServerName()
        {
            return serverName;
        }

        public String getServerDescription()
        {
            return serverDescription;
        }

        public String getServerIP()
        {
            return serverIP;
        }

        public int getPlayerCount()
        {
            return playerCount;
        }

        public int getMaxPlayerSlots()
        {
            return maxPlayerSlots;
        }
    }
}
