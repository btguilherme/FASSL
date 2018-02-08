/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.methods;

import fassl.utils.IO;
import fassl.utils.InstancesManipulation;
import fassl.utils.MapUtil;
import fassl.utils.Timer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.KDTree;

/**
 *
 * @author guilherme
 */
public class AFC {

    protected final Instances file;
    protected final int nClusters;
    protected final Instances fileWithoutClass;
    protected final List<Double> distances;
    protected Instances deletedRootsWithClass;
    protected SimpleKMeans clusterer;
    protected Instances sorted;
    protected String fileName;
    protected String method;
    protected final String savePath;
    
    public AFC(Instances file, int nClusters, String fileName, String method, 
            String savePath) throws Exception{
        
        this.distances = new ArrayList<>();
        this.file = file;
        this.nClusters = nClusters;
        this.fileName = fileName;
        this.method = method;
        this.savePath = savePath;
        
        this.fileWithoutClass = InstancesManipulation.
                removeAtributoClasse(this.file);
        
        makeItHappen();
    }
    
    public void makeItHappen() throws Exception {

        Timer timer = new Timer();
        
        int kVizinhos = file.numClasses()/2;
        cluster();
        Instances raizes = roots(clusterer.getClusterCentroids());
        removeRootsFromFiles(raizes);

        Instances amostrasDeFronteira = neighbors(clusterer, kVizinhos);
        Instances amostrasDeFronteiraOrdenada = sort(amostrasDeFronteira);
        sorted = rootsPlusSorted(amostrasDeFronteiraOrdenada);
        
        String sortTime = String.valueOf(timer.getTime());
        
        String uuid = String.valueOf(UUID.randomUUID());
        IO.saveConcat("#" + uuid, savePath + "_sort_time_" + method + ".txt");
        IO.saveConcat(sortTime, savePath + "_sort_time_" + method + ".txt");
        
        save();
    }

    protected void save() throws IOException {
        
        String saveArffPath = System.getProperty("user.dir").
                    concat(File.separator).concat("arff-files-sorted").
                    concat(File.separator).concat(fileName).concat("_").
                    concat(method).concat(".arff");
        
        IO.save(sorted, saveArffPath);
        
    }

    
    
    protected void cluster() {
        clusterer = new SimpleKMeans();
        try {
            String[] options = weka.core.Utils.splitOptions(
                    "-N " + nClusters + " -I 500 -S 10 -O");
            
            clusterer.setOptions(options);
            clusterer.buildClusterer(fileWithoutClass);
        } catch (Exception ex) {
            Logger.getLogger(AFC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    protected Instances neighbors(SimpleKMeans clusterer, int kVizinhos) throws Exception {
        
        Instances ret = new Instances(file);
        ret.delete();
        
        for (int i = 0; i < fileWithoutClass.numInstances(); i++) {
            
            Instance t = fileWithoutClass.instance(i);
            int clusterT = clusterer.clusterInstance(t);

            EuclideanDistance df = new EuclideanDistance(fileWithoutClass);
            df.setDontNormalize(true);
            
            KDTree tree = new KDTree();
            tree.setInstances(fileWithoutClass);
            tree.setDistanceFunction(df);
            
            Instances vizinhos = tree.kNearestNeighbours(t, kVizinhos);
            
            for (int j = 0; j < vizinhos.numInstances(); j++) {
                
                int clusterV = clusterer.clusterInstance(vizinhos.instance(j));
                
                if (clusterT != clusterV) {
                    ret.add(file.instance(i));
                    
                    distances.add(new EuclideanDistance(file).distance(
                            file.instance(i), vizinhos.instance(j)));
                    break;
                }
            }
        }
        
        return ret;
    }    
    
    protected Instances roots(Instances samples) throws Exception {
        
        Instances ret = new Instances(samples);
        ret.delete();
        
        EuclideanDistance df = new EuclideanDistance(fileWithoutClass);
        df.setDontNormalize(true);
        
        KDTree tree = new KDTree();
        tree.setInstances(fileWithoutClass);
        tree.setDistanceFunction(df);
        
        for (int i = 0; i < samples.numInstances(); i++) {
            ret.add(tree.nearestNeighbour(samples.instance(i)));
        }
        
        return ret;
    }
    
    protected Instances sort(Instances amostrasDeFronteira) throws Exception {
        Instances ret = new Instances(amostrasDeFronteira);
        ret.delete();
        
        Map<Integer, Double> mapa = new HashMap<>();
        
        for (int i = 0; i < distances.size(); i++) {
            mapa.put(i, distances.get(i));
        }
        
        Map<Integer, Double> sorted = MapUtil.sortByValue(mapa);
        
        Set<Integer> keys = sorted.keySet();
        
        keys.stream().forEach((key) -> {
            ret.add(amostrasDeFronteira.instance(key));
        });

        return ret;
    }

    protected void removeRootsFromFiles(Instances raizes) {
        deletedRootsWithClass = new Instances(file);
        deletedRootsWithClass.delete();
        
        for (int i = raizes.numInstances() - 1; i >= 0; i--) {
            for (int j = 0; j < fileWithoutClass.numInstances(); j++) {
                if(raizes.instance(i).toString().
                        equals(fileWithoutClass.instance(j).toString())){
                    
                    deletedRootsWithClass.add(file.instance(j));
                    file.delete(j);
                    fileWithoutClass.delete(j);
                    break;
                }
            }
        }        
    }

    protected Instances rootsPlusSorted(Instances amostrasDeFronteiraOrdenada) {
        Instances ret = new Instances(deletedRootsWithClass);
        for (int i = 0; i < amostrasDeFronteiraOrdenada.numInstances(); i++) {
            ret.add(amostrasDeFronteiraOrdenada.instance(i));
        }
        return ret;
    }

}
