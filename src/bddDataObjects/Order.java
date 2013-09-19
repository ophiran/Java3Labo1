package bddDataObjects;

import java.util.Date;

/**
 *
 * @author mike
 */
public class Order {
    private int id;
    private Date date;
    private int refClient;
    private PartsType partsType;
    private int quantity;
    
    public Order(int id, Date date, int refClient, PartsType partsType,
            int quantity){
        this.id = id;
        this.date = date;
        this.refClient = refClient;
        this.partsType = partsType;
        this.quantity = quantity;
    }
}
