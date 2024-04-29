package me.moderator_man.osml.redux;

import me.moderator_man.osml.util.OS;
import me.moderator_man.osml.util.Util;
import me.moderator_man.osml.util.ZipUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
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
            if (!currentJar.getName().endsWith(".jar")) // tf am i supposed to do without a jar?
                throw new RuntimeException("Runtime code-source URI location does not point to a JAR file.");
            System.out.println("Current JAR: " + currentJar.getAbsolutePath());
            tmp.mkdirs();
            runtimeDir.mkdirs();
            if (runtimeDir.exists() && runtimeDir.isDirectory() && Objects.requireNonNull(runtimeDir.listFiles()).length == 0)
            {
                System.out.println("Downloading JavaFX runtime files...");
                String os = OS.getOS().name().toLowerCase();
                FileUtils.copyURLToFile(new URL("https://os-mc.net/launcher/dl/fx-runtime-" + os + ".zip"), runtimeFile);
                ZipUtil.extractAllTo(runtimeFile.getAbsolutePath(), runtimeDir.getAbsolutePath());
                System.out.println("Finished downloading JavaFX runtime files.");
            }
            String[] cmd = new String[]
            {
                findJava(),
                "--module-path",
                new File(runtimeDir, "lib/").getAbsolutePath(),
                "--add-modules",
                "javafx.controls,javafx.fxml,javafx.graphics",
                "-cp",
                currentJar.getAbsolutePath(),
                "me.moderator_man.osml.Main"
            };
            boolean debug = args.length >= 1 && args[0].equalsIgnoreCase("debug");
            ProcessBuilder pb = new ProcessBuilder(cmd);
            System.out.println("Executing: " + Arrays.toString(cmd));
            if (debug) pb.inheritIO();
            Process proc = pb.start();
            FileUtils.deleteDirectory(tmp);
            if (debug)
            {
                int exit = proc.waitFor();
                while (proc.isAlive()) {}
                System.out.println("Exit code: " + exit);
            }
            System.out.println("OSML bootstrap finished, exiting...");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            notify(ex.getMessage(), "Bootstrapping failed");
        }
    }

    private static String findJava()
    {
        return (OS.getOS() == OS.Windows) ? findJavaWindows() : findJavaUnix();
    }

    private static String findJavaWindows()
    {
        String[] pathDirs = System.getenv("PATH").split(";");
        for (String pathDir : pathDirs)
        {
            File javaFile = new File(pathDir, "java.exe");
            if (javaFile.exists()) return javaFile.getAbsolutePath();
        }
        return null;
    }

    private static String findJavaUnix()
    {
        try
        {
            Process process = Runtime.getRuntime().exec("whereis -b java");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String latestJavaPath = null;

            while ((line = reader.readLine()) != null)
            {
                String[] locations = line.split(":");
                if (locations.length > 1)
                {
                    // Use the first location returned by 'whereis'
                    latestJavaPath = locations[1].trim();
                    // Follow symlinks to get the actual location
                    latestJavaPath = getRealPath(latestJavaPath);
                    break;
                }
            }

            process.waitFor();
            if (latestJavaPath != null) return latestJavaPath;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return null;
    }

    private static String getRealPath(String path)
    {
        try
        {
            Process process = Runtime.getRuntime().exec("realpath " + path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String realPath = reader.readLine();
            process.waitFor();
            return realPath;
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return path; // Return the original path if 'realpath' command fails
    }

    private static void notify(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
    }
}
