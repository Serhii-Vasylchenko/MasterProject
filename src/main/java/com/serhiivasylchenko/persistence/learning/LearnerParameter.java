package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.utils.FieldType;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = LearnerParameter.NQ_BY_LEARNER, query = "SELECT x FROM LearnerParameter x WHERE  x.configuration.learner = :learner ORDER BY x.id")
)
public class LearnerParameter extends Field {

    private static final long serialVersionUID = -8622272036930715326L;

    public static final String NQ_BY_LEARNER = "nq.learnerparam.by.learner";

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AbstractConfiguration configuration;

    @Column
    private boolean isConfigurable;

    public LearnerParameter(AbstractConfiguration configuration, String name, String description, FieldType fieldType, boolean isConfigurable) {
        super(null, name, description, fieldType);
        this.configuration = configuration;
        this.isConfigurable = isConfigurable;
    }

    public LearnerParameter() {
        super();
    }

    public boolean isConfigurable() {
        return isConfigurable;
    }

    public void setConfigurable(boolean configurable) {
        isConfigurable = configurable;
    }

    public AbstractConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(AbstractConfiguration configuration) {
        this.configuration = configuration;
    }
}
