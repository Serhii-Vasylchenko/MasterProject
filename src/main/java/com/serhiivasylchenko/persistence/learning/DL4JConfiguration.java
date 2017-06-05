package com.serhiivasylchenko.persistence.learning;

import com.serhiivasylchenko.utils.FieldType;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */

@Entity
@NamedQueries(
        @NamedQuery(name = DL4JConfiguration.NQ_BY_LEARNER, query = "select x FROM DL4JConfiguration x WHERE x.learner = :learner")
)
public class DL4JConfiguration extends AbstractConfiguration {

    private static final long serialVersionUID = -9028081644961906615L;

    private static final Logger LOGGER = Logger.getLogger(DL4JConfiguration.class);

    public static final String NQ_BY_LEARNER = "nq.configuration.get.by.learner";

    private static final Integer NUM_PARAMETERS = 5; // number of configurable parameters

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
    public void initDefault() {
        this.seed = new LearnerParameter(this, "Seed", "", FieldType.INT_NUMBER, true);
        this.seed.setIntValue(123);

        this.learningRate = new LearnerParameter(this, "Learning Rate", "", FieldType.FLOAT_NUMBER, true);
        this.learningRate.setFloatValue(0.01f);

        this.batchSize = new LearnerParameter(this, "Batch Size", "", FieldType.INT_NUMBER, true);
        this.batchSize.setIntValue(50);

        this.numEpochs = new LearnerParameter(this, "Number of Epochs", "", FieldType.INT_NUMBER, true);
        this.numEpochs.setIntValue(30);

        this.numInputs = new LearnerParameter(this, "Number of inputs", "", FieldType.INT_NUMBER, false);
        this.numInputs.setFieldType(FieldType.INT_NUMBER);

        this.numOutputs = new LearnerParameter(this, "Number of target fields", "", FieldType.INT_NUMBER, false);
        this.numOutputs.setIntValue(1);

        this.numHiddenNodes = new LearnerParameter(this, "Number of hidden nodes", "", FieldType.INT_NUMBER, true);
        this.numHiddenNodes.setIntValue(20);
    }

    @Override
    public List<LearnerParameter> getConfigurableFields() {
        List<LearnerParameter> parameters = new ArrayList<>(NUM_PARAMETERS);

        parameters.add(this.seed);
        parameters.add(this.learningRate);
        parameters.add(this.batchSize);
        parameters.add(this.numEpochs);
        parameters.add(this.numHiddenNodes);

        return parameters;
    }

    /**
     * This function expects values EXACTLY in the same order, as in getConfigurableFields()
     *
     * @param fieldValues field values as string array
     */
    @Override
    public void setConfigurableFields(List<String> fieldValues) {
        if (fieldValues.size() == NUM_PARAMETERS) {
            this.seed.setIntValue(Integer.valueOf(fieldValues.get(0)));
            this.learningRate.setFloatValue(Float.valueOf(fieldValues.get(1)));
            this.batchSize.setIntValue(Integer.valueOf(fieldValues.get(2)));
            this.numEpochs.setIntValue(Integer.valueOf(fieldValues.get(3)));
            this.numHiddenNodes.setIntValue(Integer.valueOf(fieldValues.get(4)));
        } else {
            LOGGER.error("Number of parameters to set is not equal to " + NUM_PARAMETERS);
        }
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
