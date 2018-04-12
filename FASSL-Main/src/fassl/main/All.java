/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.main;

import fassl.information.FASSLInformation;
import fassl.sort.FASSLSort;
import fassl.splitter.FASSLSplitter;
import fassl.utils.*;

/**
 *
 * @author guilherme
 */
public class All {

    /**
     * This program will perform all stages of the classifier learning process, 
     * from the data set split to the information retrieval.
     * 
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        
        FASSLSplitter.main(args);
        FASSLSort.main(args);
        FASSLSelectionClassification.main(args);
        FASSLInformation.main(args);

    }

}
