package me.moderator_man.osml.redux;

import com.github.mouse0w0.darculafx.DarculaFX;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.QueryAPI;
import me.moderator_man.osml.util.Util;
import java.io.*;

public class Redux
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Stage primaryStage;
    private LauncherConfig config;

    public void init(Stage primaryStage)
    {
        instance = this;
        this.primaryStage = primaryStage;

        patchLetsEncryptCerts();
        ensureDirectoryStructure();
        handleConfig();
        launchFXApp();
        handleUpdates();
    }

    private void patchLetsEncryptCerts()
    {
        try
        {
            // ensure latest LetsEncrypt certificates are installed & trusted
            Logger.log("Applying patch for LetsEncrypt certs issue");
            final String[] certs = { "/isrgrootx1.der", "/lets-encrypt-r3.der" };
            Util.setTrustStore(certs, "changeit");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ensureDirectoryStructure()
    {
        String INSTALL_DIRECTORY = Util.getInstallDirectory();
        String LOGS_DIRECTORY = INSTALL_DIRECTORY + "logs/";
        String BIN_DIRECTORY = INSTALL_DIRECTORY + "bin/";
        String NATIVES_DIRECTORY = BIN_DIRECTORY + "natives";

        Logger.log("Ensuring directory structure: " + INSTALL_DIRECTORY);
        File inst_dir = new File(INSTALL_DIRECTORY);
        if (!inst_dir.exists()) inst_dir.mkdir();
        File logs_dir = new File(LOGS_DIRECTORY);
        if (!logs_dir.exists()) logs_dir.mkdir();
        File bin_dir = new File(BIN_DIRECTORY);
        if (!bin_dir.exists()) bin_dir.mkdir();
        File natives_dir = new File(NATIVES_DIRECTORY);
        if (!natives_dir.exists()) natives_dir.mkdir();
    }

    private void handleConfig()
    {
        try
        {
            Logger.log("Ensuring configuration sanity");
            File configFile = new File(Util.getInstallDirectory(), "config.v2.json");
            if (!configFile.exists())
            {
                Logger.log("config.v2.json was missing, created one with defaults");
                config = new LauncherConfig(true);
                saveConfig();
                return;
            }

            loadConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadConfig()
    {
        try
        {
            Logger.log("Loading config");
            File configFile = new File(Util.getInstallDirectory(), "config.v2.json");
            try (FileReader reader = new FileReader(configFile))
            {
                config = gson.fromJson(reader, LauncherConfig.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveConfig()
    {
        try
        {
            Logger.log("Saving config");
            File configFile = new File(Util.getInstallDirectory(), "config.v2.json");
            try (FileWriter writer = new FileWriter(configFile))
            {
                gson.toJson(config, LauncherConfig.class, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void alert(String msg, String title)
    {
        alert(Alert.AlertType.INFORMATION, msg, title);
    }

    public void alert(Alert.AlertType alertType, String msg, String title)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle("System Alert");
        alert.setContentText(msg);
        alert.setResizable(false);
        alert.setHeaderText(title);
        alert.show();
    }

    private void handleUpdates()
    {
        try
        {
            int latestVersion = Integer.parseInt(QueryAPI.get("https://os-mc.net/launcher/lv.php"));

            if (latestVersion > Main.VERSION)
                alert(Alert.AlertType.INFORMATION, "There is a new version available: " +
                                                                  latestVersion + "\n" +
                                                                  "You are currently running on version: " + Main.VERSION + "\n\n" +
                                                                  "Download @ https://os-mc.net", "Update Notification");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void launchFXApp()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("OSML v" + Main.VERSION);
            primaryStage.setResizable(false);
            DarculaFX.applyDarculaStyle(scene);
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Failed to launch FX application");
            System.exit(1);
        }
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public LauncherConfig getConfig()
    {
        return config;
    }

    public void overwriteConfig(LauncherConfig newConfig)
    {
        config = newConfig;
        saveConfig();
    }

    public static Redux getInstance()
    {
        return instance;
    }

    private static Redux instance;
}
