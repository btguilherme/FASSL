/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.unsupervised;

import weka.classifiers.collective.meta.YATSI;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class YATSIRF extends Unsupervised{
    
    public YATSIRF(Instances z2i, Instances z2ii, Instances z3, String savePath,
            boolean firstIteration) throws Exception {
        
        super(z2i, z2ii, z3, savePath, firstIteration);
    }

    @Override
    public void train() throws Exception {
        YATSI yatsi = new YATSI();
        yatsi.setClassifier(new RandomForest());
        yatsi.setKNN(10);
        yatsi.setNoWeights(true);
        yatsi.buildClassifier(z2i, z2ii);
        
        classifier = yatsi.getClassifier();
    }
    
    
    
}
