package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.Named;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.LearnerType;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public abstract class AbstractLearner extends Named {

    private static final long serialVersionUID = -1361182570280398360L;

    @ManyToOne
    private System system;

    @Column
    private LearnerType type;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AbstractConfiguration configuration;

    public AbstractLearner(String name, LearnerType learnerType) {
        super(name);
        this.type = learnerType;
    }

    protected AbstractLearner() {
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
