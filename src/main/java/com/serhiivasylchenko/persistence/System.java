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
        @NamedQuery(name = System.NQ_ALL, query = "SELECT x FROM System x"),
        @NamedQuery(name = System.NQ_BY_NAME, query = "SELECT x FROM System x WHERE x.name = :name")
})
public class System extends TechnicalEntity implements Group {

    private static final long serialVersionUID = 8937660893306074636L;

    public static final String NQ_ALL = "nq.system.get.all";
    public static final String NQ_BY_NAME = "nq.system.get.by.name";

    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL)
    private List<Component> components;

    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL)
    private List<ComponentGroup> componentGroups;

    public System(String name, String description) {
        super(name, description);
    }

    public System() {
    }

    @Override
    public Persistable getParent() {
        return null;
    }

    public List<Persistable> getChildren() {
        List<Persistable> children = new ArrayList<>();
        children.addAll(componentGroups);
        children.addAll(components);
        return children;
    }

    public List<Persistable> getChildrenSorted() {
        List<Persistable> children = new ArrayList<>();
        children.addAll(componentGroups.stream()
                .sorted(Persistable::compareTo)
                .collect(Collectors.toList()));
        children.addAll(components.stream()
                .sorted(Persistable::compareTo)
                .collect(Collectors.toList()));
        return children;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<ComponentGroup> getComponentGroups() {
        return componentGroups;
    }

    public void setComponentGroups(List<ComponentGroup> componentGroups) {
        this.componentGroups = componentGroups;
    }
}
