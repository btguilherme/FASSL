/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.main;

import fassl.unsupervised.CollectiveWrapperOPF;
import fassl.unsupervised.CollectiveWrapperSVM;
import fassl.unsupervised.YATSISVM;
import fassl.unsupervised.CollectiveWrapperRF;
import fassl.unsupervised.YATSIRF;
import fassl.unsupervised.YATSIOPF;
import fassl.supervised.RF;
import fassl.supervised.OPF;
import fassl.supervised.SVM;
import fassl.selection.MultipleLists;
import fassl.selection.UniqueList;
import fassl.utils.IO;
import fassl.utils.ReadProperties;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class FASSLSelectionClassification {
    
    private static final String SEP = File.separator;
    private static String[] classifiers;
    private static int xNumClasses;
    private static Instances z2iSingle;
    private static Instances z2iiSingle;
    private static Instances[] z2iMultiple;
    private static Instances[] z2iiMultiple;
    private static Instances z3;
    private static String basesSavePath;
    private static Classifier classificador;
    private static String savePath;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, Exception {
        
        ReadProperties rp = new ReadProperties();
        classifiers = rp.getClassifiers();
        xNumClasses = rp.getxNumClasses();
        basesSavePath = rp.getBasesSavePath();
        String splitsSortedPath = rp.getSplitsSorted();
        String methodsSort[] = rp.getSort();
        
        savePath = System.getProperty("user.dir").concat(SEP).
                concat("arff-files-iterations").concat(SEP);
        
        File[] files = IO.getFiles(".arff", splitsSortedPath);
        Set<String> uuids = new HashSet<>();
        
        for (File file : files)
            uuids.add(file.getName().split("@")[0]);
        
        for (String uuid : uuids) {
            for (String methodSort : methodsSort) {
                boolean did = false;
                for (int i = 0; i < files.length; i++) {
                    switch(methodSort){
                        case "AFC":
                        case "Rand":
                            if(files[i].getName().startsWith(uuid) && files[i].
                                    getName().contains(methodSort) && files[i].
                                            getName().contains("_z2i_"))
                                routineSingleList(files[i], methodSort);
                            break;
                        case "Clu":
                            if(files[i].getName().startsWith(uuid) && files[i].
                                    getName().contains(methodSort) && files[i].
                                            getName().contains("_z2i_") && !did){
                        
                                routineMultipleListsClu(uuid, files, methodSort);
                                did = true;
                            }
                            break;
                        case "RDBS":
                        case "RDS":
                            if(files[i].getName().startsWith(uuid) && files[i].
                                    getName().contains(methodSort) && files[i].
                                            getName().contains("_z2i_") && !did){
                                
                                routineMultipleLists(uuid, files, methodSort);
                                did = true;
                            }
                            break;
                    }
                }
            }
        }
    }
    
    private static void routineSingleList(File file, String method) throws Exception {
        
        UniqueList ul = new UniqueList();
        
        ul.loadsFilesUniqueList(file, basesSavePath);
        
        z2iSingle = ul.getZ2iSingle();
        z2iiSingle = ul.getZ2iiSingle();
        z3 = ul.getZ3();
        
        Instances z2i = new Instances(z2iSingle);
        z2i.delete();
        Instances z2ii = new Instances(z2iiSingle);
        z2ii.delete();
        
        String save = System.getProperty("user.dir").split("EAL-SelectionClassif"
                + "ication")[0].concat("txt-files").concat(SEP).concat(file.
                        getName().split("_z2")[0].concat("_").concat(method));

        int contIt = 0;
        boolean firstIteration = true;
        do {
            
            z2ii = ul.selectSamplesUniqueList(z2ii, z2iiSingle,
                    z2iiSingle.numClasses());
            
            z2i = ul.selectSamplesUniqueList(z2i, z2iSingle,
                    z2iSingle.numClasses() * xNumClasses);
            
            IO.save(z2i, savePath + file.getName().split(".arff")[0].concat("_it_").
                    concat(String.valueOf(contIt)).concat(".arff"));
            IO.save(z2ii, savePath + file.getName().replace("z2i", "z2ii").split(".arff")[0].
                    concat("_it_").concat(String.valueOf(contIt)).concat(".arff"));
            
            makesClassification(z2i, z2ii, save, firstIteration);
            
            firstIteration = false;
            
            contIt++;
        
        } while (!ul.isOutOfSamples());
    }

    private static void routineMultipleLists(String uuid, File[] files, 
            String method) throws Exception {
        
        MultipleLists ml = new MultipleLists();
        
        ml.loadsFilesMultipleLists(uuid, files, basesSavePath, method);
        
        z2iMultiple = ml.getZ2iMultiple();
        z2iiMultiple = ml.getZ2iiMultiple();
        z3 = ml.getZ3();
        
        Instances z2i = new Instances(z2iMultiple[0]);
        z2i.delete();
        Instances z2ii = new Instances(z2iiMultiple[0]);
        z2ii.delete();

        int contIt = 0;
        
        for (Instances z2iMultiple1 : z2iMultiple)
            z2i.add(z2iMultiple1.firstInstance()); //raizes
        
        for (int i = 0; i < z2iiMultiple.length / 2; i++)
            z2ii.add(z2iiMultiple[i].firstInstance());
        
        IO.save(z2i, savePath + ml.getFileName().split("lista_")[0].concat("it_").
                concat(String.valueOf(contIt)).concat(".arff"));
        
        IO.save(z2ii, savePath + ml.getFileName().replace("z2i", "z2ii").
                split("lista_")[0].concat("it_").concat(String.valueOf(contIt)).
                concat(".arff"));
        
        contIt++;
        
        String save = System.getProperty("user.dir").split("EAL-SelectionClassif"
                + "ication")[0].concat("txt-files").concat(SEP).concat(ml.
                        getFileName().split("_z2")[0].concat("_").concat(method));
        
        makesClassification(z2i, z2ii, save, true);
        
        do {
            z2ii = ml.selectSamplesMultipleLists(classificador, z2ii, 
                    z2iiMultiple, z2iiMultiple[0].numClasses(), 1);
            
            z2i = ml.selectSamplesMultipleLists(classificador, z2i, 
                    z2iMultiple, z2iMultiple[0].numClasses() * xNumClasses, 0);
            
            IO.save(z2i, savePath + ml.getFileName().split("lista_")[0].concat("it_").
                    concat(String.valueOf(contIt)).concat(".arff"));

            IO.save(z2ii, savePath + ml.getFileName().replace("z2i", "z2ii").
                    split("lista_")[0].concat("it_").concat(String.valueOf(contIt)).
                    concat(".arff"));
            
            makesClassification(z2i, z2ii, save, false);
            
            contIt++;
            
        } while (!ml.isOutOfSamples());
    }
    
    private static void routineMultipleListsClu(String uuid, File[] files, 
            String method) throws Exception {
        
        MultipleLists ml = new MultipleLists();
        
        ml.loadsFilesMultipleLists(uuid, files, basesSavePath, method);

        z2iMultiple = ml.getZ2iMultiple();
        z2iiMultiple = ml.getZ2iiMultiple();
        z3 = ml.getZ3();
        
        Instances z2i = new Instances(z2iMultiple[0]);
        z2i.delete();
        Instances z2ii = new Instances(z2iiMultiple[0]);
        z2ii.delete();
        
        String save = System.getProperty("user.dir").split("EAL-SelectionClassif"
                + "ication")[0].concat("txt-files").concat(SEP).concat(ml.
                        getFileName().split("_z2")[0].concat("_").concat(method));
        
        int contIt = 0;
        boolean firstIteration = true;
        
        do {
            z2ii = ml.selectSamplesMultipleListsClu(z2ii, z2iiMultiple, 
                    z2iiMultiple[0].numClasses(), 1);
            
            z2i = ml.selectSamplesMultipleListsClu(z2i, z2iMultiple, 
                    z2iMultiple[0].numClasses() * xNumClasses, 0);
            
            IO.save(z2i, savePath + ml.getFileName().split("lista_")[0].concat("it_").
                    concat(String.valueOf(contIt)).concat(".arff"));

            IO.save(z2ii, savePath + ml.getFileName().replace("z2i", "z2ii").
                    split("lista_")[0].concat("it_").concat(String.valueOf(contIt)).
                    concat(".arff"));
            
            makesClassification(z2i, z2ii, save, firstIteration);
            
            firstIteration = false;
            
            contIt++;
        } while (!ml.isOutOfSamples());
    }
    
    private static void makesClassification(Instances z2i, Instances z2ii, 
            String savePath, boolean firstIteration) throws Exception {
        
        for (String classifier : classifiers) {
            
            System.err.print("Aplicando o classificador " + classifier + "... ");
            
            switch (classifier) {
                //supervised
                case "SVM":
                    SVM svm = new SVM(z2i, z3, savePath, firstIteration);
                    classificador = svm.getClassifier();
                    break;
                case "RF":
                    RF rf = new RF(z2i, z3, savePath, firstIteration);
                    classificador = rf.getClassifier();
                    break;
                case "OPF":
                    OPF opf = new OPF(z2i, z3, savePath, firstIteration);
                    classificador = opf.getClassifier();
                    break;
                //semisupervised
                case "YSVM":
                    YATSISVM ysvm = new YATSISVM(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = ysvm.getClassifier();
                    break;
                case "YRF":
                    YATSIRF yrf = new YATSIRF(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = yrf.getClassifier();
                    break;
                case "YOPF":
                    YATSIOPF yopf = new YATSIOPF(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = yopf.getClassifier();
                    break;
                case "WSVM":
                    CollectiveWrapperSVM wsvm = new CollectiveWrapperSVM(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = wsvm.getClassifier();
                    break;
                case "WRF":
                    CollectiveWrapperRF wrf = new CollectiveWrapperRF(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = wrf.getClassifier();
                    break;
                case "WOPF":
                    CollectiveWrapperOPF wopf = new CollectiveWrapperOPF(z2i, z2ii, z3, savePath, firstIteration);
                    classificador = wopf.getClassifier();
                    break;
            }
            
            System.err.println("Fim");
        }
    }

}