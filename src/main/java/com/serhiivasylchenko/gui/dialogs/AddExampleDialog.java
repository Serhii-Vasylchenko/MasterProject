package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Constants;
import com.serhiivasylchenko.utils.Parameters;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.datavec.api.records.writer.impl.csv.CSVRecordWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Serhii Vasylchenko
 */
public class AddExampleDialog extends VBox implements Initializable {

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

            inputFieldsGridPane.getChildren().forEach(node -> {
                if ((node instanceof TextField && ((TextField) node).getText().isEmpty()) ||
                        (node instanceof ChoiceBox && ((ChoiceBox<String>) node).getValue() == null)) {
                    alert.setContentText("All fields should be set!");
                    alert.show();
                    event.consume();
                }
            });

            targetFieldsGridPane.getChildren().forEach(node -> {
                if ((node instanceof TextField && ((TextField) node).getText().isEmpty()) ||
                        (node instanceof ChoiceBox && ((ChoiceBox<String>) node).getValue() == null)) {
                    alert.setContentText("All fields should be set!");
                    alert.show();
                    event.consume();
                }
            });
        });

        systemName.setText("New example for system '" + system.toString() + "'");

        Map<String, Field> fieldMap = new LinkedHashMap<>();

        // Add all system fields first
        List<Field> systemFields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", system.getParameterList()));
        systemFields.forEach(field -> fieldMap.put(system.toString() + "->" + field.getName(), field));

        // Then all component groups fields
        persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(group -> group.getParameterList().getFields()
                        .forEach(field -> fieldMap.put(group.toString() + "->" + field.getName(), field))
                );

        // Then all component fields
        persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(component -> component.getParameterList().getFields()
                        .forEach(field -> fieldMap.put(component.toString() + "->" + field.getName(), field))
                );

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
                String dir = AddExampleDialog.class.getClassLoader().getResource(Constants.datasetPath).getFile();
                dir = dir.replace("%20", " ");
                String useFor = useForChoiceBox.getSelectionModel().getSelectedItem();
                String fileName = system.toString().replace(" ", "") + "-" + useFor + ".csv";
                File datasetFile = new File(dir + fileName);

                if (!datasetFile.exists()) {
                    try {
                        datasetFile.createNewFile();
                    } catch (IOException e) {
                        LOGGER.error("File was not created!", e);
                    }
                }

                try {
                    CSVRecordWriter csvRecordWriter = new CSVRecordWriter(datasetFile, true);

                    List<Field> inputFields = new ArrayList<>();
                    List<Field> targetFields = new ArrayList<>();

                    inputFieldsGridPane.getChildren().forEach(node -> {

                    });

                } catch (FileNotFoundException e) {
                    LOGGER.error("Writing to file failed!");
                }


            }

            // clear dialog
            useForChoiceBox.getSelectionModel().select(-1);
            targetFieldsGridPane.getChildren().clear();
            inputFieldsGridPane.getChildren().clear();
            return null;
        });

        dialog.getDialogPane().setContent(this);

        dialog.showAndWait();
    }
}
