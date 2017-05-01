package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.System;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class MainController implements Initializable {
    public TreeView<String> componentsTreeView;

    private WorkflowManager workflowManager = new WorkflowManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateComponentList();
        //componentsTreeView.setCellFactory();
    }

    public void runAnalysis() {

    }

    public void addSystem() {
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

    public void addComponent() {
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
        List<System> systems = workflowManager.getSystemList();

        TreeItem<String> rootNode = new TreeItem<>();
        rootNode.setExpanded(true);

        systems.forEach(system -> {
            TreeItem<String> systemName = new TreeItem<>(system.getName());
            systemName.setExpanded(true);
            workflowManager.getComponentList(system.getName()).forEach(component -> {
                TreeItem<String> compName = new TreeItem<>(component.getName());
                systemName.getChildren().add(compName);
            });
            rootNode.getChildren().add(systemName);
        });

        componentsTreeView.setRoot(rootNode);
    }
}
