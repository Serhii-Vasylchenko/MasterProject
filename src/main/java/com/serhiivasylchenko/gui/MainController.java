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
    private TreeView<Object> componentsTreeView;
    @FXML
    private ToggleGroup evaluationToggleGroup;
    @FXML
    private Label status;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    private DialogController dialogController = DialogController.getInstance();

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
