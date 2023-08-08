package me.moderator_man.osml.swing.tabs;

import javax.swing.*;

public abstract class AbstractTab
{
    private final String title;

    public AbstractTab(String title)
    {
        this.title = title;
    }

    public abstract JPanel build();

    public final String getTitle()
    {
        return title;
    }
}
