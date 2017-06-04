package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.persistence.learning.AbstractLearner;
import com.serhiivasylchenko.persistence.learning.LearnerParameter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class TrainingPaneController implements Initializable {

    @FXML
    private ChoiceBox<AbstractLearner> learnerChoiceBox;
    @FXML
    private GridPane learnerParameters;

    private DialogController dialogController = DialogController.getInstance();
    private SharedData sharedData = SharedData.getInstanse();

    private System system;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (system != null) {
            List<AbstractLearner> abstractLearners = system.getLearners();

            learnerChoiceBox.setItems(FXCollections.observableList(abstractLearners));
            learnerChoiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                AbstractLearner learner = learnerChoiceBox.getSelectionModel().getSelectedItem();
                List<LearnerParameter> parameters = learner.getConfiguration().getConfigurableFields();


            });
        }
    }

    public void lookupSystem(TechnicalEntity selectedEntity) {
        if (selectedEntity instanceof System) {
            this.system = (System) selectedEntity;
        } else if (selectedEntity instanceof Component) {
            this.system = ((Component) selectedEntity).getSystem();
        } else if (selectedEntity instanceof ComponentGroup) {
            this.system = ((ComponentGroup) selectedEntity).getSystem();
        }
    }

    @FXML
    private void addNewExample() {
        if (system != null) {
            dialogController.addExample(system);
        }
    }

    @FXML
    private void setExternalFiles() {

    }

    @FXML
    private void showTrainingSet() {

    }

    @FXML
    private void showTestSet() {

    }

    @FXML
    private void showHelpTraining() {

    }

    @FXML
    private void addLearner() {

    }

    @FXML
    private void showHelpLearnerConfiguration() {

    }
}