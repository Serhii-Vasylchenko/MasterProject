package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.GUIUpdater;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Validator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class AddComponentDialog extends GridPane {
    @FXML
    private ChoiceBox<System> compSystem;
    @FXML
    private ChoiceBox<ComponentGroup> compGroup;
    @FXML
    private TextField compName;
    @FXML
    private TextArea compDescription;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();

    public AddComponentDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addComponentDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }

    public void showDialog(System system, ComponentGroup componentGroup) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component");
        dialog.setContentText(null);

        List<System> systems = workflowManager.getSystemList();

        if (!systems.isEmpty()) {
            compSystem.setItems(FXCollections.observableArrayList(systems));
        }

        if (system != null) {
            compSystem.getSelectionModel().select(system);
            loadGroups();
            if (componentGroup != null) {
                compGroup.getSelectionModel().select(componentGroup);
            } else {
                compGroup.getSelectionModel().selectFirst();
            }
        } else {
            compSystem.getSelectionModel().selectFirst();
        }

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Validation on fields
        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong fields!");
            alert.setHeaderText(null);

            if (compName.getText().isEmpty()) {
                event.consume();
                alert.setContentText("Please set the name of the component");
                alert.showAndWait();
            } else if (compSystem.getValue() == null) {
                event.consume();
                alert.setContentText("Please choose the system");
                alert.showAndWait();
            } else if (!Validator.validateComponentName(compSystem.getValue(), compName.getText())) {
                event.consume();
                alert.setContentText("Component name should unique per system");
                alert.showAndWait();
            }

        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        Platform.runLater(compName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                workflowManager.addComponent(compSystem.getValue(), compGroup.getValue(), compName.getText(), compDescription.getText());
                guiUpdater.updateComponentTree();
            }
            compName.setText("");
            compDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void loadGroups() {
        List<ComponentGroup> componentGroups = workflowManager.getComponentGroupList(compSystem.getSelectionModel().getSelectedItem());
        if (componentGroups != null && !componentGroups.isEmpty()) {
            compGroup.setItems(FXCollections.observableArrayList(componentGroups));
        } else {
            compGroup.setItems(FXCollections.emptyObservableList());
            compGroup.getSelectionModel().selectFirst();
        }
    }
}
