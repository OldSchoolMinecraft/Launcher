package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.Util;

import java.util.HashMap;

public class LauncherConfig
{
    public boolean disableBitDepthFix;
    public boolean disableUpdates;
    public boolean showGameOutput;
    public boolean hideLauncher; // hide launcher while game is running
    public boolean exitAfterClose; // whether to exit after the game closes
    public boolean rememberCredentials;
    public String username;
    public String password;
    public String jvmArguments;
    public String javaExecutable;
    public String clientToken; // client token to be used for yggdrasil authentication
    public String uplink = "https://os-mc.net/launcher/uplink"; // URL to connect with remote update service
    public String launcherTheme;
    public HashMap<String, String> accountTokenMap = new HashMap<>(); // allow multiple access tokens (for account manager?)

    public LauncherConfig() {}

    public LauncherConfig(boolean ignored)
    {
        disableBitDepthFix = false;
        disableUpdates = false;
        showGameOutput = false;
        hideLauncher = true;
        exitAfterClose = true;
        rememberCredentials = true;
        jvmArguments = "-Xmx1024M";
        javaExecutable = Util.getJavaExecutable().getAbsolutePath();
        clientToken = Util.generateToken();
        launcherTheme = "darcula.css";
    }
}
