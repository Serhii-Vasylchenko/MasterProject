package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.TechnicalEntity;
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
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();

    private DialogController dialogController = DialogController.getInstance();
    @FXML
    private ParametersPaneController parametersPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsTreeView.setCellFactory(p -> new ContextMenuTreeCell());
        componentsTreeView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem<Object> selectedItem = componentsTreeView.getSelectionModel().getSelectedItem();
            // Selected item can be null, if the tree was recreated, for example on update
            if (selectedItem != null) {
                parametersPaneController.showEntityParameters((TechnicalEntity) selectedItem.getValue());
            }
        });

        guiUpdater.setComponentsTreeView(componentsTreeView);

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
