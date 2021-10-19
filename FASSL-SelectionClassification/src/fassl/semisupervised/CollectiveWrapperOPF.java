/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.semisupervised;

import weka.classifiers.collective.meta.CollectiveWrapper;
import weka.classifiers.opf.OPF;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class CollectiveWrapperOPF extends Semisupervised{
    
    public CollectiveWrapperOPF(Instances z2i, Instances z2ii, Instances z3, 
            String savePath, boolean firstIteration) throws Exception {
        
        super(z2i, z2ii, z3, savePath, firstIteration);
    }

    @Override
    public void train() throws Exception {
        CollectiveWrapper wrapper = new CollectiveWrapper();
        wrapper.setClassifier(new OPF());
        for (Instance instance : z3) {
            z2ii.add(instance);
        }
        wrapper.buildClassifier(z2i, z2ii);
        
        classifier = wrapper;
    }
    
}
