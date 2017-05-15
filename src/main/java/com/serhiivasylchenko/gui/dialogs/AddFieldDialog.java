package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.Validator;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.FieldType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class AddFieldDialog extends GridPane {
    @FXML
    private TextField fieldName;
    @FXML
    private ChoiceBox<FieldType> fieldType;

    private Validator validator = new Validator();

    public AddFieldDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addFieldDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }

    public void showDialog(TechnicalEntity entity) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new component");
        dialog.setContentText(null);

        fieldType.setItems(FXCollections.observableList(Arrays.asList(FieldType.values())));
        fieldType.getSelectionModel().selectFirst();

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Validation on fields
        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong fields!");
            alert.setHeaderText(null);

            if (fieldName.getText().isEmpty()) {
                event.consume();
                alert.setContentText("Please set the name of the field");
                alert.showAndWait();
            } else if (fieldType.getValue() == null) {
                event.consume();
                alert.setContentText("Please choose the type");
                alert.showAndWait();
            } else if (!validator.validateFieldName(entity, fieldName.getText())) {
                event.consume();
                alert.setContentText("Field name should unique per entity");
                alert.showAndWait();
            }
        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        Platform.runLater(fieldName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == dialog.getDialogPane().getButtonTypes().get(0)) {
                Field field = new Field(fieldName.getText(), fieldType.getValue());
                entity.getParameterList().addField(field);
            }
            fieldName.setText("");
            return null;
        });

        dialog.showAndWait();
    }
}
