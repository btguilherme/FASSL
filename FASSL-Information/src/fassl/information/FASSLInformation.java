/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.information;

import fassl.utils.IO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guilherme
 */
public class FASSLInformation {

    /**
     * @param args the command line arguments
     */
    
    private static final String SEP = File.separator;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String txtFilesPath = System.getProperty("user.dir").split("EAL-Informat"
                + "ion")[0].concat("txt-files").concat(SEP);
        
        File[] txtFiles = IO.getFiles("txt", txtFilesPath);
        
        String[] sortMethods = {"AFC", "Clu", "RDBS", "RDS", "Rand"};
        String[] classifiers = {"_SVM", "_RF", "_OPF", "_YATSISVM", "_YATSIRF", 
            "_YATSIOPF", "_CollectiveWrapperSVM", "_CollectiveWrapperRF", "_CollectiveWrapperOPF"};
        String[] mensurations = {"acc", "train", "test", "precision", "recall", "fmeasure", "roc"};
        
        
        for (String mensuration : mensurations) {
            for (String classifier : classifiers) {
                for (String sortMethod : sortMethods) {
                    List<File> f = new ArrayList<>();
                    for (File txtFile : txtFiles) {
                        String name = txtFile.getName();
                        if(name.contains(mensuration) && name.contains(classifier) && name.contains(sortMethod)){
                            f.add(txtFile);
                        }
                    }
                    System.err.print("Calculando para "+mensuration +""+ classifier +"_"+ sortMethod+" ... ");
                    calc(f, txtFilesPath);
                    System.err.println("Fim");
                }
            }
        }
        
    }

    private static void calc(List<File> f, String txtFilesPath) throws 
            FileNotFoundException, IOException {
        
        int nivela = Integer.MAX_VALUE;
        List<List<Double>> accs = new ArrayList<>();

        for (File file : f) {    
            List<String> conteudo = IO.openTxt(file.getAbsolutePath());

            if(conteudo.size() - 1 < nivela)
                nivela = conteudo.size() - 1;

            List<Double> aux = new ArrayList<>();
            for (int i = 0; i < conteudo.size(); i++){ 
                if(!conteudo.get(i).startsWith("#")){
                    aux.add(Double.valueOf(conteudo.get(i)));
                }
            }
            accs.add(aux);
        }


        double[] medias = new double[nivela];
        for (int i = 0; i < nivela; i++) {    
            double soma = 0;
            for (int j = 0; j < accs.size(); j++) {
                soma += accs.get(j).get(i);
            }
            medias[i] = soma/accs.size();
        }

        double[] desvios = new double[nivela];
        for (int i = 0; i < nivela; i++) {
            double soma = 0;
            for (int j = 0; j < accs.size(); j++) {
                soma += Math.pow(accs.get(j).get(i) - medias[i], 2);
            }
            desvios[i] = Math.sqrt(soma/accs.size());
        }

        String dat = "";
        for (int i = 0; i < nivela; i++) {
            if(i == nivela - 1){
                dat = dat.concat((i+1)+" "+medias[i]+" "+desvios[i]);
            }else{
                dat = dat.concat((i+1)+" "+medias[i]+" "+desvios[i]).concat("\n");
            }
        }

        String[] aux = f.get(0).getName().split("split_")[1].split("_");
        String secPart = "";
        for (int i = 1; i < aux.length; i++) {
            if(i == aux.length - 1){
                secPart = secPart.concat(aux[i]);
            }else{
                secPart = secPart.concat(aux[i]).concat("_");
            }

        }

        String path = txtFilesPath.replace("txt", "dat") + f.get(0).getName().split("split_")[0] + 
                secPart.replace(".txt", ".dat");
        
        IO.save(dat, path);
    }
    
}
