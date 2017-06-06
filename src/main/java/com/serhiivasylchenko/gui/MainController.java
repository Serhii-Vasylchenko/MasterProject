package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.learners.LearningUtils;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.net.URL;
import java.util.List;
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

    @FXML
    private GridPane resultGridPane;
    @FXML
    private Label targetFieldName;

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

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public void showResult(System system, List<INDArray> indArrayList) {
        this.resultGridPane.getChildren().clear();
        this.resultGridPane.setVisible(true);

        List<Field> targetFields = LearningUtils.getTargetFields(system);
        for (int i = 0; i < targetFields.size(); i++) {
            Field targetField = targetFields.get(i);
            INDArray result = indArrayList.get(i);

            // "compName->fieldName"
            this.targetFieldName.setText(targetField.getParameterList().getParentEntity().toString() + "->" + targetField.toString());

            switch (targetField.getFieldType()) {
                case INT_NUMBER:
                    break;
                case FLOAT_NUMBER:
                    break;
                case CHOICE_BOX:


                    GridPane recommendationGridPane = new GridPane();
                    recommendationGridPane.setHgap(10);
                    recommendationGridPane.setVgap(10);

                    for (int j = 0; j < targetField.getChoiceStrings().size(); j++) {
                        String option = targetField.getChoiceStrings().get(j);
                        float recommendation = result.getFloat(j);

                        Label optionLabel = new Label(option);
                        Label recomLabel = new Label(String.valueOf(recommendation));

                        recommendationGridPane.addRow(j, optionLabel, recomLabel);
                    }

                    recommendationGridPane.getColumnConstraints().forEach(columnConstraints -> {
                        columnConstraints.setHalignment(HPos.CENTER);
                    });
//                    recommendationGridPane.autosize();
                    this.resultGridPane.addRow(i, targetFieldName, recommendationGridPane);
                    break;
            }
        }
    }
}
