package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * @author Serhii Vasylchenko
 */
public final class ContextMenuTreeCell extends TreeCell<Object> {

    private ContextMenu contextMenu = new ContextMenu();

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private DialogController dialogController = DialogController.getInstance();

    ContextMenuTreeCell() {
        MenuItem addComponentMenuItem = new MenuItem("Add new component here");
        MenuItem addGroupMenuItem = new MenuItem("Add new group here");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(addComponentMenuItem, addGroupMenuItem, deleteMenuItem);
        addComponentMenuItem.setOnAction(event -> {
            TreeItem<Object> systemNode = findSystem(getTreeItem());
            if (getTreeItem().getParent().getValue() instanceof System) {
                dialogController.addComponent((System) systemNode.getValue(), null);
            } else {
                dialogController.addComponent((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getParent().getValue());
            }
        });
        addGroupMenuItem.setOnAction(event -> {
            TreeItem<Object> systemNode = findSystem(getTreeItem());
            if (getTreeItem().getParent().getValue() instanceof System) {
                dialogController.addComponentGroup((System) systemNode.getValue(), null);
            } else {
                dialogController.addComponentGroup((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getParent().getValue());
            }
        });
        deleteMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete this element and all its children? There would be no way back!");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                workflowManager.deleteEntity((TechnicalEntity) getTreeItem().getValue());
                guiUpdater.updateComponentTree();
            }
        });
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.toString());
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(contextMenu);
        }
    }

    private TreeItem<Object> findSystem(TreeItem<Object> node) {
        while (!(node.getValue() instanceof System)) {
            node = node.getParent();
        }
        return node;
    }


}
