package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class GameSetupThread extends Thread
{
    private FlagPipe flagPipe;

    public GameSetupThread(FlagPipe flagPipe)
    {
        this.flagPipe = flagPipe;
    }

    public void run()
    {
        try
        {
            System.out.println("Downloading game files...");
            downloadLibraries();
            downloadNatives();
            downloadClient();
            flagPipe.pipe(true);
            System.out.println("Finished downloading games files!");
        } catch (Exception ex) {
            ex.printStackTrace();
            flagPipe.pipe(false);
        }
    }

    private void downloadLibraries()
    {
        for (String lib : StaticData.libraries)
        {
            try
            {
                File targetFile = new File(Util.getBinPath(), lib);
                if (targetFile.exists()) continue;
                FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/libraries/" + lib), targetFile);
            } catch (Exception ex) {
                flagPipe.pipe(false);
                ex.printStackTrace();
            }
        }
    }

    private void downloadNatives()
    {
        for (String lib : getNatives())
        {
            try
            {
                File targetFile = new File(Util.getNativesPath(), lib);
                if (targetFile.exists()) continue;
                FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/natives/" + OS.getOS().name().toLowerCase() + "/" + lib), targetFile);
            } catch (Exception ex) {
                flagPipe.pipe(false);
                ex.printStackTrace();
            }
        }
    }

    private void downloadClient()
    {
        try
        {
            File clientFile = new File(Util.getBinPath(), "minecraft.jar");
            if (clientFile.exists())
            {
                String latestHash = QueryAPI.get("https://os-mc.net/launcher/versioncheck.php");
                String currentHash = Util.getMD5Checksum(clientFile.getAbsolutePath());
                if (currentHash.equals(latestHash))
                    return;
            }
            if (!Redux.getInstance().getConfig().disableUpdates)
                FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/libraries/minecraft.jar"), clientFile);
            else System.out.println("New client update available, ignored by user's config");
        } catch (Exception ex) {
            flagPipe.pipe(false);
            ex.printStackTrace();
        }
    }

    private String[] getNatives()
    {
        switch (OS.getOS())
        {
            default:
            case Windows:
                return StaticData.natives_windows;
            case Mac:
                return StaticData.natives_mac;
            case Linux:
                return StaticData.natives_linux;
        }
    }
}
