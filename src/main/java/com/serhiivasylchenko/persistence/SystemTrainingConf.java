package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class SystemTrainingConf extends CachedPersistable {

    private static final long serialVersionUID = 939355041147046426L;

    @ManyToOne
    private System system;

    @Column
    private String pathToTrainingSet;

    @Column
    private String pathToTestSet;

    public SystemTrainingConf(System system) {
        this.system = system;

    }

    public SystemTrainingConf() {
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public String getPathToTrainingSet() {
        return pathToTrainingSet;
    }

    public void setPathToTrainingSet(String pathToTrainingSet) {
        this.pathToTrainingSet = pathToTrainingSet;
    }

    public String getPathToTestSet() {
        return pathToTestSet;
    }

    public void setPathToTestSet(String pathToTestSet) {
        this.pathToTestSet = pathToTestSet;
    }
}
