package com.serhiivasylchenko.learners;

import com.serhiivasylchenko.utils.DataType;

/**
 * @author Serhii Vasylchenko
 */
public interface ILearner {
    void setTrainingData(String fileName);

    void setTestData(String fileName);

    void setDataType(DataType dataType);

    void setConfiguration(AbstractConfiguration configuration);

    void train();

    AbstractResult calculateResult(AbstractInput input);
}