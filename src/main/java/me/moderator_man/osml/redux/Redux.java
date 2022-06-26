package me.moderator_man.osml.redux;

import com.github.mouse0w0.darculafx.DarculaFX;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.util.Logger;
import me.moderator_man.osml.util.QueryAPI;
import me.moderator_man.osml.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

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
        unpackThemes();
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
        File INSTALL_DIRECTORY = new File(Util.getInstallDirectory());
        File LOGS_DIRECTORY = new File(INSTALL_DIRECTORY, "logs/");
        File BIN_DIRECTORY = new File(INSTALL_DIRECTORY, "bin/");
        File NATIVES_DIRECTORY = new File(BIN_DIRECTORY, "natives/");
        File THEMES_DIRECTORY = new File(INSTALL_DIRECTORY, "themes/");

        Logger.log("Ensuring directory structure: " + INSTALL_DIRECTORY);
        if (!INSTALL_DIRECTORY.exists()) INSTALL_DIRECTORY.mkdir();
        if (!LOGS_DIRECTORY.exists()) LOGS_DIRECTORY.mkdir();
        if (!BIN_DIRECTORY.exists()) BIN_DIRECTORY.mkdir();
        if (!NATIVES_DIRECTORY.exists()) NATIVES_DIRECTORY.mkdir();
        if (!THEMES_DIRECTORY.exists()) THEMES_DIRECTORY.mkdir();
    }

    private void unpackThemes()
    {
        try
        {
            System.out.println("Unpacking themes");

            String darcula = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/css/darcula.css")), StandardCharsets.UTF_8);

            File themesDir = new File(Util.getInstallDirectory(), "themes/");
            File darculaFile = new File(themesDir, "darcula.css");
            FileUtils.writeStringToFile(darculaFile, darcula, StandardCharsets.UTF_8);
            if (!darculaFile.setReadable(true)) System.out.println("Failed to make Darcula theme readable");;
            if (!darculaFile.setWritable(true)) System.out.println("Failed to make Darcula theme writable");;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        alert(alertType, msg, title, false);
    }

    public void alert(Alert.AlertType alertType, String msg, String title, boolean wait)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle("System Alert");
        alert.setContentText(msg);
        alert.setResizable(false);
        alert.setHeaderText(title);
        if (wait) alert.showAndWait();
        else alert.show();
    }

    public boolean confirmAction(String msg, String title)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(title);
        alert.showAndWait();
        return alert.getResult().equals(ButtonType.YES);
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
            File theme = new File(Util.getInstallDirectory(), "themes/" + config.launcherTheme);
            if (!theme.exists()) System.out.println("The selected theme does not exist: " + theme.getName());
            scene.getStylesheets().add(theme.exists() ? "file://" + theme.getAbsolutePath() : getClass().getResource("/fxml/css/darcula.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("OSML v" + Main.VERSION);
            primaryStage.setResizable(false);
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

    public void reloadTheme()
    {
        primaryStage.getScene().getStylesheets().clear();
        File theme = new File(Util.getInstallDirectory(), "themes/" + config.launcherTheme);
        if (!theme.exists()) System.out.println("The selected theme does not exist: " + theme.getName());
        primaryStage.getScene().getStylesheets().add("file://" + theme.getAbsolutePath());
        primaryStage.setResizable(false);
    }

    public static Redux getInstance()
    {
        return instance;
    }

    private static Redux instance;
}
