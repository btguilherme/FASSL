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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class RDBS extends RDS{

    public RDBS(Instances file, int nClusters, String fileName, String method, String savePath) throws Exception{
        super(file, nClusters, fileName, method, savePath);
    }

    @Override
    public void makeItHappen() throws Exception {
        
        Timer timer = new Timer();
        
        int kVizinhos = file.numClasses()/2;
        cluster();
        raizes = clusterer.getClusterCentroids();
        Instances amostrasDeFronteira = neighbors(clusterer, kVizinhos);
        sort(amostrasDeFronteira);
        
        String sortTime = String.valueOf(timer.getTime());
        String uuid = String.valueOf(UUID.randomUUID());
        IO.saveConcat("#" + uuid, savePath + "_sort_time_" + method + ".txt");
        IO.saveConcat(sortTime, savePath + "_sort_time_" + method + ".txt");
        
        save();
    }
    
    @Override
    public Instances sort(Instances amostrasDeFronteira) {
        
        Instances ret = new Instances(amostrasDeFronteira);
        ret.delete();
        
        Instances amostrasDeFronteiraSemClasse = 
                InstancesManipulation.removeAtributoClasse(amostrasDeFronteira);
        
        listas = new Instances[nClusters];
        
        int[] assign = new int[amostrasDeFronteira.numInstances()];
        
        for (int i = 0; i < amostrasDeFronteira.numInstances(); i++) {
            try {
                assign[i] = clusterer.
                        clusterInstance(amostrasDeFronteiraSemClasse.instance(i));
            } catch (Exception ex) {
                Logger.getLogger(RDBS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (int i = 0; i < raizes.numInstances(); i++) {
            
            Map<Integer, Double> instDistRel = new HashMap<>();
            int clusterNum = 0;
            try {
                clusterNum = clusterer.clusterInstance(raizes.instance(i));
            } catch (Exception ex) {
                Logger.getLogger(RDBS.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            listas[clusterNum] = new Instances(amostrasDeFronteira);
            listas[clusterNum].delete();
            
            for (int j = 0; j < assign.length; j++) {
                if(assign[j] == clusterNum){
                    EuclideanDistance ed = 
                            new EuclideanDistance(amostrasDeFronteiraSemClasse);
                    
                    double distance = ed.distance(raizes.instance(i), 
                            amostrasDeFronteiraSemClasse.instance(j));
                    instDistRel.put(j, distance);
                }
            }
            
            Map<Integer, Double> _sorted = MapUtil.sortByValue(instDistRel);
            
            Set<Integer> keys = _sorted.keySet();
            
            for (Integer key : keys) {
                listas[clusterNum].add(amostrasDeFronteira.instance(key));
            }
        }
        
        return ret;
    }
    
}
