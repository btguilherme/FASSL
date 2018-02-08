/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.unsupervised;

import fassl.utils.IO;
import fassl.utils.Timer;
import java.util.UUID;
import weka.classifiers.Classifier;
import weka.classifiers.CollectiveEvaluation;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class Unsupervised implements IUnsupervised{
    
    protected final Instances z2i;
    protected final Instances z2ii;
    protected final Instances z3;
    protected Classifier classifier;
    protected double acc;
    protected final boolean firstIteration;
    protected final String savePath;
    protected double precision;
    protected double recall;
    protected double fmeasure;
    protected double roc;
    
    public Unsupervised(Instances z2i, Instances z2ii, Instances z3, 
            String savePath, boolean firstIteration) throws Exception{
        
        this.z2i = z2i;
        this.z2ii = z2ii;
        this.z3 = z3;
        this.savePath = savePath;
        this.firstIteration = firstIteration;
        
        makeItHappen();
    }

    @Override
    public void makeItHappen() throws Exception {
        Timer timer = new Timer();
        train();
        String trainTime = String.valueOf(timer.getTime());
        
        timer = new Timer();
        classify();
        String testTime = String.valueOf(timer.getTime());
        
        String classifierType = getClass().getSimpleName();
        
        if(firstIteration){
            String uuid = String.valueOf(UUID.randomUUID());
            IO.save("#" + uuid, savePath + "_acc_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_train_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_test_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_precision_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_recall_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_fmeasure_" + classifierType + ".txt");
            IO.save("#" + uuid, savePath + "_roc_" + classifierType + ".txt");
        }
        
        IO.saveConcat(String.valueOf(acc), savePath + "_acc_" + classifierType + ".txt");
        IO.saveConcat(trainTime, savePath + "_train_" + classifierType + ".txt");
        IO.saveConcat(testTime, savePath + "_test_" + classifierType + ".txt");
        IO.saveConcat(String.valueOf(precision), savePath + "_precision_" + classifierType + ".txt");
        IO.saveConcat(String.valueOf(recall), savePath + "_recall_" + classifierType + ".txt");
        IO.saveConcat(String.valueOf(fmeasure), savePath + "_fmeasure_" + classifierType + ".txt");
        IO.saveConcat(String.valueOf(roc), savePath + "_roc_" + classifierType + ".txt");
    }

    @Override
    public void train() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void classify() throws Exception {
        CollectiveEvaluation eval = new CollectiveEvaluation(z2i);
        eval.evaluateModel(classifier, z3);
        acc = eval.pctCorrect();
        precision = eval.weightedPrecision();
        recall = eval.weightedRecall();
        fmeasure = eval.weightedFMeasure();
        roc = eval.weightedAreaUnderROC();
        
        //With high precision but low recall, you classifier is extremely accurate, 
        //but it misses a significant number of instances that are difficult to classify. This is not very useful.
    }
    
    public Classifier getClassifier() {
        return classifier;
    }

    
}
