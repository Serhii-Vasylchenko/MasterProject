package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.persistence.learning.AbstractConfiguration;
import com.serhiivasylchenko.persistence.learning.DL4JConfiguration;
import com.serhiivasylchenko.persistence.learning.Learner;
import com.serhiivasylchenko.utils.LearnerType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class AddLearnerDialog extends VBox implements Initializable, IDialog {
    @FXML
    private Label dialogName;
    @FXML
    private TextField learnerName;
    @FXML
    private ChoiceBox<LearnerType> learnerType;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    private System system;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public AddLearnerDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addLearnerDialog.fxml"));
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
        if (system != null) {
            this.system = system;

            Dialog<List<String>> dialog = new Dialog<>();
            dialog.setTitle("Add new field");
            dialog.setContentText(null);

            dialogName.setText("Add new learner for system '" + system.toString() + "'");
            learnerType.setItems(FXCollections.observableList(Arrays.asList(LearnerType.values())));
            learnerType.getSelectionModel().selectFirst();

            // Set the button types.
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Validation on fields
            final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
            addButton.addEventFilter(ActionEvent.ACTION, event -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong fields!");
                alert.setHeaderText(null);

                if (learnerName.getText().isEmpty()) {
                    event.consume();
                    alert.setContentText("Please set the name of this learner");
                    alert.showAndWait();
                }
            });

            dialog.getDialogPane().setContent(this);

            // Request focus on the name field by default.
            Platform.runLater(learnerName::requestFocus);

            // Send the result to workflowManager when the add button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    addLearner(learnerName.getText(), learnerType.getValue());
                }
                learnerName.setText("");
                return null;
            });

            dialog.showAndWait();
        }
    }

    private void addLearner(String learnerName, LearnerType learnerType) {
        AbstractConfiguration configuration = null;
        switch (learnerType) {
            case MLP_CLASSIFIER_LINEAR:
                configuration = new DL4JConfiguration();
                configuration.initDefault();
                break;
        }

        Learner learner = new Learner(this.system, learnerName, learnerType, configuration);
        configuration.setLearner(learner);

        persistenceBean.persist(learner);
    }
}
