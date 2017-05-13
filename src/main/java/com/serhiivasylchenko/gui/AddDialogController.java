package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.Validator;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.ComponentGroup;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;
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

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private Validator validator = new Validator();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    private Map<String, List<String>> groupNamesBySystem = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void showAddDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component/group/system");
        dialog.setContentText(null);

        List<String> systemNames = new ArrayList<>();

        workflowManager.getSystemList().forEach(system -> {
            systemNames.add(system.getName());
            List<String> groupNames = system.getComponentGroups().stream()
                    .map(ComponentGroup::getName)
                    .collect(Collectors.toList());
            groupNamesBySystem.put(system.getName(), groupNames);
        });

        if (!systemNames.isEmpty()) {
            compSystem.setItems(FXCollections.observableArrayList(systemNames));
            groupSystem.setItems(FXCollections.observableArrayList(systemNames));
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
            switch (addTabPane.getSelectionModel().getSelectedIndex()) {
                case 0: // new component
                    if (compName.getText().isEmpty()) {
                        event.consume();
                        alert.setContentText("Please set the name of the component");
                        alert.showAndWait();
                    } else if (compSystem.getValue() == null) {
                        event.consume();
                        alert.setContentText("Please choose the system");
                        alert.showAndWait();
                    } else if (!validator.validateComponentName(compSystem.getValue(), compName.getText())) {
                        event.consume();
                        alert.setContentText("Component name should unique per system");
                        alert.showAndWait();
                    }
                    break;
                case 1: // new system
                    if (systemName.getText().isEmpty()) {
                        event.consume();
                        alert.setContentText("Please set the name of the system");
                        alert.showAndWait();
                    } else if (!validator.validateSystemName(systemName.getText())) {
                        event.consume();
                        alert.setContentText("System name should be unique");
                        alert.showAndWait();
                    }
                    break;
                case 2: // new component group
                    if (groupName.getText().isEmpty()) {
                        event.consume();
                        alert.setContentText("Please set the name of the group");
                        alert.showAndWait();
                    } else if (groupSystem.getValue() == null) {
                        event.consume();
                        alert.setContentText("Please choose the system");
                        alert.showAndWait();
                    } else if (!validator.validateGroupName(groupSystem.getValue(), groupName.getText())) {
                        event.consume();
                        alert.setContentText("Group name should be unique per system");
                        alert.showAndWait();
                    }
                    break;
            }
        });

        dialog.getDialogPane().setContent(addTabPane);

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
                componentsTreeUpdater.update();
            }
            clear();
            return null;
        });

        dialog.showAndWait();

    }

    public void loadGroups() {
        List<String> groupNames = groupNamesBySystem.get(compSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            compGroup.setItems(FXCollections.observableArrayList(groupNames));
        } else {
            compGroup.setItems(FXCollections.emptyObservableList());
        }
    }

    public void loadParentGroups() {
        List<String> groupNames = groupNamesBySystem.get(groupSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            groupParent.setItems(FXCollections.observableArrayList(groupNames));
        } else {
            groupParent.setItems(FXCollections.emptyObservableList());
        }
    }

    private void clear() {
        compName.setText("");
        compDescription.setText("");
        systemName.setText("");
        systemDescription.setText("");
        groupName.setText("");
        groupDescription.setText("");
    }
}
