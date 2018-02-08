/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.supervised;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class RF extends Supervised{
    
    public RF(Instances z2i, Instances z3, String savePath, 
            boolean firstIteration) throws Exception {
        
        super(z2i, z3, savePath, firstIteration);
    }

    @Override
    public void train() throws Exception {
        classifier = new RandomForest();
        classifier.buildClassifier(z2i);
        
    }

}
