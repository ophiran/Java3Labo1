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
    L("l",2000), 
    T("t",5000), 
    U("u",2700), 
    X2("x2",3354), 
    X3("x3",1457), 
    C2("c2",2114), 
    C3("c3",4521), 
    C4("c4",4122),
    T2("t2",4525);
    
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
