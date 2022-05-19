package me.moderator_man.osml.redux.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import me.moderator_man.osml.redux.LauncherConfig;
import me.moderator_man.osml.redux.Redux;

public class OptionsController extends ReduxController
{
    private LauncherConfig config;

    /* BEGIN -- BASIC OPTIONS */
    @FXML protected CheckBox chkDepthFix;
    @FXML protected CheckBox chkUpdates;
    @FXML protected CheckBox chkGameOutput;
    @FXML protected CheckBox chkHideLauncher;
    @FXML protected CheckBox chkExitAfterClose;
    @FXML protected CheckBox chkRemCreds;
    @FXML protected TextField txtJVM;
    @FXML protected TextField txtJavaExecutable;
    @FXML protected Button btnSaveBasic;
    /* END -- BASIC OPTIONS */

    /* BEGIN -- ADVANCED OPTIONS */
    @FXML protected TextArea txtEditor;
    @FXML protected ListView<String> lstFiles;
    @FXML protected Button btnSaveAdvanced;
    /* END -- ADVANCED OPTIONS */

    @FXML
    protected void initialize()
    {
        config = Redux.getInstance().getConfig();

        chkDepthFix.setSelected(config.disableBitDepthFix);
        chkUpdates.setSelected(config.disableUpdates);
        chkGameOutput.setSelected(config.showGameOutput);
        chkHideLauncher.setSelected(config.hideLauncher);
        chkExitAfterClose.setSelected(config.exitAfterClose);
        chkRemCreds.setSelected(config.rememberCredentials);
        txtJVM.setText(config.jvmArguments);
        txtJavaExecutable.setText(config.javaExecutable);

        chkDepthFix.setOnAction(this::handleAction);
        chkUpdates.setOnAction(this::handleAction);
        chkGameOutput.setOnAction(this::handleAction);
        chkHideLauncher.setOnAction(this::handleAction);
        chkExitAfterClose.setOnAction(this::handleAction);
        chkRemCreds.setOnAction(this::handleAction);
        txtJVM.textProperty().addListener(((observable, oldValue, newValue) -> thinkAboutChange()));
        txtJavaExecutable.textProperty().addListener(((observable, oldValue, newValue) -> thinkAboutChange()));

        btnSaveBasic.setOnAction(event ->
        {
            config.disableBitDepthFix = chkDepthFix.isSelected();
            config.disableUpdates = chkUpdates.isSelected();
            config.showGameOutput = chkGameOutput.isSelected();
            config.hideLauncher = chkHideLauncher.isSelected();
            config.exitAfterClose = chkExitAfterClose.isSelected();
            config.rememberCredentials = chkRemCreds.isSelected();
            config.jvmArguments = txtJVM.getText();
            config.javaExecutable = txtJavaExecutable.getText();

            Redux.getInstance().saveConfig();
            btnSaveBasic.setDisable(true);
        });

        btnSaveAdvanced.setOnAction(event ->
        {
            try
            {
                Redux.getInstance().overwriteConfig(new Gson().fromJson(txtEditor.getText(), LauncherConfig.class));
            } catch (Exception ignored) {}
            btnSaveAdvanced.setDisable(true);
        });
    }

    private void handleAction(ActionEvent event)
    {
        thinkAboutChange();
    }

    private void thinkAboutChange()
    {
        boolean didSomethingChange = didSomethingChange();
        System.out.println("Did user change options? " + (didSomethingChange ? "Yes" : "No"));
        btnSaveBasic.setDisable(!didSomethingChange);
    }

    private boolean didSomethingChange()
    {
        if (chkDepthFix.isSelected() != config.disableBitDepthFix) return true;
        if (chkUpdates.isSelected() != config.disableUpdates) return true;
        if (chkGameOutput.isSelected() != config.showGameOutput) return true;
        if (chkHideLauncher.isSelected() != config.hideLauncher) return true;
        if (chkExitAfterClose.isSelected() != config.exitAfterClose) return true;
        if (chkRemCreds.isSelected() != config.rememberCredentials) return true;
        if (!txtJVM.getText().equals(config.jvmArguments)) return true;
        if (!txtJavaExecutable.getText().equals(config.javaExecutable)) return true;
        return false;
    }
}
