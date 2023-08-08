package me.moderator_man.osml.swing;

import com.formdev.flatlaf.IntelliJTheme;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.moderator_man.osml.instance.InstanceManager;
import me.moderator_man.osml.redux.Bootstrap;
import me.moderator_man.osml.util.Util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.util.Objects;

public class OSML
{
    private static final Gson gson = new Gson();

    public static int VERSION = 21; // NEW REV#
    private static OSML instance;

    private ReduxConfig config;
    private InstanceManager instanceManager;
    private MainUI mainUI;

    public static void main(String[] args)
    {

        try
        {
            new OSML().init();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void init() throws Exception
    {
        instance = this;

        reloadConfig();

        instanceManager = new InstanceManager();
        instanceManager.init();


        IntelliJTheme.setup(Bootstrap.class.getResourceAsStream("/themes/Spacegray.theme.json"));

        mainUI = new MainUI();
        mainUI.init();
    }

    public void reloadConfig()
    {
        File config = new File(Util.getInstallDirectory(), "config.v3.json");
        if (config.exists())
        {
            try (JsonReader reader = new JsonReader(new FileReader(config)))
            {
                this.config = gson.fromJson(reader, ReduxConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.config = new ReduxConfig();
            try (JsonWriter writer = new JsonWriter(new FileWriter(config)))
            {
                gson.toJson(this.config, ReduxConfig.class, writer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public InstanceManager getInstanceManager()
    {
        return instanceManager;
    }

    public ReduxConfig getConfig()
    {
        return config;
    }

    public static OSML getInstance()
    {
        return instance;
    }
}
