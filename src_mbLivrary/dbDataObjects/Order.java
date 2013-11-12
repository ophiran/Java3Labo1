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
    private boolean isTreated;
    
    public Order(int id, Date date, int refClient, PartsType partsType, int quantity, boolean treated){
        this.id = id;
        this.date = date;
        this.refClient = refClient;
        this.partsType = partsType;
        this.quantity = quantity;
        this.isTreated = treated;
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
    
    public boolean isTreated() {
        return this.isTreated;
    }
    
}
