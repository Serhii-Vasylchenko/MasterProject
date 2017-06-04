package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.ParameterList;
import com.serhiivasylchenko.utils.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class LearnerParameter extends Field {

    private static final long serialVersionUID = -8622272036930715326L;

    @Column
    private boolean isConfigurable;

    public LearnerParameter(ParameterList parameterList, String name, String description, FieldType fieldType) {
        super(parameterList, name, description, fieldType);
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
}
