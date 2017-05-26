package com.serhiivasylchenko.persistence;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Component.NQ_ALL, query = "SELECT x FROM Component x"),
        @NamedQuery(name = Component.NQ_BY_SYSTEM, query = "SELECT x FROM Component x WHERE x.system = :system ORDER BY x.id"),
        @NamedQuery(name = Component.NQ_BY_PARENT_GROUP, query = "SELECT x FROM Component x WHERE x.parentGroup = :parentGroup ORDER BY x.id"),
        @NamedQuery(name = Component.NQ_BY_NAME_AND_SYSTEM, query = "SELECT x FROM Component x WHERE x.name = :name and x.system = :system")
})
public class Component extends TechnicalEntity {

    private static final long serialVersionUID = -2461904212127946257L;

    public static final String NQ_ALL = "nq.component.get.all";
    public static final String NQ_BY_SYSTEM = "nq.component.get.by.system";
    public static final String NQ_BY_PARENT_GROUP = "nq.component.get.by.parent.group";
    public static final String NQ_BY_NAME_AND_SYSTEM = "nq.component.get.by.name.and.system";

    @ManyToOne
    private System system;

    @ManyToOne
    private ComponentGroup parentGroup;

    @OneToOne
    private AbstractSolver solver;

    public Component(System system, ComponentGroup parentGroup, String name, String description) {
        super(name, description);
        this.system = system;
        this.parentGroup = parentGroup;
    }

    public Component() {
    }

    public AbstractSolver getSolver() {
        return solver;
    }

    public void setSolver(AbstractSolver solver) {
        this.solver = solver;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public ComponentGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(ComponentGroup componentGroup) {
        this.parentGroup = componentGroup;
    }
}
