package com.serhiivasylchenko.persistence;

import com.serhiivasylchenko.persistence.learning.Learner;
import com.serhiivasylchenko.persistence.learning.SystemTrainingConf;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries({
        @NamedQuery(name = System.NQ_ALL, query = "SELECT x FROM System x ORDER BY x.id"),
        @NamedQuery(name = System.NQ_BY_NAME, query = "SELECT x FROM System x WHERE x.name = :name")
})
@SuppressWarnings("unused")
public class System extends TechnicalEntity implements Group {

    private static final long serialVersionUID = 8937660893306074636L;

    public static final String NQ_ALL = "nq.system.get.all";
    public static final String NQ_BY_NAME = "nq.system.get.by.name";

    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Component> components;

    @OneToMany(mappedBy = "system", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentGroup> componentGroups;

    @OneToOne(mappedBy = "system", cascade = CascadeType.ALL, orphanRemoval = true)
    private SystemTrainingConf systemTrainingConf;

    @OneToMany(mappedBy = "system", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Learner> learners;

    public System(String name, String description) {
        super(name, description);
        this.systemTrainingConf = new SystemTrainingConf(this);
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
        return components == null ? Collections.emptyList() : components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<ComponentGroup> getComponentGroups() {
        return componentGroups == null ? Collections.emptyList() : componentGroups;
    }

    public void setComponentGroups(List<ComponentGroup> componentGroups) {
        this.componentGroups = componentGroups;
    }

    public SystemTrainingConf getSystemTrainingConf() {
        return systemTrainingConf;
    }

    public void setSystemTrainingConf(SystemTrainingConf systemTrainingConf) {
        this.systemTrainingConf = systemTrainingConf;
    }

    public List<Learner> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner> learners) {
        this.learners = learners;
    }
}
