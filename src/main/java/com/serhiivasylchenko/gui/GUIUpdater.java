package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.gui.filtering.FilterableTreeItem;
import com.serhiivasylchenko.gui.filtering.TreeItemPredicate;
import com.serhiivasylchenko.persistence.*;
import com.serhiivasylchenko.persistence.System;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class GUIUpdater {

    private static final Logger LOGGER = Logger.getLogger(GUIUpdater.class);

    private static GUIUpdater instance;

    private WorkflowManager workflowManager = WorkflowManager.getInstance();

    private TreeView<TechnicalEntity> componentsTreeView;
    private GridPane parametersGridPane;
    private TextField searchTextField;
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

    public void setComponentsTreeView(TreeView<TechnicalEntity> componentsTreeView) {
        this.componentsTreeView = componentsTreeView;
    }

    public void setParametersGridPane(GridPane parametersGridPane) {
        this.parametersGridPane = parametersGridPane;
    }

    public void setSearchTextField(TextField searchTextField) {
        this.searchTextField = searchTextField;
    }

    public void updateComponentTree() {
        List<System> systems = workflowManager.getSystemList();

        FilterableTreeItem<TechnicalEntity> rootNode = new FilterableTreeItem<>(null);
        rootNode.setExpanded(true);
        systems.forEach(system -> {
            FilterableTreeItem<TechnicalEntity> systemNode = new FilterableTreeItem<>(system);
            systemNode.setGraphic(new ImageView(systemIcon));

            List<Persistable> directChildren = new ArrayList<>();
            directChildren.addAll(workflowManager.getComponentGroupList(system).stream()
                    .filter(group -> group.getParentGroup() == null)
                    .collect(Collectors.toList()));
            directChildren.addAll(workflowManager.getComponentList(system).stream()
                    .filter(component -> component.getParentGroup() == null)
                    .collect(Collectors.toList()));

            addChildrenToTheNode(systemNode, directChildren);
            rootNode.getInternalChildren().add(systemNode);
        });

        rootNode.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (searchTextField.getText() == null || searchTextField.getText().isEmpty())
                return null;
            return TreeItemPredicate.create(entity -> entity.toString().contains(searchTextField.getText()));
        }, searchTextField.textProperty()));

        componentsTreeView.setRoot(rootNode);
    }

    private void addChildrenToTheNode(FilterableTreeItem<TechnicalEntity> node, List<? extends Persistable> children) {
        // Set the node to be expanded by default
        node.setExpanded(true);

        children.forEach(child -> {
            FilterableTreeItem<TechnicalEntity> childNode = null;

            // Assign different icons depending on the class
            if (child instanceof ComponentGroup) {
                ComponentGroup group = (ComponentGroup) child;
                childNode = new FilterableTreeItem<>(group);
                childNode.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.OBJECT_GROUP, "16px"));

                // Other component groups may be nested inside, so we need to check for this
                List<Persistable> directChildren = new ArrayList<>();
                directChildren.addAll(workflowManager.getComponentGroupList(group));
                directChildren.addAll(workflowManager.getComponentList(group));
                addChildrenToTheNode(childNode, directChildren);
            } else if (child instanceof Component) {
                Component component = (Component) child;
                childNode = new FilterableTreeItem<>(component);
                childNode.setGraphic(new ImageView(componentIcon));
            }

            if (childNode != null) {
                node.getInternalChildren().add(childNode);
            } else {
                LOGGER.warn("Unknown entity on updating components tree view!");
            }
        });
    }

    public void filterComponentsTree(String input) {
        if (input == null || Objects.equals(input, "")) {
            updateComponentTree();
        } else {

        }
    }
}
