package com.serhiivasylchenko.gui;

/**
 * @author Serhii Vasylchenko
 */

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.System;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MainWindow extends Application {

    @FXML
    ToggleGroup evaluateToggleGroup = new ToggleGroup();

    private WorkflowManager workflowManager = new WorkflowManager();

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

            updateComponentList();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

    }

    public void runAnalysis(ActionEvent actionEvent) {

    }

    public void addSystem(ActionEvent actionEvent) {
        // Create the custom dialog.
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component");
        dialog.setContentText(null);

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Fields for the name and description
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("optional");

        // Add the content to the grid
        grid.add(new Label("Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description"), 0, 1);
        grid.add(descriptionField, 1, 1);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (nameField.getText().trim().isEmpty()) {
                event.consume();
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default.
        Platform.runLater(nameField::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                workflowManager.addSystem(nameField.getText(), descriptionField.getText());
                updateComponentList();
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void addComponent(ActionEvent actionEvent) {
        // Create the custom dialog.
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component");
        dialog.setContentText(null);

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Choice box for selecting the system
        ChoiceBox<String> systemChoiceBox = new ChoiceBox<>();
        List<String> systemNames = workflowManager.getSystemList().stream()
                .map(System::getName)
                .collect(Collectors.toList());
        systemChoiceBox.setItems(FXCollections.observableArrayList(systemNames));
        systemChoiceBox.setTooltip(new Tooltip("Select the system"));

        // Fields for the name and description
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("optional");

        // Add the content to the grid
        grid.add(new Label("System"), 0, 0);
        grid.add(systemChoiceBox, 1, 0);
        grid.add(new Label("Name"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Description"), 0, 2);
        grid.add(descriptionField, 1, 2);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (systemChoiceBox.getValue().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
                event.consume();
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default.
        Platform.runLater(nameField::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                workflowManager.addComponent(systemChoiceBox.getValue(), nameField.getText(), descriptionField.getText());
                updateComponentList();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateComponentList() {

    }
}
