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
    public static final String NQ_BY_SYSTEM_NAME = "nq.get.by.system";

    @ManyToOne
    private System system;

    @OneToOne
    private AbstractSolver solver;

    public Component(System system, String name, String description) {
        super(name, description);
        this.system = system;
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
}
