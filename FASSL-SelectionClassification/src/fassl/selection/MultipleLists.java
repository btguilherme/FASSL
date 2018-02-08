/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.selection;

import fassl.utils.IO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class MultipleLists {
    
    private boolean outOfSamples;
    private Instances[] z2iMultiple;
    private Instances[] z2iiMultiple;
    private Instances z3;
    private String fileName;
    
    public Instances selectSamplesMultipleListsClu(Instances ret, 
            Instances[] files, int numSamples, int labOrUnlab) throws Exception {
        
        int added = 0, indexControl = 0, loopsWithoutRemove = 0;
        
        do {
            
            if(files[indexControl].numInstances() > 0){
                ret.add(files[indexControl].firstInstance());
                files[indexControl].delete(0);
                added++;
                loopsWithoutRemove = 0;
            } else
                loopsWithoutRemove++;
            
            if(indexControl == files.length - 1)
                indexControl = 0;
            else
                indexControl++;
            
            if(added == numSamples)
                break;
            
            if (loopsWithoutRemove == files.length + 1){
                outOfSamples = true;
                break;
            }
            
        } while (true);
        
        if(labOrUnlab == 0)
            z2iMultiple = files;
        else if(labOrUnlab == 1)
            z2iiMultiple = files;
        
        return ret;   
    }
    
    
    public Instances selectSamplesMultipleLists(Classifier classificador,
            Instances ret, Instances[] files, int numSamples, int labOrUnlab) 
            throws Exception {
        
        int added = 0, indexControl = 0, toRemove, loopsWithoutRemove = 0;
        
        do {
            
            double rootValue = classificador.classifyInstance(files[indexControl].
                    firstInstance());
            toRemove = 0;
            
            for (int i = 1; i < files[indexControl].numInstances(); i++) {
                double instValue = classificador.classifyInstance(files[indexControl].
                        instance(i));
                if((rootValue != instValue) || (i == files[indexControl].
                        numInstances() - 1)){
                    ret.add(files[indexControl].instance(i));
                    toRemove = i;
                    added++;
                    break;
                }       
            }              
            if(toRemove != 0){
                Instances aux = new Instances(files[indexControl]);
                aux.delete();
                for (int i = 0; i < files[indexControl].numInstances(); i++) {
                    if(i == toRemove){/*do nothing*/}
                    else
                        aux.add(files[indexControl].instance(i));
                }
                files[indexControl] = aux;
                loopsWithoutRemove = 0;
            } else
                loopsWithoutRemove++;
            
            if(added == numSamples)
                break;
            
            if (loopsWithoutRemove == files.length + 1){
                outOfSamples = true;
                break;
            }
            
            if(indexControl == files.length - 1)
                indexControl = 0;
            else
                indexControl++;
            
        } while (true);
        
        if(labOrUnlab == 0)
            z2iMultiple = files;
        else if(labOrUnlab == 1)
            z2iiMultiple = files;
        
        return ret;   
    }
    
    public void loadsFilesMultipleLists(String uuid, File[] files,
            String basesSavePath, String method) throws Exception {
        
        List<Instances> z2iList = new ArrayList<>();
        List<Instances> z2iiList = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        
        boolean openedZ3 = false;
        
        for (int i = 0; i < files.length; i++) {
            if(files[i].getName().startsWith(uuid) && files[i].getName().
                       contains(method) && files[i].getName().contains("_z2i_")
                    && files[i].getName().contains("lista")){
                
                fileName = files[i].getName();
                
                indexes.add(Integer.valueOf(files[i].getName().split("_lista_")[1].
                        split(".arff")[0]));
                
                Instances aux = IO.open(files[i].getAbsolutePath());
                aux.setClassIndex(aux.numAttributes() - 1);
                z2iList.add(aux);
                
                aux = IO.open(files[i].getAbsolutePath().replace("_z2i_", 
                        "_z2ii_"));
                aux.setClassIndex(aux.numAttributes() - 1);
                z2iiList.add(aux);
         
                if(!openedZ3){
                    z3 = IO.open(basesSavePath + files[i].getName().
                            split("_z2")[0] + "_z3.arff");
                    z3.setClassIndex(z3.numAttributes() - 1);
                    openedZ3 = true;
                }       
            }    
        }
        
        z2iMultiple = new Instances[indexes.size()];
        z2iiMultiple = new Instances[indexes.size()];
        
        for (int i = 0; i < indexes.size(); i++) {
            z2iMultiple[indexes.get(i)] = z2iList.get(i);
            z2iiMultiple[indexes.get(i)] = z2iiList.get(i);
        }
    }

    public boolean isOutOfSamples() {
        return outOfSamples;
    }

    public Instances[] getZ2iMultiple() {
        return z2iMultiple;
    }

    public Instances[] getZ2iiMultiple() {
        return z2iiMultiple;
    }

    public Instances getZ3() {
        return z3;
    }

    public String getFileName() {
        return fileName;
    }
    
}
