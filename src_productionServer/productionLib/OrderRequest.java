/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

import dbDataObjects.PartsType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public OrderRequest(String data) {
        try {
            String vectStr[] = data.split("#");
            this.partsType = PartsType.valueOf(vectStr[1].toUpperCase());
            this.quantity = Integer.parseInt(vectStr[2]);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            this.desiredDate = df.parse(vectStr[3]);
        } catch (ParseException ex) {
            Logger.getLogger(OrderRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
