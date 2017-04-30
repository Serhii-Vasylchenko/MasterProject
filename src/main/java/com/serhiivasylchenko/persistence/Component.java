package com.serhiivasylchenko.persistence;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class Component extends TechnicalEntity {
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
