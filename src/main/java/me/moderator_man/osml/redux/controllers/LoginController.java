package me.moderator_man.osml.redux.controllers;

import com.github.mouse0w0.darculafx.DarculaFX;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.moderator_man.osml.Main;
import me.moderator_man.osml.redux.LauncherConfig;
import me.moderator_man.osml.redux.Redux;
import me.moderator_man.osml.redux.auth.AuthUtility;
import me.moderator_man.osml.redux.auth.YggdrasilAuthRequest;
import me.moderator_man.osml.redux.auth.YggdrasilAuthResponse;
import me.moderator_man.osml.redux.launch.LaunchUtility;

public class LoginController extends ReduxController
{
    @FXML protected Button btnLogin;
    @FXML protected Button btnOptions;
    @FXML protected Label lblStatus;
    @FXML protected TextField txtUsername;
    @FXML protected PasswordField txtPassword;

    private void setLock(boolean flag)
    {
        btnLogin.setDisable(flag);
        btnOptions.setDisable(flag);
        txtUsername.setDisable(flag);
        txtPassword.setDisable(flag);
    }

    private void setStatusMessage(String msg, String color)
    {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + color);
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

        btnLogin.setOnAction((event) ->
        {
            setLock(true);
            lblStatus.setText("Launching the game. Please wait...");
            lblStatus.setStyle("-fx-text-fill: #41af19"); // green

            AuthUtility auth = new AuthUtility();
            YggdrasilAuthResponse authRes = auth.makeRequest(new YggdrasilAuthRequest(txtUsername.getText(), txtPassword.getText()));
            if (authRes == null || authRes.accessToken == null)
            {
                lblStatus.setText("Invalid username or password (or connection failed)");
                lblStatus.setStyle("-fx-text-fill: " + "#b01919"); // red
                setLock(false);
                return;
            }

            LaunchUtility launch = new LaunchUtility();
            launch.setMessagePipe(this::setStatusMessage);

            launch.setGameProcessRunnable((proc) ->
            {
                if (config.exitAfterClose) System.exit(0);
                else {
                    Redux.getInstance().getPrimaryStage().show();
                    setLock(false);
                }
            });

            launch.setLaunchResultPipe((flag) ->
            {
                if (flag && config.hideLauncher)
                    Platform.runLater(() -> Redux.getInstance().getPrimaryStage().hide());
            });

            config.username = txtUsername.getText();
            config.password = txtPassword.getText();
            Redux.getInstance().saveConfig();

            launch.launch(txtUsername.getText());
        });

        btnOptions.setOnAction((event) ->
        {
            try
            {
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Options.fxml"));
                Scene scene = new Scene(loader.load());
                newStage.setScene(scene);
                newStage.setTitle("OSML v" + Main.VERSION);
                newStage.setResizable(false);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initOwner(Redux.getInstance().getPrimaryStage());
                DarculaFX.applyDarculaStyle(scene);
                newStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
