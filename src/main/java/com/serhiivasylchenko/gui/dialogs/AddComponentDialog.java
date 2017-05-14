package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.Validator;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.ComponentsTreeUpdater;
import com.serhiivasylchenko.persistence.ComponentGroup;
import javafx.application.Platform;
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
public class AddComponentDialog extends GridPane {

    @FXML
    private ChoiceBox<String> compSystem;
    @FXML
    private ChoiceBox<String> compGroup;
    @FXML
    private TextField compName;
    @FXML
    private TextArea compDescription;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private Validator validator = new Validator();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    private Map<String, List<String>> groupNamesBySystem = new HashMap<>();

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

    public void showDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component");
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
            } else if (!validator.validateComponentName(compSystem.getValue(), compName.getText())) {
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
                componentsTreeUpdater.update();
            }
            compName.setText("");
            compDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void loadGroups() {
        List<String> groupNames = groupNamesBySystem.get(compSystem.getValue());
        if (groupNames != null && !groupNames.isEmpty()) {
            compGroup.setItems(FXCollections.observableArrayList(groupNames));
        } else {
            compGroup.setItems(FXCollections.emptyObservableList());
        }
    }
}
