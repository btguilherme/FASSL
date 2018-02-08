/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.unsupervised;

import java.io.IOException;

/**
 *
 * @author guilherme
 */
public interface IUnsupervised {
    
    void makeItHappen() throws Exception;
    void train() throws Exception;
    void classify() throws Exception;
    
}
