/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bddDataObjects;

import java.awt.List;

/**
 *
 * @author mike
 */
public enum PartsType {
    L("L",2000), 
    T("T",5000), 
    U("U",2700), 
    X2("X2",3354), 
    X3("X3",1457), 
    C2("C2",2114), 
    C3("C3",4521), 
    C4("C4",4122),
    T2("T2",4525);
    
    private String type;
    private int baseTime;
    
    PartsType(String type,int baseTime){
        this.type = type;
        this.baseTime = baseTime;
    }
    
    public String getType() {
    	return type;
    }
    
    public int getBaseTime() {
    	return baseTime;
    }
    
}
