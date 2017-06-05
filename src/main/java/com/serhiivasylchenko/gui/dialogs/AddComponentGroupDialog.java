package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.GUIUpdater;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Validator;
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
public class AddComponentGroupDialog extends GridPane {
    @FXML
    private ChoiceBox<System> groupSystem;
    @FXML
    private ChoiceBox<ComponentGroup> groupParent;
    @FXML
    private TextField groupName;
    @FXML
    private TextArea groupDescription;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();

    public AddComponentGroupDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addComponentGroupDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }

    public void showDialog(System system, ComponentGroup parentGroup) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component group");
        dialog.setContentText(null);

        List<System> systems = workflowManager.getSystemList();

        if (!systems.isEmpty()) {
            groupSystem.setItems(FXCollections.observableArrayList(systems));
        }

        if (system != null) {
            groupSystem.getSelectionModel().select(system);
            loadParentGroups();
            if (parentGroup != null) {
                groupParent.getSelectionModel().select(parentGroup);
            } else {
                groupParent.getSelectionModel().selectFirst();
            }
        } else {
            groupSystem.getSelectionModel().selectFirst();
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

            if (groupName.getText().isEmpty()) {
                event.consume();
                alert.setContentText("Please set the name of the group");
                alert.showAndWait();
            } else if (groupSystem.getValue() == null) {
                event.consume();
                alert.setContentText("Please choose the system");
                alert.showAndWait();
            } else if (!Validator.validateGroupName(groupSystem.getValue(), groupName.getText())) {
                event.consume();
                alert.setContentText("Group name should be unique per system");
                alert.showAndWait();
            }
        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        //Platform.runLater(compName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                workflowManager.addComponentGroup(groupSystem.getValue(), groupParent.getValue(), groupName.getText(), groupDescription.getText());
                guiUpdater.updateComponentTree();
            }
            groupName.setText("");
            groupDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void loadParentGroups() {
        List<ComponentGroup> componentGroups = workflowManager.getComponentGroupList(groupSystem.getSelectionModel().getSelectedItem());
        if (componentGroups != null && !componentGroups.isEmpty()) {
            groupParent.setItems(FXCollections.observableArrayList(componentGroups));
        } else {
            groupParent.setItems(FXCollections.emptyObservableList());
            groupParent.getSelectionModel().selectFirst();
        }
    }
}
