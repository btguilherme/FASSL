/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.sort;

import fassl.methods.AFC;
import fassl.methods.Clu;
import fassl.methods.RDS;
import fassl.methods.RDBS;
import fassl.methods.Rand;
import fassl.utils.Enumerations;
import fassl.utils.ReadProperties;
import java.io.File;
import java.io.IOException;
import weka.core.Instances;
import fassl.utils.IO;
import fassl.utils.SystemUserDir;

/**
 *
 * @author guilherme
 */
public class FASSLSort {

    /**
     * @param args the command line arguments
     */
    private static String[] sort;
    private static int xNumClasses;
    
    public static void main(String[] args) throws IOException, Exception {
        
        ReadProperties rp = new ReadProperties();
        sort = rp.getSort();
        xNumClasses = rp.getxNumClasses();
        
        String basesSavePath = rp.getBasesSavePath();
        
        File[] filesZ2i = IO.getFiles("_z2i.arff", basesSavePath);
        File[] filesZ2ii = IO.getFiles("_z2ii.arff", basesSavePath);
        
        for (File file : filesZ2i) {
            System.err.print("Sorting Z2'... ");
            sort(file, 0);
            System.err.println("End");
        }
        
        for (File file : filesZ2ii) {
            System.err.print("Sorting Z2''... ");
            sort(file, 1);
            System.err.println("End");
        }
        
    }

    private static void sort(File _file, int labOrUnlab) throws Exception {
        
        Instances file = IO.open(_file.getAbsolutePath());
        file.setClassIndex(file.numAttributes() - 1);

        for (String method : sort) {
            
            String save = SystemUserDir.newPath(Enumerations.PROGRSORT.value).
                    split(Enumerations.PROGRSORT.value)[0].concat(Enumerations.SEP.value).
                    concat("txt-files").concat(Enumerations.SEP.value).concat(_file.getName().
                            split(".arff")[0].concat("_").concat(method));
            
            switch(method){
                case "AFC":
                    new AFC(file, file.numClasses() * xNumClasses,
                            _file.getName().split(".arff")[0], method, save);
                    break;
                case "Clu":
                    new Clu(file, file.numClasses() * xNumClasses,
                            _file.getName().split(".arff")[0], method, save);
                    break;
                case "RDBS":
                    new RDBS(file, file.numClasses() * xNumClasses,
                            _file.getName().split(".arff")[0], method, save);
                    break;
                case "RDS":
                    new RDS(file, file.numClasses() * xNumClasses,
                            _file.getName().split(".arff")[0], method, save);
                    break;
                case "Rand":
                    new Rand(file, _file.getName().split(".arff")[0], method, save);
                    break;
            }
        }
    }
    
}
