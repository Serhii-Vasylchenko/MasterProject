package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.persistence.CachedPersistable;
import com.serhiivasylchenko.persistence.System;

import javax.persistence.*;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = SystemTrainingConf.NQ_BY_SYSTEM_ORDERED, query = "SELECT x FROM SystemTrainingConf x WHERE x.system = :system ORDER BY x.id")
)
public class SystemTrainingConf extends CachedPersistable {

    private static final long serialVersionUID = 939355041147046426L;

    public static final String NQ_BY_SYSTEM_ORDERED = "nq.training.conf.get.by.system.ordered";

    @OneToOne
    private System system;

    @Column
    private String pathToTrainingSet;

    @Column
    private String pathToTestSet;

    public SystemTrainingConf(System system) {
        this.system = system;
        this.pathToTrainingSet = this.system.toString().replace(" ", "") + "-Training.csv";
        this.pathToTestSet = this.system.toString().replace(" ", "") + "-Testing.csv";
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
