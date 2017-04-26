package com.serhiivasylchenko.core.components;

import com.serhiivasylchenko.core.components.parameters.ParameterList;
import com.serhiivasylchenko.solvers.AbstractSolver;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
