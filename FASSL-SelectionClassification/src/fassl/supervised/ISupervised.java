/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fassl.supervised;

/**
 *
 * @author guilherme
 */
public interface ISupervised {
    
    void makeItHappen() throws Exception;
    void train() throws Exception;
    void classify() throws Exception;
    
}
