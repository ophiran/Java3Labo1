package dbDataObjects;

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
    
    public Order(int id, Date date, int refClient, PartsType partsType, int quantity){
        this.id = id;
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
    
    public Date getDate() {
        return this.date;
    }
    
    public int getRefClient() {
        return this.refClient;
    }
    
    public int getIdOrder() {
        return this.id;
    }
    
}
