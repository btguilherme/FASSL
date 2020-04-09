/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fassl.methods;

import fassl.utils.IO;
import fassl.utils.Timer;
import java.util.List;
import java.util.UUID;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 *
 * @author Guilherme Camargo
 */
public class MSTBE extends AFC{

    public MSTBE(Instances file, int nClusters, String fileName, String method, String savePath) throws Exception {
        super(file, nClusters, fileName, method, savePath);
    }
    
    @Override
    public void makeItHappen() throws Exception {
        Timer timer = new Timer();
              
        cluster();
        Instances raizes = roots(clusterer.getClusterCentroids());
        removeRootsFromFiles(raizes);
        
        EuclideanDistance d = new EuclideanDistance(this.file);
        int vertices = this.file.numInstances();
        KrushkalMST.Graph graph = new KrushkalMST.Graph(vertices);
        
        for (int i = 0; i < this.file.numInstances()-1; i++){
            for (int j = i+1; j < this.file.numInstances(); j++) {
                double dist = d.distance(this.file.instance(i), 
                        this.file.instance(j));
                graph.addEgde(i, j, dist);
            }
        }
        
        Instances amostrasDeFronteiraOrdenada = new Instances(this.file);
        amostrasDeFronteiraOrdenada.delete();
        
        List<Integer> indexes = graph.kruskalMST();
        for (Integer i : indexes) {
            amostrasDeFronteiraOrdenada.add(this.file.get(i));
        }
        
        this.sorted = rootsPlusSorted(amostrasDeFronteiraOrdenada);
        
        String sortTime = String.valueOf(timer.getTime());
        
        String uuid = String.valueOf(UUID.randomUUID());
        IO.saveConcat("#" + uuid, savePath + "_sort_time_" + method + ".txt");
        IO.saveConcat(sortTime, savePath + "_sort_time_" + method + ".txt");
        
        save();
    }
    

}
