/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.splitter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import weka.core.Debug;
import weka.core.Instances;

/**
 *
 * @author guilherme
 */
public class Splitter {
    
    private Instances z2i, z2ii, z3;
    
    public void split(Instances data, Double pct){
        
        Enumeration<Object> classes = data.classAttribute().enumerateValues();
        
        List<Instances> amostrasSeparadas = new ArrayList<>();
        
        while(classes.hasMoreElements()){
            String classe = classes.nextElement().toString();
            
            Instances temp = new Instances(data);
            temp.delete();
            
            for (int i = data.numInstances() - 1; i >= 0; i--) {
                if(data.instance(i).stringValue(data.classIndex()).equals(classe)){
                    temp.add(data.instance(i));
                    data.delete(i);
                }
            }
            temp.randomize(new Debug.Random());
            amostrasSeparadas.add(temp);
        }
        
        Instances _z2i = new Instances(data);
        Instances _z2ii = new Instances(data);
        Instances _z3 = new Instances(data);
        
        _z2i.delete();
        _z2ii.delete();
        _z3.delete();
            
        amostrasSeparadas.stream().forEach((amostras) -> {
            int numInstZ2i, numInstZ2ii;
            numInstZ2i = numInstZ2ii = (int) Math.ceil((amostras.numInstances() * pct) / 2);
            
            for (int i = 0; i < numInstZ2i; i++) {
                _z2i.add(amostras.instance(i));
            }
            for (int i = numInstZ2i; i < (numInstZ2i + numInstZ2ii); i++) {
                _z2ii.add(amostras.instance(i));
            }
            for (int i = (numInstZ2i + numInstZ2ii); i < amostras.numInstances(); i++) {
                _z3.add(amostras.instance(i));
            }
        });
        
        setZ2i(_z2i);
        setZ2ii(_z2ii);
        setZ3(_z3);
        
    }
    
    public Instances getZ2i() {
        return z2i;
    }

    public void setZ2i(Instances z2i) {
        this.z2i = z2i;
    }

    public Instances getZ2ii() {
        return z2ii;
    }

    public void setZ2ii(Instances z2ii) {
        this.z2ii = z2ii;
    }

    public Instances getZ3() {
        return z3;
    }

    public void setZ3(Instances z3) {
        this.z3 = z3;
    }
    
}
