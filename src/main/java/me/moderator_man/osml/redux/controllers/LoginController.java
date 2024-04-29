package me.moderator_man.osml.redux.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.redux.LauncherConfig;
import me.moderator_man.osml.redux.Redux;
import me.moderator_man.osml.redux.TextAreaInputDialog;
import me.moderator_man.osml.redux.auth.AuthUtility;
import me.moderator_man.osml.redux.auth.YggdrasilAuthRequest;
import me.moderator_man.osml.redux.auth.YggdrasilAuthResponse;
import me.moderator_man.osml.redux.auth.YggdrasilProfile;
import me.moderator_man.osml.redux.launch.LaunchUtility;
import me.moderator_man.osml.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class LoginController extends ReduxController
{
    @FXML protected Button btnLogin;
    @FXML protected Button btnOptions;
    @FXML protected Label lblStatus;
    @FXML protected TextField txtUsername;
    @FXML protected PasswordField txtPassword;
    @FXML protected Hyperlink linkWebsite, linkDiscord, linkYouTube;

    private void setLock(boolean flag)
    {
        btnLogin.setDisable(flag);
        btnOptions.setDisable(flag);
        txtUsername.setDisable(flag);
        txtPassword.setDisable(flag);
    }

    private void setStatusMessage(String msg, String color)
    {
        Platform.runLater(() ->
        {
            lblStatus.setText(msg);
            lblStatus.setStyle("-fx-text-fill: " + color);
        });
    }

    @FXML protected void initialize()
    {
        LauncherConfig config = Redux.getInstance().getConfig();

        if (config.rememberCredentials && (config.username != null && config.password != null))
        {
            txtUsername.setText(config.username);
            txtPassword.setText(config.password);
            System.out.println("OSML auto-filled credentials from the config");
        }

        setupButtonAnimation(btnLogin, 1.1);
        setupButtonAnimation(btnOptions, 1.1);

        linkWebsite.setOnAction(event -> Util.openNetpage("https://os-mc.net"));
        linkDiscord.setOnAction(event -> Util.openNetpage("https://os-mc.net/discord"));
        linkYouTube.setOnAction(event -> Util.openNetpage("https://youtube.com/OldSchoolMinecraft"));

        btnLogin.setOnAction((event) ->
        {
            setLock(true);
            lblStatus.setText("Launching the game. Please wait...");
            lblStatus.setStyle("-fx-text-fill: #41af19"); // green

            AuthUtility auth = new AuthUtility();
            YggdrasilAuthResponse authRes = null;

            try
            {
                authRes = auth.makeRequest(new YggdrasilAuthRequest(txtUsername.getText(), txtPassword.getText()));
                if (authRes == null || authRes.accessToken == null)
                {
                    lblStatus.setText("Invalid username or password (or API is down)");
                    lblStatus.setStyle("-fx-text-fill: " + "#b01919"); // red
                    setLock(false);
                    return;
                }
            } catch (IOException ex) {
                // Create the new dialog
                TextAreaInputDialog dialog = new TextAreaInputDialog("Username...");
                dialog.setHeaderText("There appears to be an issue with the API.\nPlease provide an offline username to play with.");
                dialog.setGraphic(null);

                // Show the dialog and capture the result.
                Optional<String> result = dialog.showAndWait();

                // If the "Okay" button was clicked, the result will contain our String in the get() method
                if (result.isPresent())
                {
                    authRes = new YggdrasilAuthResponse();
                    authRes.selectedProfile = new YggdrasilProfile();
                    authRes.selectedProfile.name = result.get();
                    authRes.selectedProfile.id = "N/A";
                    authRes.accessToken = "N/A";
                    authRes.clientToken = UUID.randomUUID().toString();
                } else {
                    lblStatus.setText("Invalid username or password (or connection failed)");
                    lblStatus.setStyle("-fx-text-fill: " + "#b01919"); // red
                    setLock(false);
                    return;
                }
            }

            LaunchUtility launch = new LaunchUtility();
            launch.setMessagePipe(this::setStatusMessage);

            launch.setGameProcessRunnable((proc) ->
            {
                if (config.exitAfterClose) System.exit(0);
                else {
                    Platform.runLater(() -> Redux.getInstance().getPrimaryStage().show());
                    setLock(false);
                }
            });

            launch.setLaunchResultPipe((flag) ->
            {
                if (flag && config.hideLauncher)
                    Platform.runLater(() -> System.exit(0));
            });

            config.username = txtUsername.getText();
            config.password = txtPassword.getText();
            Redux.getInstance().saveConfig();

            launch.launch(authRes.selectedProfile.name);
        });

        btnOptions.setOnAction((event) ->
        {
            try
            {
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Options.fxml"));
                Scene scene = new Scene(loader.load());
                File theme = new File(Util.getInstallDirectory(), "themes/" + config.launcherTheme);
                if (!theme.exists()) System.out.println("The selected theme does not exist: " + theme.getName());
                scene.getStylesheets().add("file://" + theme.getAbsolutePath());
                newStage.setScene(scene);
                newStage.setTitle("OSML v" + Main.VERSION);
                newStage.setResizable(false);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initOwner(Redux.getInstance().getPrimaryStage());
                newStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
