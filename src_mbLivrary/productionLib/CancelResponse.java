/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

/**
 *
 * @author mike
 */
public class CancelResponse implements Response{
    public boolean ack;
    public String cause;
    
    public CancelResponse(boolean ack, String cause) {
        this.ack = ack;
        this.cause = cause;
    }
    
    public String networkString() {
        String toRet;
        if (ack) {
            toRet = "login#1#\r\n";
        } else {
            toRet = "login#0#" + this.cause + "\r\n";
        }
        
        return toRet;
    }
    
}
