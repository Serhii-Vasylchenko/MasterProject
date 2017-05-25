package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Persistable;
import com.serhiivasylchenko.persistence.System;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class GUIUpdater {

    private static final Logger LOGGER = Logger.getLogger(GUIUpdater.class);

    private static GUIUpdater instance;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();

    private TreeView<Object> componentsTreeView;
    private GridPane parametersGridPane;
    private final Image systemIcon = new Image(getClass().getResourceAsStream("/icons/system1_16.png"));
    private final Image componentIcon = new Image(getClass().getResourceAsStream("/icons/component1_16.png"));

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
            directChildren.addAll(workflowManager.getComponentGroupList(system).stream()
                    .filter(group -> group.getParentGroup() == null)
                    .collect(Collectors.toList()));
            directChildren.addAll(workflowManager.getComponentList(system).stream()
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
                childNode = new TreeItem<>(group, GlyphsDude.createIcon(FontAwesomeIcon.OBJECT_GROUP, "16px"));
                // Other component groups may be nested inside, so we need to check for this
                List<Persistable> directChildren = new ArrayList<>();
                directChildren.addAll(workflowManager.getComponentList(group));
                directChildren.addAll(workflowManager.getComponentGroupList(group));
                addChildrenToTheNode(childNode, directChildren);
            } else if (child instanceof Component) {
                Component component = (Component) child;
                childNode = new TreeItem<>(component, new ImageView(componentIcon));
            }

            if (childNode != null) {
                node.getChildren().add(childNode);
            } else {
                LOGGER.warn("Unknown entity on updating components tree view!");
            }
        });
    }


}
