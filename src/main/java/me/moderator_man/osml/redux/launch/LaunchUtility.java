package me.moderator_man.osml.redux.launch;

import me.moderator_man.osml.redux.GameSetupThread;
import me.moderator_man.osml.redux.JavaSanityThread;
import me.moderator_man.osml.redux.Redux;
import me.moderator_man.osml.redux.process.GameProcessRunnable;
import me.moderator_man.osml.redux.process.direct.DirectGameProcess;
import me.moderator_man.osml.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class LaunchUtility
{
    private FXMessagePipe messagePipe;
    private GameProcessRunnable gameProcessRunnable;
    private FlagPipe launchResultPipe;

    public void setMessagePipe(FXMessagePipe messagePipe)
    {
        this.messagePipe = messagePipe;
    }

    public void setGameProcessRunnable(GameProcessRunnable gameProcessRunnable)
    {
        this.gameProcessRunnable = gameProcessRunnable;
    }

    public void setLaunchResultPipe(FlagPipe launchResultPipe)
    {
        this.launchResultPipe = launchResultPipe;
    }

    public void launch(String username)
    {
        new GameSetupThread((gameSetupCompleted) -> new JavaSanityThread((javaSanityEnsured) ->
        {
            if (!gameSetupCompleted)
            {
                setLaunchResult(false);
                return;
            }

            if (!javaSanityEnsured) setLaunchResult(false);
            if (!javaSanityEnsured) pipeMessage("Failed to install Java", "#b01919");
            else {
                try
                {
                    File javaFile = Util.getJavaExecutable();
                    File gameDirectory = new File(Util.getInstallDirectory());
                    File nativesDir = new File(gameDirectory, "bin/natives");
                    if (!javaFile.canExecute())
                    {
                        pipeMessage("Cannot continue: JDK binary is not executable", "#b01919");
                        return;
                    }
                    if (!javaFile.exists())
                    {
                        pipeMessage("Failed to locate Java installation", "#b01919");
                        return;
                    }
                    // build the process & start the game
                    ProcessBuilder pb = new ProcessBuilder(javaFile.getAbsolutePath(), "-Djava.library.path=" + nativesDir.getAbsolutePath(), Redux.getInstance().getConfig().jvmArguments, "-classpath", buildClasspath(new File(gameDirectory, "bin")), "net.minecraft.client.Minecraft", username);
                    pb.directory(gameDirectory);
                    pb.inheritIO();
                    pb.redirectErrorStream(true); // hack to get proc.waitFor() to return

                    DirectGameProcess gameProcess = new DirectGameProcess(pb.start(), (proc, msg) -> System.out.println(msg));
                    gameProcess.setExitRunnable(gameProcessRunnable != null ? gameProcessRunnable : (proc) -> System.exit(0));
                    pipeMessage("GAME IS RUNNING", "#41af19");
                    setLaunchResult(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    pipeMessage(ex.getMessage(), "#b01919");
                    setLaunchResult(false);
                }
            }
        }).start()).start();
    }

    private void pipeMessage(String msg, String color)
    {
        System.out.println("Message for UI: " + msg);
        if (messagePipe == null) return;
        messagePipe.pipe(msg, color);
    }

    private void setLaunchResult(boolean flag)
    {
        if (launchResultPipe == null) return;
        launchResultPipe.pipe(flag);
    }

    public String buildClasspath(File binDir)
    {
        ArrayList<String> excludes = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (File file : Objects.requireNonNull(binDir.listFiles()))
            if (!file.isDirectory() && file.getName().endsWith(".jar") && !excludes.contains(file.getName().toLowerCase()))
                sb.append(file.getAbsolutePath() + File.pathSeparator);
        String pre = sb.toString();
        return pre.substring(0, pre.length() - 1);
    }
}
