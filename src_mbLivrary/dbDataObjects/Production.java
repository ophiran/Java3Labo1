package dbDataObjects;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mike
 */
public class Production implements Serializable{
    private Integer id;
    private Date date;
    private int refPart;
    private int refOrder;
    private Integer quantity;
    private Integer defectivePartsQuantity;
    
    public void addDefectPart(){
        if (this.defectivePartsQuantity < this.quantity){
            this.defectivePartsQuantity++;
        }
    }
    
    @Override
    public String toString(){
        return (quantity.toString() + "*" + String.valueOf(refPart) + " on " + date.toString()
                + " with " + defectivePartsQuantity.toString() + " defective parts");
    }
        
    public Production(Date date, int refOrder, int refPart,
            int quantity){
        this.date = date;
        this.refOrder = refOrder;
        this.refPart = refPart;
        this.quantity = quantity;
        this.defectivePartsQuantity = 0;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int getDefectivePartsQuantity() {
        return defectivePartsQuantity;
    }
    
    public int getRefPart() {
        return this.refPart;
    }
    
    public int getRefOrder() {
        return this.refOrder;
    }
}
