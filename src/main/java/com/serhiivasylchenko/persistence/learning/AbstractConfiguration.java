package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.CachedPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collections;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public abstract class AbstractConfiguration extends CachedPersistable {

    private static final long serialVersionUID = -3685822835055646148L;

    @OneToOne
    private AbstractLearner learner;

    /**
     * Override in child classes, used buy GUI to get fields for configuration
     *
     * @return list of configurable fields
     */
    public List<LearnerParameter> getConfigurableFields() {
        return Collections.emptyList();
    }

    /**
     * Override in child classes, used buy GUI to set configuration fields
     */
    public void setConfigurableFields() {
    }

    protected AbstractConfiguration() {
        super();
    }

    public AbstractLearner getLearner() {
        return learner;
    }

    public void setLearner(AbstractLearner learner) {
        this.learner = learner;
    }
}
