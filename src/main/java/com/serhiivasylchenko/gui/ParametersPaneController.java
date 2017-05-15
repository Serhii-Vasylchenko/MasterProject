package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * @author Serhii Vasylchenko
 */
public class ParametersPaneController {
    @FXML
    private ImageView iconImage;
    @FXML
    private Label name;
    @FXML
    private GridPane parameterGridPane;

    private DialogController dialogController = DialogController.getInstance();

    private TechnicalEntity entity;

    public ParametersPaneController() {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/parametersPane.fxml"));
//        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
//
//        try {
//            fxmlLoader.load();
//        } catch (IOException e) {
//            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
//            throw new RuntimeException(e);
//        }
    }

    public void showSystemParameters(System system) {
        entity = system;
        iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/system1_32.png")));
        name.setText(system.toString());
    }

    public void showComponentParameters(Component component) {
        entity = component;
        iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/component1_32.png")));
        name.setText(component.toString());
    }

    public void showComponentCroupParameters(ComponentGroup componentGroup) {
        entity = componentGroup;
        iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/group1_32.png")));
        name.setText(componentGroup.toString());
    }

    @FXML
    private void addNewField() {
        dialogController.addFieldToEntity(entity);
    }
}
