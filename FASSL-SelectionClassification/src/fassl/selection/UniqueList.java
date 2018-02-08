/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.selection;

import fassl.utils.IO;
import java.io.File;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class UniqueList {

    private boolean outOfSamples = false;
    private Instances z2iSingle;
    private Instances z2iiSingle;
    private Instances z3;
    
    public Instances selectSamplesUniqueList(Instances ret, Instances file, 
            int numSamples) {

        //outOfSamples = false;

        if(numSamples > file.numInstances()){
            numSamples = file.numInstances();
            outOfSamples = true;
        }

        for (int i = 0; i < numSamples; i++)
            ret.add(file.instance(i));

        for (int i = 0; i < numSamples; i++)
            file.delete(0);

        return ret;   
    }
    
    public void loadsFilesUniqueList(File file, String basesSavePath) 
            throws Exception {
        
        z2iSingle = IO.open(file.getAbsolutePath());
        z2iSingle.setClassIndex(z2iSingle.numAttributes() - 1);
        z2iiSingle = IO.open(file.getAbsolutePath().replace("_z2i_", "_z2ii_"));
        z2iiSingle.setClassIndex(z2iiSingle.numAttributes() - 1);
        z3 = IO.open(basesSavePath + file.getName().split("_z2")[0] + "_z3.arff");
        z3.setClassIndex(z3.numAttributes() - 1);
    }

    public boolean isOutOfSamples() {
        return outOfSamples;
    }

    public Instances getZ2iSingle() {
        return z2iSingle;
    }

    public Instances getZ2iiSingle() {
        return z2iiSingle;
    }

    public Instances getZ3() {
        return z3;
    }
    
    
    
}
