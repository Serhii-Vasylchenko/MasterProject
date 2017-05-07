package com.serhiivasylchenko.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries({
        @NamedQuery(name = ComponentGroup.NQ_ALL, query = "SELECT x FROM ComponentGroup x"),
        @NamedQuery(name = ComponentGroup.NQ_BY_NAME_AND_SYSTEM, query = "SELECT x FROM ComponentGroup x WHERE x.name = :name AND x.system = :system"),
        @NamedQuery(name = ComponentGroup.NQ_BY_SYSTEM_NAME, query = "SELECT x FROM ComponentGroup x WHERE x.system.name = :systemName")
})
public class ComponentGroup extends TechnicalEntity implements Group {

    private static final long serialVersionUID = 3880001170507004727L;

    public static final String NQ_ALL = "nq.group.get.all";
    public static final String NQ_BY_NAME_AND_SYSTEM = "nq.group.get.by.name.and.system";
    public static final String NQ_BY_SYSTEM_NAME = "nq.group.by.system.name";

    @ManyToOne
    private System system;

    @ManyToOne
    private ComponentGroup parent;

    @OneToMany
    private List<Component> componentChildren;

    @OneToMany
    private List<ComponentGroup> componentGroupChildren;

    public ComponentGroup(System system, String name, String description) {
        super(name, description);
        this.system = system;
        this.parent = null;
        this.componentGroupChildren = new ArrayList<>();
        this.componentChildren = new ArrayList<>();
    }

    public ComponentGroup() {
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

    public List<Component> getComponentChildren() {
        return componentChildren;
    }

    public void setComponentChildren(ArrayList<Component> componentChildren) {
        this.componentChildren = componentChildren;
    }

    public List<ComponentGroup> getComponentGroupChildren() {
        return componentGroupChildren;
    }

    public void setComponentGroupChildren(ArrayList<ComponentGroup> componentGroupChildren) {
        this.componentGroupChildren = componentGroupChildren;
    }
}
