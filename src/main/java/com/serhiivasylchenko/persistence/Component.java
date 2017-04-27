package com.serhiivasylchenko.persistence;

import com.serhiivasylchenko.solvers.AbstractSolver;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class Component extends Conditional {
    @Column
    private String name;

    @Column
    private String description = null;

    @Column
    private ParameterList parameterList;

    @Column
    private AbstractSolver solver;

    public Component(String name) {
        this.name = name;
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

    public AbstractSolver getSolver() {
        return solver;
    }

    public void setSolver(AbstractSolver solver) {
        this.solver = solver;
    }
}
