package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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

    private void downloadLibraries() throws IOException
    {
        for (String lib : StaticData.libraries)
        {
            File targetFile = new File(Util.getBinPath(), lib);
            if (targetFile.exists()) continue;
            FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/libraries/" + lib), targetFile);
        }
    }

    private void downloadNatives() throws IOException
    {
        for (String lib : getNatives())
        {
            File targetFile = new File(Util.getNativesPath(), lib);
            if (targetFile.exists()) continue;
            FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/natives/" + OS.getOS().name().toLowerCase() + "/" + lib), targetFile);
        }
    }

    private void downloadClient() throws Exception
    {
        File clientFile = new File(Util.getBinPath(), "minecraft.jar");
        if (clientFile.exists())
        {
            try
            {
                String localHash = Util.getMD5Checksum(clientFile.getAbsolutePath());
                if (QueryAPI.checkHash("https://os-mc.net/launcher/versioncheck.txt", localHash))
                    return;
            } catch (FileNotFoundException ignored) {
                return; // the API is down, do not attempt download
            }
        }

        if (!Redux.getInstance().getConfig().disableUpdates)
            FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/libraries/minecraft.jar"), clientFile);
        else System.out.println("New client update available, ignored by user's config");
    }

    private String[] getNatives()
    {
        return switch (OS.getOS())
        {
            default -> StaticData.natives_windows;
            case Mac -> StaticData.natives_mac;
            case Linux -> StaticData.natives_linux;
        };
    }
}
