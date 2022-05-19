package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.Util;
import me.moderator_man.osml.util.ZipUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.Objects;

public class Bootstrap
{
    public static void main(String[] args)
    {
        System.out.println("OSML bootstrap started");

        try
        {
            new File(Util.getInstallDirectory()).mkdirs();
            File tmp = new File(Util.getInstallDirectory(), "temp/");
            File runtimeFile = new File(tmp, "fx-runtime.zip");
            File runtimeDir = new File(Util.getInstallDirectory(), "java/fx-runtime/");
            File currentJar = new File(Redux.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            tmp.mkdirs();
            runtimeDir.mkdirs();
            if (runtimeDir.exists() && runtimeDir.isDirectory() && Objects.requireNonNull(runtimeDir.listFiles()).length == 0)
            {
                String os = OS.getOS().name().toLowerCase();
                FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/dl/fx-runtime-" + os + ".zip"), runtimeFile);
                ZipUtil.extractAllTo(runtimeFile.getAbsolutePath(), runtimeDir.getAbsolutePath());
                System.out.println("Current JAR: " + currentJar.getAbsolutePath());
                if (!currentJar.getName().endsWith(".jar")) // tf am i supposed to do without a jar?
                    return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("java ")
                    .append("--module-path ")
                    .append(new File(runtimeDir, "lib/").getAbsolutePath())
                    .append(" --add-modules ")
                    .append("javafx.controls,javafx.fxml,javafx.graphics")
                    .append(" -cp ")
                    .append(currentJar.getAbsolutePath())
                    .append(" me.moderator_man.osml.Main");
            String cmd = sb.toString().trim();
            System.out.println("Executing: " + cmd);
            Runtime.getRuntime().exec(cmd);
            FileUtils.deleteDirectory(tmp);
            System.out.println("OSML bootstrap finished, exiting...");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            notify(ex.getMessage(), "Bootstrapping failed");
        }
    }

    private static void notify(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
    }
}
