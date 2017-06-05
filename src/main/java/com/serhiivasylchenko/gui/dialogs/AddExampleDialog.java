package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.learners.LearningUtils;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.Constants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class AddExampleDialog extends VBox implements Initializable, IDialog {

    private static final Logger LOGGER = Logger.getLogger(AddExampleDialog.class);

    @FXML
    private ChoiceBox<String> useForChoiceBox;
    @FXML
    private Label systemName;
    @FXML
    private GridPane inputFieldsGridPane;
    @FXML
    private GridPane targetFieldsGridPane;

    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public AddExampleDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addExampleDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showDialog(TechnicalEntity entity) {
        if (entity instanceof System) {
            this.showDialog((System) entity);
        }
    }

    public void showDialog(System system) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add new example");
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

            if (useForChoiceBox.getValue() == null) {
                alert.setContentText("Select usage of this example!");
                alert.show();
                event.consume();
            }
            if (!validateFields(inputFieldsGridPane) || !validateFields(targetFieldsGridPane)) {
                alert.setContentText("All fields should be set!");
                alert.show();
                event.consume();
            }
        });

        systemName.setText("New example for system '" + system.toString() + "'");

        Map<String, Field> fieldMap = LearningUtils.fieldWithNameMap(system);

        int input = 0;
        int target = 0;
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {

            Field field = entry.getValue();

            // Label for the field
            Label fieldName = new Label(entry.getKey());
            fieldName.setTooltip(new Tooltip(field.getDescription()));

            // Value setter for this field
            Control fieldValue = null;
            switch (field.getFieldType()) {
                case INT_NUMBER:
                    fieldValue = new TextField();
                    ((TextField) fieldValue).setPromptText("integer value");
                    fieldValue.setTooltip(new Tooltip("integer value"));

                    break;

                case FLOAT_NUMBER:
                    fieldValue = new TextField();
                    ((TextField) fieldValue).setPromptText("float value");
                    fieldValue.setTooltip(new Tooltip("float value"));

                    break;

                case CHOICE_BOX:
                    fieldValue = new ChoiceBox<String>();
                    if (field.getChoiceStrings() != null) {
                        ((ChoiceBox<String>) fieldValue).setItems(FXCollections.observableList(field.getChoiceStrings()));
                    }

                    break;
            }

            if (field.isTarget()) {
                targetFieldsGridPane.addRow(target, fieldName, fieldValue);
                target++;
            } else {
                inputFieldsGridPane.addRow(input, fieldName, fieldValue);
                input++;
            }
        }

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {

                // Create directory is not exists
                File datasetFolder = new File(Constants.datasetPath);
                if (!datasetFolder.exists()) {
                    if (datasetFolder.mkdir()) {
                        LOGGER.info("Dataset folder was created");
                    } else {
                        LOGGER.info("Dataset folder already exists");
                    }
                }

                // Create file for dataset, e.g. "system1-Training.csv"
                String useFor = useForChoiceBox.getSelectionModel().getSelectedItem();

                String fileName = null;
                if ("Training".equals(useFor)) {
                    fileName = system.getSystemTrainingConf().getPathToTrainingSet();
                } else {
                    fileName = system.getSystemTrainingConf().getPathToTestSet();
                }

                File datasetFile = new File(datasetFolder.getPath() + "/" + fileName);

                if (!datasetFile.exists()) {
                    try {
                        if (datasetFile.createNewFile()) {
                            LOGGER.info("Dataset file '" + fileName + "' was created");
                        } else {
                            LOGGER.info("Dataset file '" + fileName + "' already exists");
                        }
                    } catch (IOException e) {
                        LOGGER.error("Creation of file '" + fileName + "' failed!", e);
                    }
                }

                if (datasetFile.exists()) {
                    List<String> fieldValues = new ArrayList<>();
                    fieldValues.addAll(LearningUtils.collectFieldValues(inputFieldsGridPane));
                    fieldValues.addAll(LearningUtils.collectFieldValues(targetFieldsGridPane));

                    LearningUtils.writeCSV(datasetFile, fieldValues);
                }
            }


            // clear dialog
            useForChoiceBox.getSelectionModel().select(-1);
            targetFieldsGridPane.getChildren().clear();
            inputFieldsGridPane.getChildren().clear();
            return null;
        });

        dialog.getDialogPane().

                setContent(this);

        dialog.showAndWait();
    }

    private boolean validateFields(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if ((node instanceof TextField && ((TextField) node).getText().isEmpty()) ||
                    (node instanceof ChoiceBox && ((ChoiceBox<String>) node).getValue() == null)) {
                return false;
            }
        }
        return true;
    }
}
