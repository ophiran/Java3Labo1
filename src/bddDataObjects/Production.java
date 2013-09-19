package bddDataObjects;

import java.util.Date;

/**
 *
 * @author mike
 */
public class Production {
    private int id;
    private Date date;
    private PartsType partsType;
    private int quantity;
    
    public Production(int id, Date date, PartsType partsType,
            int quantity){
        this.id = id;
        this.date = date;
        this.partsType = partsType;
        this.quantity = quantity;
    }
}
