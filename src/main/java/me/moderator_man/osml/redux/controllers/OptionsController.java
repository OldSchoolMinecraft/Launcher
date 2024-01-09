package me.moderator_man.osml.redux.controllers;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import me.moderator_man.osml.redux.LauncherConfig;
import me.moderator_man.osml.redux.Redux;
import me.moderator_man.osml.util.Util;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class OptionsController extends ReduxController
{
    private LauncherConfig config;

    @FXML protected TabPane tabPane;
    @FXML protected Tab tBasic;
    @FXML protected Tab tAdvanced;

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
    @FXML protected Button btnUnlock;
    private File currentEditFile;
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
        txtEditor.textProperty().addListener(((observable, oldValue, newValue) -> thinkAboutChange()));

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
            thinkAboutChange();
        });

        btnSaveAdvanced.setOnAction(event ->
        {
            try
            {
                FileUtils.writeStringToFile(currentEditFile, txtEditor.getText(), StandardCharsets.UTF_8);
                Redux.getInstance().reloadTheme();
                Redux.getInstance().loadConfig();
            } catch (Exception ignored) {}
            thinkAboutChange();
        });

        btnUnlock.setOnAction(event ->
        {
            if (Redux.getInstance().confirmAction("The advanced configuration menu contains sensitive information, INCLUDING YOUR PASSWORD. Are you absolutely sure you want to continue?", "Security Warning"))
            {
                tAdvanced.setDisable(false);
                btnUnlock.setDisable(true);
            }
        });

        lstFiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (oldValue != null && lstFiles.getSelectionModel().getSelectedItem() != null && !Util.textToFileComparison(txtEditor.getText(), currentEditFile.getAbsolutePath()))
                if (!Redux.getInstance().confirmAction("You are about to discard your unsaved changes! Are you sure you want to continue?", "Unsaved Changes")) return;
            changeEditFile(newValue);
        });

        // add editable configuration files
        lstFiles.getItems().add("config.v2.json");
        for (String f : Objects.requireNonNull(new File(Util.getInstallDirectory(), "themes/").list()))
            lstFiles.getItems().add("themes/" + f);

        thinkAboutChange();
    }

    private void changeEditFile(String file)
    {
        try
        {
            currentEditFile = new File(Util.getInstallDirectory(), file);
            String fileContent = FileUtils.readFileToString(currentEditFile, StandardCharsets.UTF_8);
            txtEditor.setText(fileContent);
            System.out.println("Now editing file: " + file);
            thinkAboutChange();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        btnSaveAdvanced.setDisable(!didSomethingChange);
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
        if (lstFiles.getSelectionModel().getSelectedItem() == null) return false;
        boolean advFileChanged = !Util.textToFileComparison(txtEditor.getText(), currentEditFile.getAbsolutePath());
        System.out.println("Current edit file: " + currentEditFile.getName());
        System.out.println("Advanced file changed? " + (advFileChanged ? "Yes" : "No"));
        if (advFileChanged) return true;
        return false;
    }
}
