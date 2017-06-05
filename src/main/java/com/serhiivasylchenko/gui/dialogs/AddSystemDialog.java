package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.GUIUpdater;
import com.serhiivasylchenko.utils.Validator;
import javafx.application.Platform;
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
public class AddSystemDialog extends GridPane {
    @FXML
    private TextField systemName;
    @FXML
    private TextArea systemDescription;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();

    public AddSystemDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addSystemDialog.fxml"));
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
        dialog.setTitle("Add new system");
        dialog.setContentText(null);

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Validation on fields
        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong fields!");
            alert.setHeaderText(null);

            if (systemName.getText().isEmpty()) {
                event.consume();
                alert.setContentText("Please set the name of the system");
                alert.showAndWait();
            } else if (!Validator.validateSystemName(systemName.getText())) {
                event.consume();
                alert.setContentText("System name should be unique");
                alert.showAndWait();
            }

        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        Platform.runLater(systemName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                workflowManager.addSystem(systemName.getText(), systemDescription.getText());
                guiUpdater.updateComponentTree();
            }
            systemName.setText("");
            systemDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }
}
