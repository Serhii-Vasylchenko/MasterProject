package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.utils.FieldType;
import javafx.fxml.Initializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */

@Entity
public class DL4JConfiguration extends AbstractConfiguration implements Initializable {

    private static final long serialVersionUID = -9028081644961906615L;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter seed;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter learningRate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter batchSize;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter numEpochs;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter numInputs;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter numOutputs;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LearnerParameter numHiddenNodes;

    public DL4JConfiguration() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.seed.setFieldType(FieldType.INT_NUMBER);
        this.seed.setIntValue(123);
        this.seed.setConfigurable(true);

        this.learningRate.setFieldType(FieldType.FLOAT_NUMBER);
        this.learningRate.setFloatValue(0.01f);
        this.learningRate.setConfigurable(true);

        this.batchSize.setFieldType(FieldType.INT_NUMBER);
        this.batchSize.setIntValue(50);
        this.batchSize.setConfigurable(true);

        this.numEpochs.setFieldType(FieldType.INT_NUMBER);
        this.numEpochs.setIntValue(30);
        this.numEpochs.setConfigurable(true);

        this.numInputs.setFieldType(FieldType.INT_NUMBER);

        this.numOutputs.setFieldType(FieldType.INT_NUMBER);
        this.numOutputs.setIntValue(1);
        this.numOutputs.setConfigurable(true);

        this.numHiddenNodes.setFieldType(FieldType.INT_NUMBER);
        this.numHiddenNodes.setIntValue(20);
        this.numHiddenNodes.setConfigurable(true);
    }


    @Override
    public List<LearnerParameter> getConfigurableFields() {
        List<LearnerParameter> parameters = new ArrayList<>();


        return parameters;
    }

    @Override
    public void setConfigurableFields() {
        super.setConfigurableFields();
    }

    public LearnerParameter getSeed() {
        return seed;
    }

    public void setSeed(LearnerParameter seed) {
        this.seed = seed;
    }

    public LearnerParameter getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(LearnerParameter learningRate) {
        this.learningRate = learningRate;
    }

    public LearnerParameter getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(LearnerParameter batchSize) {
        this.batchSize = batchSize;
    }

    public LearnerParameter getNumEpochs() {
        return numEpochs;
    }

    public void setNumEpochs(LearnerParameter numEpochs) {
        this.numEpochs = numEpochs;
    }

    public LearnerParameter getNumInputs() {
        return numInputs;
    }

    public void setNumInputs(LearnerParameter numInputs) {
        this.numInputs = numInputs;
    }

    public LearnerParameter getNumOutputs() {
        return numOutputs;
    }

    public void setNumOutputs(LearnerParameter numOutputs) {
        this.numOutputs = numOutputs;
    }

    public LearnerParameter getNumHiddenNodes() {
        return numHiddenNodes;
    }

    public void setNumHiddenNodes(LearnerParameter numHiddenNodes) {
        this.numHiddenNodes = numHiddenNodes;
    }
}
