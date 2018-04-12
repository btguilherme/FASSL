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
public class SystemUserDir {

    private static final String SEP = File.separator;
    
    public static String newPath(String progr) {
        
        String[] aux = System.getProperty("user.dir").split(SEP);
        String newString = "";
        for (int i = 0; i < aux.length - 1; i++) {
            newString = newString.concat(aux[i]).concat(SEP);
        }
        return newString.concat(progr);
    }

}
