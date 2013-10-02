package bddDataObjects;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mike
 */
public class Production implements Serializable{
    private Integer id;
    private Date date;
    private PartsType partsType;
    private Integer quantity;
    private Integer defectivePartsQuantity;
    
    public void addDefectPart(){
        if (this.defectivePartsQuantity < this.quantity){
            this.defectivePartsQuantity++;
        }
    }
    
    @Override
    public String toString(){
        return (quantity.toString() + "*" + partsType.toString() + " on " + date.toString()
                + " with " + defectivePartsQuantity.toString() + " defective parts");
    }
        
    public Production(Date date, PartsType partsType,
            int quantity){
        this.date = date;
        this.partsType = partsType;
        this.quantity = quantity;
        this.defectivePartsQuantity = 0;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int getDefectivePartsQuantity() {
        return defectivePartsQuantity;
    }
    
    public String getIdParts() {
        return partsType.getType();
    }
}
