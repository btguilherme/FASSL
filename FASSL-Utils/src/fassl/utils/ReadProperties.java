/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author guilherme
 */
public final class ReadProperties {
    
    private String arffFile;
    private int nSplits;
    private double pct;
    private String[] sort;
    private String basesSavePath;
    private int xNumClasses;
    private String[] classifiers;
    private String splitsSorted;

    
    public ReadProperties() throws IOException{
        read();
    }
    
    public void read() throws IOException{
        
        String temp[] = System.getProperty("user.dir").split("/");
        String propPath = "";
        for (int i = 0; i < temp.length - 1; i++) {
            propPath = propPath.concat(temp[i]);
            propPath = propPath.concat("/");
        }
        
        Properties props = new Properties();
        FileInputStream file
                = new FileInputStream(propPath + "settings.properties");
        props.load(file);
        
        setxNumClasses(Integer.valueOf(props.getProperty("x-num-classes")));
        setBasesSavePath(props.getProperty("bases-save-path"));
        setArffFile(props.getProperty("arff-path"));
        setnSplits(Integer.valueOf(props.getProperty("num-splits")));
        setPct(Double.valueOf(props.getProperty("pct")));
        setSort(props.getProperty("sort").split(" "));
        setClassifiers(props.getProperty("classifier").split(" "));
        setSplitsSorted(props.getProperty("splits-sorted"));
        
        file.close();
    }
    
    public String getArffFile() {
        return arffFile;
    }

    public void setArffFile(String arffFile) {
        this.arffFile = arffFile;
    }

    public int getnSplits() {
        return nSplits;
    }

    public void setnSplits(int nSplits) {
        this.nSplits = nSplits;
    }

    public double getPct() {
        return pct;
    }

    public void setPct(double pct) {
        this.pct = pct;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }
    
    public String[] getSort() {
        return sort;
    }

    public void setBasesSavePath(String basesSavePath) {
        this.basesSavePath = basesSavePath;
    }
    
    public String getBasesSavePath(){
        return basesSavePath;
    }
    
    public int getxNumClasses() {
        return xNumClasses;
    }

    public void setxNumClasses(int xNumClasses) {
        this.xNumClasses = xNumClasses;
    }
    
    public String[] getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(String[] classifiers) {
        this.classifiers = classifiers;
    }
    
    public String getSplitsSorted() {
        return splitsSorted;
    }

    public void setSplitsSorted(String splitsSorted) {
        this.splitsSorted = splitsSorted;
    }
}
