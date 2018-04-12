/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.utils;

import java.io.File;

/**
 *
 * @author guilherme
 */
public enum Enumerations {
    
    SEP(File.separator),
    
    PROGRMAIN("FASSL-Main"),
    PROGRSORT("FASSL-Sort"), 
    PROGRSPLITTER("FASSL-Splitter"), 
    PROGRSELECTION("FASSL-SelectionClassification"), 
    PROGRINFO("FASSL-Information"), 
    PROGRUTILS("FASSL-Utils");
    
    public String value;
    Enumerations(String value){
        this.value = value;
    }
    
}
