package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class TrainingPaneController implements Initializable {


    private DialogController dialogController = DialogController.getInstance();
    private SharedData sharedData = SharedData.getInstanse();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void addNewExample() {
        TechnicalEntity selectedEntity = sharedData.getSelectedEntity();

        System system = null;
        if (selectedEntity instanceof System) {
            system = (System) selectedEntity;
        } else if (selectedEntity instanceof Component) {
            system = ((Component) selectedEntity).getSystem();
        } else if (selectedEntity instanceof ComponentGroup) {
            system = ((ComponentGroup) selectedEntity).getSystem();
        }

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
    private void showHelpLearnerConfiguration() {

    }
}