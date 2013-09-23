package bddDataObjects;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mike
 */
public class Order implements Serializable{
    private int id;
    private Date date;
    private int refClient;
    private PartsType partsType;
    private int quantity;
    private static int orderNumber = 0;
    
    public Order(Date date, int refClient, PartsType partsType, int quantity){
        orderNumber++;
        this.id = orderNumber;
        this.date = date;
        this.refClient = refClient;
        this.partsType = partsType;
        this.quantity = quantity;
    }
    
    public PartsType getType(){
        return this.partsType;
    }
    
    public int getQuantity(){
        return this.quantity;
    }
    
}
