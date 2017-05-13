package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * @author Serhii Vasylchenko
 */
public final class ContextMenuTreeCell extends TreeCell<String> {

    private ContextMenu contextMenu = new ContextMenu();

    private WorkflowManager workflowManager = WorkflowManager.getInstance();
    private ComponentsTreeUpdater componentsTreeUpdater = ComponentsTreeUpdater.getInstance();

    ContextMenuTreeCell() {
        MenuItem addMenuItem = new MenuItem("Add new here");
        MenuItem deleteMenuItem = new MenuItem("Remove");
        contextMenu.getItems().addAll(addMenuItem, deleteMenuItem);
        addMenuItem.setOnAction(event -> {
            TreeItem<String> systemNode = findSystem(getTreeItem());

        });
        deleteMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete this element and all its children? There would be no way back!");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                TreeItem<String> systemNode = findSystem(getTreeItem());
                if (systemNode == getTreeItem()) {
                    workflowManager.deleteSystem(systemNode.getValue());
                } else {
                    workflowManager.deleteEntityFromSystem(systemNode.getValue(), getTreeItem().getValue());
                }
                componentsTreeUpdater.update();
            }
        });
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(contextMenu);
        }
    }

    private TreeItem<String> findSystem(TreeItem<String> node) {
        while (node.getParent() != getTreeView().getRoot()) {
            node = node.getParent();
        }
        return node;
    }
}
