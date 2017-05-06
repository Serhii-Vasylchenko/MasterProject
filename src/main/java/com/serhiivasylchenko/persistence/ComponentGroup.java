package com.serhiivasylchenko.persistence;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class ComponentGroup extends TechnicalEntity implements Group {

    private static final long serialVersionUID = 3880001170507004727L;

    @ManyToOne
    private System system;

    @OneToOne
    private ComponentGroup parent;

    @OneToMany
    private ArrayList<Component> componentChildren;

    @OneToMany
    private ArrayList<ComponentGroup> componentGroupChildren;

    public ComponentGroup(String name, String description, System system) {
        super(name, description);
        this.system = system;
        this.parent = null;
        this.componentGroupChildren = new ArrayList<>();
        this.componentChildren = new ArrayList<>();
    }

    public ComponentGroup() {
        this.parent = null;
        this.componentGroupChildren = new ArrayList<>();
        this.componentChildren = new ArrayList<>();
    }

    @Override
    public Persistable getParent() {
        return parent;
    }

    @Override
    public List<Persistable> getChildren() {
        List<Persistable> children = new ArrayList<>();
        children.addAll(componentGroupChildren);
        children.addAll(componentChildren);
        return children;
    }

    @Override
    public List<Persistable> getChildrenSorted() {
        List<Persistable> children = new ArrayList<>();
        children.addAll(componentGroupChildren.stream()
                .sorted(Persistable::compareTo)
                .collect(Collectors.toList()));
        children.addAll(componentChildren.stream()
                .sorted(Persistable::compareTo)
                .collect(Collectors.toList()));
        return children;
    }

    public void addComponent(Component component) {
        componentChildren.add(component);
    }

    public void addComponentGroup(ComponentGroup componentGroup) {
        componentGroupChildren.add(componentGroup);
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public void setParent(ComponentGroup parent) {
        this.parent = parent;
    }

    public ArrayList<Component> getComponentChildren() {
        return componentChildren;
    }

    public void setComponentChildren(ArrayList<Component> componentChildren) {
        this.componentChildren = componentChildren;
    }

    public ArrayList<ComponentGroup> getComponentGroupChildren() {
        return componentGroupChildren;
    }

    public void setComponentGroupChildren(ArrayList<ComponentGroup> componentGroupChildren) {
        this.componentGroupChildren = componentGroupChildren;
    }
}
