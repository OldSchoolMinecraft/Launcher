package me.moderator_man.osml.redux;

import com.formdev.flatlaf.IntelliJTheme;
import me.moderator_man.osml.Configuration;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.io.FormatReader;
import me.moderator_man.osml.io.FormatWriter;
import me.moderator_man.osml.ui.MainFrame;
import me.moderator_man.osml.ui.legacy.LauncherFrame;
import me.moderator_man.osml.util.*;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
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
            IntelliJTheme.setup(Bootstrap.class.getResourceAsStream("/themes/Spacegray.theme.json"));

            try
            {
                // Find user's home directory if running from an Linux OS
                if (OS.getOS() == OS.Linux) Util.getLinuxHomeDirectory();

                String install_directory = Util.getInstallDirectory();
                String logs_directory = install_directory + "logs/";
                String bin_directory = install_directory + "bin/";
                String natives_directory = bin_directory + "natives";
                Logger.log("Install directory: " + install_directory);
                File inst_dir = new File(install_directory);
                if (!inst_dir.exists())
                    inst_dir.mkdir();
                File logs_dir = new File(logs_directory);
                if (!logs_dir.exists())
                    logs_dir.mkdir();
                File bin_dir = new File(bin_directory);
                if (!bin_dir.exists())
                    bin_dir.mkdir();
                File natives_dir = new File(natives_directory);
                if (!natives_dir.exists())
                    natives_dir.mkdir();
            } catch (Exception ex) {
                Logger.log("Something went wrong while creating the install directory!");
                System.exit(1);
            }

            Logger.log("Config path: " + Main.getConfigPath());

            try
            {
                int latestVersion = Integer.parseInt(QueryAPI.get("https://os-mc.net/launcher/lv.php"));

                if (latestVersion > Main.VERSION)
                    Main.updateAvailable = true;

			/*if (new File(Util.getInstallDirectory(), "ft.eol").exists())
				firstTime = false;
			else
				firstTime = true;*/

                Main.firstTime = !new File(Util.getInstallDirectory(), "ft.lts").exists();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try
            {
                if (Main.firstTime)
                    new File(Main.getConfigPath()).delete();

                if (!new File(Main.getConfigPath()).exists())
                {
                    Main.config = new Configuration();
                    FormatWriter<Configuration> writer = new FormatWriter<>();
                    writer.write(Main.config, Main.getConfigPath());
                    Logger.log("Config was missing, so a new one was created with default values.");
                } else {
                    FormatReader<Configuration> reader = new FormatReader<>();
                    Main.config = reader.read(Main.getConfigPath());
                    Logger.log("Finished reading config, no problems.");

                    Main.config.legacyUI = false;

                    Logger.log("Config/disableUpdate: " + Main.config.disableUpdate);
                    Logger.log("Config/rememberPassword: " + Main.config.rememberPassword);
                    Logger.log("Config/legacyUI: " + Main.config.legacyUI);
                    Logger.log("Config/disableJavaCheck: " + Main.config.disableJavaCheck);
                }
            } catch (Exception ex) {
                new File(Main.getConfigPath()).delete();
                Main.config = new Configuration();
            }

            if (!Main.config.disableJavaCheck)
                if (!System.getProperty("java.version").contains("1.8"))
                    Main.problematicJava = true;

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        if (Main.config.legacyUI)
                        {
                            LauncherFrame window = new LauncherFrame();
                            window.setVisible(true);
                        } else {
                            MainFrame window = new MainFrame();
                            window.frmOldSchoolMinecraft.setVisible(true);
                            window.frmOldSchoolMinecraft.setIconImage(ImageIO.read(getClass().getResourceAsStream("/favicon.png")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this is a legacy function from old javafx UI
     */
    private static void bootstrap()
    {
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
                            "java",
                            "--module-path",
                            new File(runtimeDir, "lib/").getAbsolutePath(),
                            "--add-modules",
                            "javafx.controls,javafx.fxml,javafx.graphics",
                            "-cp",
                            currentJar.getAbsolutePath(),
                            "me.moderator_man.osml.Main"
                    };
            System.out.println("Executing: " + Arrays.toString(cmd));
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
