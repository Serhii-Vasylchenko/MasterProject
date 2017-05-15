package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class MainController implements Initializable {
    @FXML
    private TreeView<Object> componentsTreeView;
    @FXML
    private ToggleGroup evaluationToggleGroup;
    @FXML
    private Label status;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    private DialogController dialogController = DialogController.getInstance();
    @FXML
    private ParametersPaneController parametersPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsTreeView.setCellFactory(p -> new ContextMenuTreeCell());
        componentsTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<Object> selectedItem = componentsTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem.getValue() instanceof System) {
                parametersPaneController.showSystemParameters((System) selectedItem.getValue());
            } else if (selectedItem.getValue() instanceof Component) {
                parametersPaneController.showComponentParameters((Component) selectedItem.getValue());
            } else if (selectedItem.getValue() instanceof ComponentGroup) {
                parametersPaneController.showComponentCroupParameters((ComponentGroup) selectedItem.getValue());
            }
        });

        componentsTreeUpdater.setComponentsTreeView(componentsTreeView);

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
        componentsTreeUpdater.update();
        status.setText("Idle");
    }


}
