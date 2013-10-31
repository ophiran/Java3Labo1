/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

import dbDataObjects.PartsType;
import java.util.Date;

/**
 *
 * @author mike
 */
public class OrderRequest implements Request {
    public PartsType partsType;
    public int quantity;
    public Date desiredDate;
    
    public OrderRequest(PartsType partsType, int quantity, Date desiredDate) {
        this.partsType = partsType;
        this.quantity = quantity;
        this.desiredDate = desiredDate;
    }
}
