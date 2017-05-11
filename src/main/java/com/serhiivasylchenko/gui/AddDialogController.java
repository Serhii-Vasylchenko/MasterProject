package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.Validator;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */

public class AddDialogController implements Initializable {

    private final Logger LOGGER = Logger.getLogger(AddDialogController.class);
    @FXML
    private TabPane addTabPane;

    @FXML
    private ChoiceBox<String> compSystem;
    @FXML
    private ChoiceBox<String> compGroup;
    @FXML
    private TextField compName;
    @FXML
    private TextArea compDescription;
    @FXML
    private TextField systemName;
    @FXML
    private TextArea systemDescription;
    @FXML
    private ChoiceBox<String> groupSystem;
    @FXML
    private TextField groupName;
    @FXML
    private TextArea groupDescription;
    @FXML
    private ChoiceBox<String> groupParent;

    private WorkflowManager workflowManager = new WorkflowManager();
    private Validator validator = new Validator();
    private Map<String, List<String>> groupNamesBySystem = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void showAddDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component/group/system");
        dialog.setContentText(null);

        List<String> systemNames;

        systemNames = workflowManager.getSystemList().stream()
                .map(System::getName)
                .collect(Collectors.toList());
        systemNames.forEach(systemName -> {
            List<String> groupNames = workflowManager.getGroupList(systemName).stream()
                    .map(ComponentGroup::getName)
                    .collect(Collectors.toList());
            groupNamesBySystem.put(systemName, groupNames);
        });

        if (!systemNames.isEmpty()) {
            compSystem.setItems(FXCollections.observableArrayList(systemNames));
            groupSystem.setItems(FXCollections.observableArrayList(systemNames));
        }

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Check if all necessary fields are set
            switch (addTabPane.getSelectionModel().getSelectedIndex()) {
                case 0:
                    if ((compName.getText().isEmpty() || compSystem.getValue().isEmpty())) {
                        event.consume();
                    } else if (!validator.validateComponentName(compSystem.getValue(), compName.getText())) {
                        event.consume();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Wrong data!");
                        alert.setHeaderText(null);
                        alert.setContentText("Component name should unique per system!");
                    }
                    break;
                case 1:

                    break;
                case 2:
                    break;
            }
            if (((addTabPane.getSelectionModel().getSelectedIndex() == 0) && (compName.getText().isEmpty() || compSystem.getValue().isEmpty())) ||
                    ((addTabPane.getSelectionModel().getSelectedIndex() == 1) && (systemName.getText().isEmpty())) ||
                    ((addTabPane.getSelectionModel().getSelectedIndex() == 2) && (groupName.getText().isEmpty() || groupSystem.getValue().isEmpty()))) {
                event.consume();
            }
        });

        dialog.getDialogPane().setContent(addTabPane);
        //dialog.getDialogPane().setPadding(new Insets(0, 0, 10, 0));

        // Request focus on the name field by default.
        //Platform.runLater(compName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                switch (addTabPane.getSelectionModel().getSelectedIndex()) {
                    case 0: // new component
                        workflowManager.addComponent(compSystem.getValue(), compGroup.getValue(), compName.getText(), compDescription.getText());
                        break;
                    case 1: // new system
                        workflowManager.addSystem(systemName.getText(), systemDescription.getText());
                        break;
                    case 2: // new component group
                        workflowManager.addComponentGroup(groupSystem.getValue(), groupParent.getValue(), groupName.getText(), groupDescription.getText());
                        break;
                }
                //mainController.updateComponentList();
            }
            return null;
        });

        dialog.showAndWait();

    }

    public void loadGroups() {
        List<String> groupNames = groupNamesBySystem.get(compSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            compGroup.setItems(FXCollections.observableArrayList(groupNames));
        }
    }

    public void loadParentGroups() {
        List<String> groupNames = groupNamesBySystem.get(groupSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            groupParent.setItems(FXCollections.observableArrayList(groupNames));
        }
    }
}
