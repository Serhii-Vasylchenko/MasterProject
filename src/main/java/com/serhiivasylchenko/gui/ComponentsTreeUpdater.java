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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class ComponentsTreeUpdater {
    private static ComponentsTreeUpdater instance;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();

    private TreeView<String> componentsTreeView;
    private final Image systemIcon = new Image(getClass().getResourceAsStream("/icons/system1_16.png"));
    private final Image componentIcon = new Image(getClass().getResourceAsStream("/icons/component1_16.png"));
    private final Image groupIcon = new Image(getClass().getResourceAsStream("/icons/group1_16.png"));

    private ComponentsTreeUpdater() {
    }

    public static ComponentsTreeUpdater getInstance() {
        if (instance == null) {
            instance = new ComponentsTreeUpdater();
        }
        return instance;
    }

    public void setComponentsTreeView(TreeView<String> componentsTreeView) {
        this.componentsTreeView = componentsTreeView;
    }

    public void update() {
        List<System> systems = workflowManager.getSystemList();

        TreeItem<String> rootNode = new TreeItem<>();
        rootNode.setExpanded(true);
        systems.forEach(system -> {
            TreeItem<String> systemNode = new TreeItem<>(system.getName(), new ImageView(systemIcon));

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

    private void addChildrenToTheNode(TreeItem<String> node, List<? extends Persistable> children) {
        // Set the node to be expanded by default
        node.setExpanded(true);

        children.forEach(child -> {
            TreeItem<String> childNode = null;

            // Assign different icons depending on the class
            if (child instanceof ComponentGroup) {
                ComponentGroup group = (ComponentGroup) child;
                childNode = new TreeItem<>(group.getName(), new ImageView(groupIcon));
                // Other component groups may be nested inside, so we need to check for this
                addChildrenToTheNode(childNode, group.getChildren());
            } else if (child instanceof Component) {
                Component component = (Component) child;
                childNode = new TreeItem<>(component.getName(), new ImageView(componentIcon));
            }

            if (childNode != null) {
                node.getChildren().add(childNode);
            }
        });
    }
}
