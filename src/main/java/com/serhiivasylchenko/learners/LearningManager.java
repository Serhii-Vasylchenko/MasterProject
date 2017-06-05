package com.serhiivasylchenko.learners;

import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.learning.DL4JConfiguration;
import com.serhiivasylchenko.persistence.learning.Learner;
import com.serhiivasylchenko.utils.Constants;
import org.apache.log4j.Logger;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;

/**
 * @author Serhii Vasylchenko
 */
public class LearningManager {

    private static final Logger LOGGER = Logger.getLogger(LearningManager.class);

    public static void train(System system, Learner learner) {
        if (learner != null) {
            DL4JConfiguration configuration = (DL4JConfiguration) learner.getConfiguration();

            int seed = configuration.getSeed().getIntValue();
            double learningRate = configuration.getLearningRate().getFloatValue();
            int batchSize = configuration.getBatchSize().getIntValue();
            int nEpochs = configuration.getNumEpochs().getIntValue();

            int numInputs = 4;
            int numOutputs = 3;
            int numHiddenNodes = configuration.getNumHiddenNodes().getIntValue();

            final String filenameTrain = Constants.datasetPath + system.getSystemTrainingConf().getPathToTrainingSet();
            final String filenameTest = Constants.datasetPath + system.getSystemTrainingConf().getPathToTestSet();


            try {
                DataSet trainingData = readCSVDataset(filenameTrain, batchSize, numInputs, numOutputs);
                DataSet testData = readCSVDataset(filenameTest, batchSize, numInputs, numOutputs);

                //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
                DataNormalization normalizer = new NormalizerStandardize();
                normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
                normalizer.transform(trainingData);     //Apply normalization to the training data
                normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set


                MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                        .seed(seed)
                        .iterations(1)
                        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                        .learningRate(learningRate)
                        .updater(Updater.NESTEROVS).momentum(0.9)
                        .regularization(true).l2(1e-4)
                        .list()
                        .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.RELU)
                                .build())
                        .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                                .nIn(numHiddenNodes).nOut(numOutputs).build())
                        .pretrain(false)
                        .backprop(true)
                        .build();

                MultiLayerNetwork model = new MultiLayerNetwork(conf);
                model.init();
                model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates

                model.fit(trainingData);

                //evaluate the model on the test set
                LOGGER.info("Evaluate model....");
                Evaluation eval = new Evaluation(3);
                INDArray output = model.output(testData.getFeatureMatrix());

                eval.eval(testData.getLabels(), output);
                LOGGER.info(eval.stats());

                // Save the model
                File learnerModelFile = new File(Constants.learnerModelPath + learner.getLearnerModelName());
                if (!learnerModelFile.exists()) {
                    if (learnerModelFile.createNewFile()) {
                        LOGGER.info("Learner model file '" + learner.getLearnerModelName() + "' created");
                    } else {
                        LOGGER.info("Learner model file '" + learner.getLearnerModelName() + "' already exists");
                    }
                }

                boolean saveUpdater = true; //Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you want to train your network more in the future
                ModelSerializer.writeModel(model, learnerModelFile, saveUpdater);

            } catch (IOException | InterruptedException e) {
                LOGGER.error(e);
            }
        }
    }

    private static DataSet readCSVDataset(
            String csvFileClasspath, int batchSize, int labelIndex, int numClasses)
            throws IOException, InterruptedException {

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(csvFileClasspath)));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, labelIndex, numClasses);
        return iterator.next();
    }
}
