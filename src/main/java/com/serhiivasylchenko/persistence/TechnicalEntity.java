package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Serhii Vasylchenko
 */
@MappedSuperclass
public abstract class TechnicalEntity extends Conditional {
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private ParameterList parameterList;

    public TechnicalEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TechnicalEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
}
