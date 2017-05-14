package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.Validator;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.ComponentsTreeUpdater;
import com.serhiivasylchenko.persistence.ComponentGroup;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class AddComponentGroupDialog extends GridPane {
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

    public void showDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component group");
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
        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        //Platform.runLater(compName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                workflowManager.addComponentGroup(groupSystem.getValue(), groupParent.getValue(), groupName.getText(), groupDescription.getText());
                componentsTreeUpdater.update();
            }
            groupName.setText("");
            groupDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void loadParentGroups() {
        List<String> groupNames = groupNamesBySystem.get(groupSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            groupParent.setItems(FXCollections.observableArrayList(groupNames));
        } else {
            groupParent.setItems(FXCollections.emptyObservableList());
        }
    }
}
