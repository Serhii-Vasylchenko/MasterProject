package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.Named;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.LearnerType;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = Learner.NQ_BY_SYSTEM_ORDERED, query = "SELECT x FROM Learner x WHERE x.system = :system ORDER BY x.id")
)
public class Learner extends Named {

    private static final long serialVersionUID = -1361182570280398360L;

    public static final String NQ_BY_SYSTEM_ORDERED = "nq.learner.get.by.system.ordered";

    @ManyToOne
    private System system;

    @Column
    private LearnerType type;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AbstractConfiguration configuration;

    public Learner(System system, String name, LearnerType learnerType, AbstractConfiguration configuration) {
        super(name);
        this.system = system;
        this.type = learnerType;
        this.configuration = configuration;
    }

    protected Learner() {
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public LearnerType getType() {
        return type;
    }

    public void setType(LearnerType type) {
        this.type = type;
    }

    public AbstractConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(AbstractConfiguration configuration) {
        this.configuration = configuration;
    }
}
