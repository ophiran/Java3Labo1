/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

import dbDataObjects.PartsType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class OrderResponse implements Response{
    public boolean ack;
    public String cause;
    public int orderId;
    
    public OrderResponse(boolean ack, String cause, int orderId) {
        this.ack = ack;
        this.cause = cause;
        this.orderId = orderId;
    }
    
    public OrderResponse(String data) {
        String vectStr[] = data.split("#");
        if (vectStr[1].equals("1")) {
            this.ack = true;
            this.orderId = Integer.parseInt(vectStr[2]);
        } else {
            this.ack = false;
            this.cause = vectStr[2];
        }
    }
    
    
    public String networkString() {
        String toRet;
        if (ack) {
            toRet = "ordelem#1#" + String.valueOf(orderId) + "\r\n";
        } else {
            toRet = "ordelem#0#" + this.cause + "\r\n";
        }
        
        return toRet;
    }
    
}
