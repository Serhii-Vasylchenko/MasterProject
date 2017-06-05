package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.Utils;
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
    private ControllerMap controllerMap = ControllerMap.getInstance();

    @FXML
    private ParametersPaneController parametersPaneController;
    @FXML
    private TrainingPaneController trainingPaneController;

    private System system;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.componentsTreeView.setCellFactory(p -> new ContextMenuTreeCell());
        this.componentsTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<TechnicalEntity> selectedItem = this.componentsTreeView.getSelectionModel().getSelectedItem();
            // Selected item can be null, if the tree was recreated, for example on update
            if (selectedItem != null) {
                TechnicalEntity selectedEntity = selectedItem.getValue();

                this.parametersPaneController.showEntityParameters(selectedEntity);

                this.system = Utils.getSystem(selectedEntity);

                this.trainingPaneController.updateSelectedEntity(this.system);

            }
        });

        this.guiUpdater.setComponentsTreeView(this.componentsTreeView);
        this.guiUpdater.setSearchTextField(this.searchTextField);

        updateComponentList();

        this.controllerMap.addController(this);
        this.controllerMap.addController(this.parametersPaneController);
        this.controllerMap.addController(this.trainingPaneController);

        this.parametersPaneController.lookupControllers();
    }

    @FXML
    private void addComponent() {
        this.status.setText("Adding new component...");
        this.dialogController.addComponent();
        this.status.setText("Idle");
    }

    @FXML
    private void addSystem() {
        this.status.setText("Adding new system...");
        this.dialogController.addSystem();
        this.status.setText("Idle");
    }

    @FXML
    private void addComponentGroup() {
        this.status.setText("Adding new component group...");
        this.dialogController.addComponentGroup();
        this.status.setText("Idle");
    }

    @FXML
    private void updateComponentList() {
        this.status.setText("Updating component list...");
        this.guiUpdater.updateComponentTree();
        this.status.setText("Idle");
    }

    public void selectTab(int tabIndex) {
        this.configurationTabPane.getSelectionModel().select(tabIndex);
    }
}
