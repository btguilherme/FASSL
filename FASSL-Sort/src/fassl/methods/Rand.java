/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.methods;

import fassl.utils.Enumerations;
import fassl.utils.IO;
import fassl.utils.SystemUserDir;
import fassl.utils.Timer;
import java.io.IOException;
import java.util.UUID;
import weka.core.Debug;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class Rand {

    private final Instances file;
    private final String method;
    private final String fileName;
    private final String savePath;
    
    public Rand(Instances file, String fileName, String method, String savePath) throws IOException{
        this.file = file;
        this.fileName = fileName;
        this.method = method;
        this.savePath = savePath;
        
        makeItHappen();
    }
    
    protected void makeItHappen() throws IOException{
        Timer timer = new Timer();
        
        file.randomize(new Debug.Random());
        
        String sortTime = String.valueOf(timer.getTime());
        String uuid = String.valueOf(UUID.randomUUID());
        IO.saveConcat("#" + uuid, savePath + "_sort_time_" + method + ".txt");
        IO.saveConcat(sortTime, savePath + "_sort_time_" + method + ".txt");
        
        save();
    }

    protected void save() throws IOException {
        
        String saveArffPath = SystemUserDir.newPath(Enumerations.PROGRSORT.value).
                    concat(Enumerations.SEP.value).concat("arff-files-sorted").
                    concat(Enumerations.SEP.value).concat(fileName).concat("_").
                    concat(method).concat(".arff");
        
        IO.save(file, saveArffPath);
        
    }
    
}
