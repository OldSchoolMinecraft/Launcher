package me.moderator_man.osml.swing.tabs;

import me.moderator_man.osml.swing.ImagePanel;

import javax.swing.*;

public class ModsTab extends AbstractTab
{
    private JPanel root = new ImagePanel("stone.gif");

    public ModsTab()
    {
        super("Mods");
    }

    @Override
    public JPanel build()
    {
        return root;
    }
}
