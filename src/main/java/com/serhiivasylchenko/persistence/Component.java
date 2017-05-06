package com.serhiivasylchenko.persistence;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Component.NQ_BY_SYSTEM_NAME, query = "SELECT x FROM Component x WHERE x.system.name = :systemName")
})
public class Component extends TechnicalEntity {

    private static final long serialVersionUID = -2461904212127946257L;

    public static final String NQ_BY_SYSTEM_NAME = "nq.get.by.system";

    @ManyToOne
    private System system;

    @ManyToOne
    private ComponentGroup componentGroup;

    @OneToOne
    private AbstractSolver solver;

    public Component(System system, ComponentGroup componentGroup, String name, String description) {
        super(name, description);
        this.system = system;
        this.componentGroup = componentGroup;
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

    public ComponentGroup getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(ComponentGroup componentGroup) {
        this.componentGroup = componentGroup;
    }
}
