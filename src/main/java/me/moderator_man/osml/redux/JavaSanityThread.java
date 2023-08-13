package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class JavaSanityThread extends Thread
{
    private FXMessagePipe messagePipe;
    private FlagPipe pipe;

    public JavaSanityThread(FlagPipe pipe)
    {
        this.pipe = pipe;
    }

    public void setMessagePipe(FXMessagePipe messagePipe)
    {
        this.messagePipe = messagePipe;
    }

    private void pipeMessage(String msg, String color)
    {
        System.out.println("Message for UI: " + msg);
        if (messagePipe == null) return;
        messagePipe.pipe(msg, color);
    }

    public void run()
    {
        try
        {
            Logger.log("Ensuring Java is installed");
            if (!Util.getJavaDir().exists())
            {
                pipeMessage("Installing Java...", "#41af19");
                File tmp = new File(Util.getJavaDir().getParentFile(), "temp/");
                tmp.mkdir();
                File tmpZip = new File(tmp, Util.getJavaName() + ".zip");
                FileUtils.copyURLToFile(new URL("https://cdn.gethydra.org/jdk/" + Util.getJavaName() + ".zip"), tmpZip);
                ZipUtil.extractAllTo(tmpZip.getAbsolutePath(), Util.getJavaDir().getAbsolutePath());
                FileUtils.deleteDirectory(tmp);
                // attempt to make make executable on linux
                if (OS.getOS() == OS.Linux)
                {
                    Process p = Runtime.getRuntime().exec("chmod u+x -R " + Util.getJavaDir().getAbsolutePath());
                    int exit = p.waitFor();
                    if (exit != 0) pipeMessage("Failed to change file permissions for Java installation. You may have to change them manually.", "#b01919");
                    else pipeMessage("Successfully made Java binary files executable", "#41af19");
                }
            }
            pipe.pipe(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            pipe.pipe(false);
        }
    }
}
