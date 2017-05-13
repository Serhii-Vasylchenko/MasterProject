package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class MainController implements Initializable {
    @FXML
    private TreeView<String> componentsTreeView;
    @FXML
    private ToggleGroup evaluationToggleGroup;
    @FXML
    private Label status;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    @FXML
    private AddDialogController addDialogController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsTreeView.setCellFactory(p -> new ContextMenuTreeCell());
        componentsTreeUpdater.setComponentsTreeView(componentsTreeView);

        updateComponentList();
    }

    @FXML
    private void runAnalysis() {

    }

    @FXML
    private void addEntity() {
        status.setText("Adding new entity...");
        addDialogController.showAddDialog();
        status.setText("Idle");
    }

    @FXML
    private void updateComponentList() {
        status.setText("Updating component list...");
        componentsTreeUpdater.update();
        status.setText("Idle");
    }
}
