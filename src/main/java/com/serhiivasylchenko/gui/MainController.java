package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Persistable;
import com.serhiivasylchenko.persistence.System;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public ToggleGroup evaluationToggleGroup;

    private final Image systemIcon = new Image(getClass().getResourceAsStream("/icons/system1_16.png"));
    private final Image componentIcon = new Image(getClass().getResourceAsStream("/icons/component1_16.png"));
    private final Image groupIcon = new Image(getClass().getResourceAsStream("/icons/group1_16.png"));

    private WorkflowManager workflowManager = new WorkflowManager();

    private DialogController dialogController = new DialogController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateComponentList();
        dialogController.setMainController(this);
        //componentsTreeView.setCellFactory();
    }

    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    public void runAnalysis() {

    }

    public void addEntity() {
        dialogController.showAddDialog();
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

    public void updateComponentList() {
        List<System> systems = workflowManager.getSystemList();

        TreeItem<String> rootNode = new TreeItem<>();

        addChildrenToTheNode(rootNode, systems);

        componentsTreeView.setRoot(rootNode);
    }

    private void addChildrenToTheNode(TreeItem<String> node, List<? extends Persistable> children) {
        // Set the node to be expanded by default
        node.setExpanded(true);

        children.forEach(child -> {
            TreeItem<String> childName = null;

            // Assign different icons depending on the class
            if (child instanceof System) {
                childName = new TreeItem<>(((System) child).getName(), new ImageView(componentIcon));
            } else if (child instanceof ComponentGroup) {
                ComponentGroup group = (ComponentGroup) child;
                childName = new TreeItem<>(group.getName(), new ImageView(groupIcon));
                // Other component groups may be nested inside, so we need to check for this
                addChildrenToTheNode(childName, group.getChildren());
            } else if (child instanceof Component) {
                childName = new TreeItem<>(((Component) child).getName(), new ImageView(componentIcon));
            }

            if (childName != null) {
                node.getChildren().add(childName);
            }
        });
    }
}
