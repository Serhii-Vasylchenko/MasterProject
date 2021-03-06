package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Named;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * @author Serhii Vasylchenko
 */
public final class ContextMenuTreeCell extends TreeCell<TechnicalEntity> {

    private ContextMenu contextMenu = new ContextMenu();

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private DialogController dialogController = DialogController.getInstance();

    ContextMenuTreeCell() {
        MenuItem addComponentMenuItem = new MenuItem("Add new component here");
        MenuItem addGroupMenuItem = new MenuItem("Add new group here");
        MenuItem renameMenuItem = new MenuItem("Rename");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(addComponentMenuItem, addGroupMenuItem, renameMenuItem, deleteMenuItem);

        addComponentMenuItem.setOnAction(event -> {
            TreeItem<TechnicalEntity> systemNode = findSystem(getTreeItem());
            if (getTreeItem().getValue() instanceof ComponentGroup) {
                dialogController.addComponent((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getValue());
            } else {
                if (getTreeItem().getParent().getValue() instanceof System) {
                    dialogController.addComponent((System) systemNode.getValue(), null);
                } else {
                    dialogController.addComponent((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getParent().getValue());
                }
            }
        });

        addGroupMenuItem.setOnAction(event -> {
            TreeItem<TechnicalEntity> systemNode = findSystem(getTreeItem());
            if (getTreeItem().getValue() instanceof ComponentGroup) {
                dialogController.addComponentGroup((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getValue());
            } else {
                if (getTreeItem().getParent().getValue() instanceof System) {
                    dialogController.addComponentGroup((System) systemNode.getValue(), null);
                } else {
                    dialogController.addComponentGroup((System) systemNode.getValue(), (ComponentGroup) getTreeItem().getParent().getValue());
                }
            }
        });

        renameMenuItem.setOnAction(event -> {
            if (Named.class.isAssignableFrom(getTreeItem().getValue().getClass())) {
                dialogController.changeName(getTreeItem().getValue());
            }
        });

        deleteMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete this element and all its children? There would be no way back!");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                workflowManager.deleteEntity(getTreeItem().getValue());
                guiUpdater.updateComponentTree();
            }
        });
    }

    @Override
    public void updateItem(TechnicalEntity item, boolean empty) {
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

    private TreeItem<TechnicalEntity> findSystem(TreeItem<TechnicalEntity> node) {
        while (!(node.getValue() instanceof System)) {
            node = node.getParent();
        }
        return node;
    }


}
