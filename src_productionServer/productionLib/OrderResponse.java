/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

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
