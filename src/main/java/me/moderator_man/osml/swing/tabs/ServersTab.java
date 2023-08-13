package me.moderator_man.osml.swing.tabs;

import me.moderator_man.osml.swing.FontManager;
import me.moderator_man.osml.swing.ImagePanel;
import me.moderator_man.osml.swing.MainUI;
import me.moderator_man.osml.util.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ServersTab extends AbstractTab
{
    private JPanel root = new ImagePanel("stone.gif");
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
        root.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setLayout(null);
        scrollPane.setBounds(0, 0, 400, 600 - 130);
        scrollPane.setPreferredSize(new Dimension(400, 600 - 130));
        scrollPane.setMaximumSize(new Dimension(400, 600 - 130));
        scrollPane.setOpaque(false);
        //scrollPane.setBackground(new Color(0, 0, 0, 0));
        //scrollPane.setForeground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null);

        ArrayList<ServerListItem> servers = new ArrayList<>(); //TODO: get from johny's server list

        for (int i = 0; i < 25; i++) servers.add(new ServerListItem("Old School Minecraft", "Test", "os-mc.net", 0, 100));

        JPanel lastItem = null;
        for (ServerListItem server : servers)
        {
            JPanel serverListItemPanel = buildServerListItemPanel(server);

            // Add a custom MouseWheelListener to let events pass through
            serverListItemPanel.addMouseWheelListener(e ->
            {
                // Get the parent scroll pane and dispatch the event to it
                JViewport viewport = scrollPane.getViewport();
                if (viewport != null) {
                    e.setSource(viewport);
                    viewport.dispatchEvent(SwingUtilities.convertMouseEvent(serverListItemPanel, e, viewport));
                }
            });

            serverListItemPanel.setLayout(null);
            serverListItemPanel.setBounds(0, (lastItem == null) ? 5 : (lastItem.getY() + 90), 400, 85);
            serverListItemPanel.setOpaque(false);
            //serverListItemPanel.setBackground(new Color(0, 0, 0, 0));
            serverListItemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            scrollPane.add(serverListItemPanel);
            lastItem = serverListItemPanel;
        }

        root.add(scrollPane);

        return root;
    }

    private JPanel buildServerListItemPanel(ServerListItem server)
    {
        JLabel lblServerName = new JLabel(server.getServerName() + " (" + server.getPlayerCount() + "/" + server.getMaxPlayerSlots() + ")");
        lblServerName.setBounds(5, 5, 400, 20);

        JLabel lblServerDescription = new JLabel(server.getServerDescription());
        lblServerDescription.setBounds(5, 30, 400, 20);

        JButton btnJoin = new JButton("Join");
        btnJoin.setBounds(400 - 120, 30, 90, 30);

        JPanel serverListItemPanel = new JPanel();
        serverListItemPanel.setLayout(null);
        serverListItemPanel.setSize(400, 85);

        serverListItemPanel.add(lblServerName);
        serverListItemPanel.add(lblServerDescription);
        serverListItemPanel.add(btnJoin);

        btnJoin.setFont(fontManager.getBoldFont().deriveFont(Font.BOLD, 14));
        lblServerDescription.setFont(fontManager.getItalicFont().deriveFont(Font.ITALIC, 11));
        lblServerName.setFont(fontManager.getBoldFont().deriveFont(Font.BOLD, 14));

        return serverListItemPanel;
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
