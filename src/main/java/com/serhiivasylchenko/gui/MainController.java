package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class MainController implements Initializable {
    @FXML
    private TreeView<TechnicalEntity> componentsTreeView;
    @FXML
    private ToggleGroup evaluationToggleGroup;
    @FXML
    private Label status;
    @FXML
    private TextField searchTextField;
    @FXML
    private TabPane configurationTabPane;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private DialogController dialogController = DialogController.getInstance();

    @FXML
    private ParametersPaneController parametersPaneController;
    @FXML
    private TrainingPaneController trainingPaneController;

    private System system;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsTreeView.setCellFactory(p -> new ContextMenuTreeCell());
        componentsTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<TechnicalEntity> selectedItem = componentsTreeView.getSelectionModel().getSelectedItem();
            // Selected item can be null, if the tree was recreated, for example on update
            if (selectedItem != null) {
                TechnicalEntity selectedEntity = selectedItem.getValue();

                parametersPaneController.showEntityParameters(selectedEntity);

                if (selectedEntity instanceof System) {
                    this.system = (System) selectedEntity;
                } else if (selectedEntity instanceof Component) {
                    this.system = ((Component) selectedEntity).getSystem();
                } else if (selectedEntity instanceof ComponentGroup) {
                    this.system = ((ComponentGroup) selectedEntity).getSystem();
                }

                trainingPaneController.updateSelectedEntity(this.system);

            }
        });

        configurationTabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

        });

        guiUpdater.setComponentsTreeView(componentsTreeView);
        guiUpdater.setSearchTextField(searchTextField);

        updateComponentList();
    }

    @FXML
    private void runAnalysis() {

    }

    @FXML
    private void addComponent() {
        status.setText("Adding new component...");
        dialogController.addComponent();
        status.setText("Idle");
    }

    @FXML
    private void addSystem() {
        status.setText("Adding new system...");
        dialogController.addSystem();
        status.setText("Idle");
    }

    @FXML
    private void addComponentGroup() {
        status.setText("Adding new component group...");
        dialogController.addComponentGroup();
        status.setText("Idle");
    }

    @FXML
    private void updateComponentList() {
        status.setText("Updating component list...");
        guiUpdater.updateComponentTree();
        status.setText("Idle");
    }
}
