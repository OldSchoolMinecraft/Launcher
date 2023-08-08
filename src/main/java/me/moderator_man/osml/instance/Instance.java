package me.moderator_man.osml.instance;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import me.moderator_man.osml.mods.Mod;
import me.moderator_man.osml.util.InstanceException;
import me.moderator_man.osml.util.Util;

import java.util.ArrayList;

public class Instance
{
    private transient final Gson gson = new Gson();

    public final String name;
    public String gameVersion;
    public final ArrayList<Mod> mods;

    public String javaExecutable = Util.getJavaExecutable().getAbsolutePath();
    public String jvmArguments;
    public String launcherTheme = "Spacegray.theme.json";
    public boolean keepOpen = false;
    public boolean showGameOutput = false;
    public boolean enableProxy = true;

    public Instance(String name, String gameVersion, ArrayList<Mod> mods) throws InstanceException
    {
        this.name = name;
        this.gameVersion = gameVersion;
        this.mods = mods;
    }

    public String getName()
    {
        return name;
    }

    public String getGameVersion()
    {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion)
    {
        this.gameVersion = gameVersion;
    }

    public ArrayList<Mod> getMods()
    {
        return mods;
    }

    public boolean isDefault()
    {
        return name.equals("(Default)");
    }
}
