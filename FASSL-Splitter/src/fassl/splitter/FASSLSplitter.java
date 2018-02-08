/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.splitter;

import fassl.utils.IO;
import fassl.utils.ReadProperties;
import java.util.UUID;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class FASSLSplitter {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    private static String arffPath;
    private static int nSplits;
    private static double pct;
    private static String baseSavePath;
    private static String fileName = "";
    
    public static void main(String[] args) throws Exception {
                
        ReadProperties rp = new ReadProperties();
        arffPath = rp.getArffFile();
        nSplits = rp.getnSplits();
        pct = rp.getPct();
        baseSavePath = rp.getBasesSavePath();
        
        for (int i = 0; i < nSplits; i++) {
            System.err.print("Início do split #"+(i+1)+"... ");
            
            String uniqueID = UUID.randomUUID().toString();
            
            Instances data = IO.open(arffPath);
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);

            if (fileName.equals("")){
                fileName = arffPath.split(".arff")[0].split("/")[arffPath.
                        split(".arff")[0].split("/").length - 1];
            }
            
            Splitter split = new Splitter();
            split.split(data, pct);

            Instances z2i = split.getZ2i();
            Instances z2ii = split.getZ2ii();
            Instances z3 = split.getZ3();
            
            String pathZ2i = baseSavePath + uniqueID + "@" + fileName + "_split_"+i+"_z2i.arff";
            String pathZ2ii = baseSavePath + uniqueID + "@" + fileName + "_split_"+i+"_z2ii.arff";
            String pathZ3 = baseSavePath + uniqueID + "@" + fileName + "_split_"+i+"_z3.arff";

            IO.save(z2i, pathZ2i);
            IO.save(z2ii, pathZ2ii);
            IO.save(z3, pathZ3);
            
            System.err.println("Fim");
        }
        
    }
    
}
