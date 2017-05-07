package com.serhiivasylchenko.persistence;

import com.sun.istack.internal.NotNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * @author Serhii Vasylchenko
 */
@MappedSuperclass
public abstract class TechnicalEntity extends Conditional {

    private static final long serialVersionUID = -8572714188110426056L;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private LocalDate creationTime;

    @OneToOne
    private ParameterList parameterList;

    public TechnicalEntity(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
        this.creationTime = LocalDate.now();
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
