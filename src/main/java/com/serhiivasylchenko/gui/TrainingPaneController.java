package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.learners.LearningUtils;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.persistence.learning.Learner;
import com.serhiivasylchenko.persistence.learning.LearnerParameter;
import com.serhiivasylchenko.utils.Parameters;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class TrainingPaneController implements Initializable {

    @FXML
    private ChoiceBox<Learner> learnerChoiceBox;
    @FXML
    private GridPane learnerParameters;
    @FXML
    private Button saveConfigurationButton;

    private PersistenceBean persistenceBean = PersistenceBean.getInstance();
    private DialogController dialogController = DialogController.getInstance();
    private SharedData sharedData = SharedData.getInstanse();

    private System system;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        learnerChoiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if ((int) newValue != -1) {
                // I have no idea why this fucking line is not working how it's supposed to. Shit.
                // Learner learner = learnerChoiceBox.getSelectionModel().getSelectedItem();
                Learner learner = learnerChoiceBox.getItems().get((int) newValue);
                List<LearnerParameter> parameters = learner.getConfiguration().getConfigurableFields();

                // clear first
                this.learnerParameters.getChildren().clear();

                for (int i = 0; i < parameters.size(); i++) {
                    Label fieldName = new Label(parameters.get(i).getName());
                    fieldName.setTooltip(new Tooltip(parameters.get(i).getDescription()));

                    Control fieldValue = null;
                    switch (parameters.get(i).getFieldType()) {
                        case INT_NUMBER:
                            fieldValue = new TextField(String.valueOf(parameters.get(i).getIntValue()));
                            fieldValue.setTooltip(new Tooltip("Integer value"));
                            break;
                        case FLOAT_NUMBER:
                            fieldValue = new TextField(String.valueOf(parameters.get(i).getFloatValue()));
                            fieldValue.setTooltip(new Tooltip("Float value"));
                            break;
                        case CHOICE_BOX:
                            fieldValue = new ChoiceBox<>(FXCollections.observableList(parameters.get(i).getChoiceStrings()));
                            fieldValue.setTooltip(new Tooltip("Choose option"));
                            break;
                    }

                    learnerParameters.addRow(i, fieldName, fieldValue);
                }

                this.saveConfigurationButton.setDisable(false);
            } else {
                this.saveConfigurationButton.setDisable(true);
            }
        });
    }

    public void updateSelectedEntity(TechnicalEntity selectedEntity) {
        if (selectedEntity instanceof System) {
            this.system = (System) selectedEntity;
        } else if (selectedEntity instanceof Component) {
            this.system = ((Component) selectedEntity).getSystem();
        } else if (selectedEntity instanceof ComponentGroup) {
            this.system = ((ComponentGroup) selectedEntity).getSystem();
        }

        if (this.system != null) {
            List<Learner> learners = persistenceBean.find(Learner.class, Learner.NQ_BY_SYSTEM_ORDERED,
                    new Parameters().add("system", system));
            if (learners != null && !learners.isEmpty()) {
                this.learnerChoiceBox.setItems(FXCollections.observableList(learners));
            } else {
                this.learnerChoiceBox.setItems(FXCollections.emptyObservableList());
            }
        } else {
            this.learnerChoiceBox.setItems(FXCollections.emptyObservableList());
        }

        this.learnerChoiceBox.getSelectionModel().clearSelection();
        this.learnerParameters.getChildren().clear();
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
        this.dialogController.addLearner(system);
    }

    @FXML
    private void saveConfiguration() {
        List<String> parameterValues = LearningUtils.collectFieldValues(learnerParameters);
        Learner learner = learnerChoiceBox.getSelectionModel().getSelectedItem();

        // TODO: validate parameters

        learner.getConfiguration().setConfigurableFields(parameterValues);
    }

    @FXML
    private void showHelpLearnerConfiguration() {

    }
}