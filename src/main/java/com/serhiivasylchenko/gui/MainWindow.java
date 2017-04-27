package com.serhiivasylchenko.gui;

/**
 * @author Serhii Vasylchenko
 */

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends Application {

    @FXML
    ToggleGroup evaluateToggleGroup = new ToggleGroup();

    private WorkflowManager workflowManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/mainWindow.fxml"));
            primaryStage.setTitle("Master Project");
            primaryStage.setScene(new Scene(root, 1200, 900));
            primaryStage.show();

            workflowManager = new WorkflowManager();
            updateComponentList();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

    }

    public void runAnalysis(ActionEvent actionEvent) {

    }

    public void addComponent(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Component");
        dialog.setHeaderText("Choose the name for the component");
        dialog.setContentText("Name of the component:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(componentName -> {
            Component component = new Component(componentName);
            workflowManager.addComponent(component);
            updateComponentList();
        });
    }

    private void updateComponentList() {

    }
}
