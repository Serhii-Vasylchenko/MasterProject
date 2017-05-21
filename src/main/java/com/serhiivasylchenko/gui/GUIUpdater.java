package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Persistable;
import com.serhiivasylchenko.persistence.System;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class GUIUpdater {
    private static GUIUpdater instance;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();

    private TreeView<Object> componentsTreeView;
    private GridPane parametersGridPane;
    private final Image systemIcon = new Image(getClass().getResourceAsStream("/icons/system1_16.png"));
    private final Image componentIcon = new Image(getClass().getResourceAsStream("/icons/component1_16.png"));
    private final Image groupIcon = new Image(getClass().getResourceAsStream("/icons/group1_16.png"));

    private GUIUpdater() {
    }

    public static GUIUpdater getInstance() {
        if (instance == null) {
            instance = new GUIUpdater();
        }
        return instance;
    }

    public void setComponentsTreeView(TreeView<Object> componentsTreeView) {
        this.componentsTreeView = componentsTreeView;
    }

    public void setParametersGridPane(GridPane parametersGridPane) {
        this.parametersGridPane = parametersGridPane;
    }

    public void updateComponentTree() {
        List<System> systems = workflowManager.getSystemList();

        TreeItem<Object> rootNode = new TreeItem<>();
        rootNode.setExpanded(true);
        systems.forEach(system -> {
            TreeItem<Object> systemNode = new TreeItem<>(system, new ImageView(systemIcon));

            List<Persistable> directChildren = new ArrayList<>();
            directChildren.addAll(system.getComponentGroups().stream()
                    .filter(group -> group.getParentGroup() == null)
                    .collect(Collectors.toList()));
            directChildren.addAll(system.getComponents().stream()
                    .filter(component -> component.getParentGroup() == null)
                    .collect(Collectors.toList()));

            addChildrenToTheNode(systemNode, directChildren);
            rootNode.getChildren().add(systemNode);
        });
        componentsTreeView.setRoot(rootNode);
    }

    private void addChildrenToTheNode(TreeItem<Object> node, List<? extends Persistable> children) {
        // Set the node to be expanded by default
        node.setExpanded(true);

        children.forEach(child -> {
            TreeItem<Object> childNode = null;

            // Assign different icons depending on the class
            if (child instanceof ComponentGroup) {
                ComponentGroup group = (ComponentGroup) child;
                childNode = new TreeItem<>(group, new ImageView(groupIcon));
                // Other component groups may be nested inside, so we need to check for this
                addChildrenToTheNode(childNode, group.getChildren());
            } else if (child instanceof Component) {
                Component component = (Component) child;
                childNode = new TreeItem<>(component, new ImageView(componentIcon));
            }

            if (childNode != null) {
                node.getChildren().add(childNode);
            }
        });
    }


}
