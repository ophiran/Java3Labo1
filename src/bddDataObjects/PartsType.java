/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bddDataObjects;

/**
 *
 * @author mike
 */
public enum PartsType {
    L("l"), T("t"), U("u"), X2("x2"), X3("x3"), C2("c2"), C3("c3"), C4("c4")
        ,T2("t2");
    String type;
    
    PartsType(String type){
        this.type = type;
    }
}
