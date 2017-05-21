package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.*;
import com.serhiivasylchenko.persistence.System;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        List<Field> fields = entity.getParameterList().getFields();

        int i = 0;
        for (Field field : fields) {
            Label fieldName = new Label(field.getName());
            Node fieldValue = null;
            switch (field.getFieldType()) {
                case INT_NUMBER:
                    fieldValue = new TextField(); // TODO: custom integer field
                    ((TextField) fieldValue).setPromptText("integer value");
                    break;
                case FLOAT_NUMBER:
                    fieldValue = new TextField(); // TODO: custom float field
                    ((TextField) fieldValue).setPromptText("float value");
                    break;
                case CHOICE_BOX:
                    fieldValue = new ChoiceBox<String>();
                    ((ChoiceBox<String>) fieldValue).setItems(FXCollections.observableList(field.getChoiceStrings()));
                    break;
            }
            parameterGridPane.addRow(i, fieldName, fieldValue);
            GridPane.setHalignment(fieldName, HPos.CENTER);
            GridPane.setHalignment(fieldValue, HPos.CENTER);

            i++;
        }
//        scrollPane.resize(400, 60 * i);
//        scrollPane.setHvalue(30 * i);
    }
}
