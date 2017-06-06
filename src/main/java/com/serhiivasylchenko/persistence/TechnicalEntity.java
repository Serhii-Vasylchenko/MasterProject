package com.serhiivasylchenko.persistence;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public abstract class TechnicalEntity extends Conditional {

    private static final long serialVersionUID = -8572714188110426056L;

    @Column
    private String description;

    @Column
    private LocalDate creationTime;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ParameterList parameterList;

    public TechnicalEntity(String name, String description) {
        super(name);
        this.description = description;
        this.creationTime = LocalDate.now();
        this.parameterList = new ParameterList(this);
    }

    public TechnicalEntity() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
}
