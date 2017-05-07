package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.System;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */

public class DialogController implements Initializable {

    public TabPane addTabPane;
    public ChoiceBox<String> compSystem;
    public TextField compName;
    public TextArea compDescription;
    public TextField systemName;
    public TextArea systemDescription;
    public ChoiceBox<String> groupSystem;
    public TextField groupName;
    public TextArea groupDescription;
    public ChoiceBox<String> groupParent;

    private MainController mainController;

    private final Logger LOGGER = Logger.getLogger(DialogController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert addTabPane != null;
    }

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    void showAddDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addDialog.fxml"));
            TabPane content = fxmlLoader.load();

            Dialog<List<String>> dialog = new Dialog<>();
            dialog.setTitle("Add new component/group/system");
            dialog.setContentText(null);

            List<String> systemNames = mainController.getWorkflowManager().getSystemList().stream()
                    .map(System::getName)
                    .collect(Collectors.toList());
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
                if (((addTabPane.getSelectionModel().getSelectedIndex() == 0) && (compName.getText().isEmpty() || compSystem.getValue().isEmpty())) ||
                        ((addTabPane.getSelectionModel().getSelectedIndex() == 1) && (systemName.getText().isEmpty())) ||
                        ((addTabPane.getSelectionModel().getSelectedIndex() == 2) && (groupName.getText().isEmpty() || groupSystem.getValue().isEmpty()))) {
                    event.consume();
                }
            });

            dialog.getDialogPane().setContent(content);
            //dialog.getDialogPane().setPadding(new Insets(0, 0, 10, 0));

            // Request focus on the name field by default.
            //Platform.runLater(compName::requestFocus);

            // Send the result to workflowManager when the add button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                    switch (addTabPane.getSelectionModel().getSelectedIndex()) {
                        case 0: // new component
                            mainController.getWorkflowManager().addComponent(compSystem.getValue(), compName.getText(), compDescription.getText());
                            break;
                        case 1: // new system
                            mainController.getWorkflowManager().addSystem(systemName.getText(), systemDescription.getText());
                            break;
                        case 2: // new component group
                            mainController.getWorkflowManager().addComponentGroup(groupSystem.getValue(), groupParent.getValue(), groupName.getText(), groupDescription.getText());
                            break;
                    }
                    mainController.updateComponentList();
                }
                return null;
            });

            dialog.showAndWait();

        } catch (IOException e) {
            LOGGER.error(null, e);
        }
    }
}
