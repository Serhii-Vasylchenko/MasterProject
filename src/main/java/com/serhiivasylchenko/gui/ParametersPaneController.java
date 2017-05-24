package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.persistence.*;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class ParametersPaneController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ParametersPaneController.class);

    @FXML
    private ImageView iconImage;
    @FXML
    private Label name;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane parameterGridPane;
    @FXML
    private VBox vBox;

    private DialogController dialogController = DialogController.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    private TechnicalEntity entity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guiUpdater.setParametersGridPane(parameterGridPane);
    }

    public void showEntityParameters(TechnicalEntity entity) {
        this.entity = entity;
        vBox.setDisable(false);

        if (entity instanceof System) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/system1_32.png")));
        } else if (entity instanceof Component) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/component1_32.png")));
        } else if (entity instanceof ComponentGroup) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/group1_32.png")));
        } else {
            LOGGER.error("Unknown TechnicalEntity! - " + entity.toString());
        }

        name.setText(entity.toString());
        updateParameters();
    }

    @FXML
    private void addNewField() {
        dialogController.addFieldToEntity(entity);
        updateParameters();
    }

    public void updateParameters() {
        // Clear previous grid first
        parameterGridPane.getChildren().clear();

        List<Field> fields = persistenceBean.find(Field.class, Field.FIELD_GET_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", entity.getParameterList()));

        // Create field labels and setters

        int i = 0;
        for (Field field : fields) {
            // Label for the field
            Label fieldName = new Label(field.getName());

            // Create buttons for edit and delete
            Button editFieldNameButton = new Button();
            Button deleteFieldButton = new Button();

            editFieldNameButton.setMaxSize(16, 16);
            deleteFieldButton.setMaxSize(16, 16);

            editFieldNameButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.EDIT, "14px"));
            deleteFieldButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "14px"));

            editFieldNameButton.setOnAction(event -> {
                dialogController.changeName(field);
            });
            deleteFieldButton.setOnAction(event -> {
                try {
                    persistenceBean.delete(field);
                    updateParameters();
                } catch (Exception e) {
                    Logger.getLogger(this.getClass()).error(null, e);
                }
            });

            // Value setter for this field
            switch (field.getFieldType()) {
                case INT_NUMBER:
                    TextField intField = new TextField(); // TODO: custom integer field
                    intField.setText(String.valueOf(field.getIntValue()));
                    intField.setTooltip(new Tooltip("integer value"));
                    intField.setOnKeyPressed(event -> {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            field.setIntValue(Integer.valueOf(intField.getText()));
                            persistenceBean.persist(field);
                        }
                    });
                    parameterGridPane.addRow(i, fieldName, intField, editFieldNameButton, deleteFieldButton);
                    GridPane.setHalignment(intField, HPos.CENTER);
                    break;
                case FLOAT_NUMBER:
                    TextField floatField = new TextField(); // TODO: custom float field
                    floatField.setText(String.valueOf(field.getFloatValue()));
                    floatField.setTooltip(new Tooltip("float value"));
                    floatField.setPromptText("float value");
                    floatField.setOnKeyPressed(event -> {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            field.setFloatValue(Float.valueOf(floatField.getText()));
                        }
                    });
                    parameterGridPane.addRow(i, fieldName, floatField, editFieldNameButton, deleteFieldButton);
                    GridPane.setHalignment(floatField, HPos.CENTER);
                    break;
                case CHOICE_BOX:
                    ChoiceBox<String> choiceBox = new ChoiceBox<>();
                    if (field.getChoiceStrings() != null) {
                        choiceBox.setItems(FXCollections.observableList(field.getChoiceStrings()));
                        choiceBox.getSelectionModel().select(field.getSelectedStringIndex());
                    }
                    choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        // -1 means no option selected
                        field.setSelectedStringIndex((Integer) newValue);
                    });
                    parameterGridPane.addRow(i, fieldName, choiceBox, editFieldNameButton, deleteFieldButton);
                    GridPane.setHalignment(choiceBox, HPos.CENTER);
                    break;
            }

            GridPane.setHalignment(fieldName, HPos.CENTER);
            GridPane.setHalignment(editFieldNameButton, HPos.CENTER);
            GridPane.setHalignment(deleteFieldButton, HPos.CENTER);

            i++;
        }
    }
}
