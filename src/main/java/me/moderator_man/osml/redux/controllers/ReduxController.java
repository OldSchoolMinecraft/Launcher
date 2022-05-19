package me.moderator_man.osml.redux.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ReduxController
{
    @FXML public Pane background;

    protected void setupButtonAnimation(Button button)
    {
        setupButtonAnimation(button,1.1D);
    }

    protected void setupButtonAnimation(Button button, double toScale)
    {
        button.setOnMouseEntered((e) -> scaleTransition(button, toScale));
        button.setOnMouseExited((e) -> scaleTransition(button, 1.0D));
    }

    protected void scaleTransition(Node node, double toScale)
    {
        scaleTransition(node, toScale, 200);
    }

    protected void scaleTransition(Node node, double toScale, double durationMillis)
    {
        Duration duration = Duration.millis(durationMillis);
        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(toScale);
        scaleTransition.setToY(toScale);
        scaleTransition.play();
    }

    protected void fadeTransition(Node node, double toOpacity)
    {
        fadeTransition(node, toOpacity, 200);
    }

    protected void fadeTransition(Node node, double toOpacity, double durationMillis)
    {
        Duration duration = Duration.millis(durationMillis);
        FadeTransition transition = new FadeTransition(duration, node);
        transition.setToValue(toOpacity);
        transition.play();
    }
}
