/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

/**
 *
 * @author mike
 */
public class LoginResponse implements Response{
    public boolean ack;
    public String cause;
    
    public LoginResponse(boolean ack, String cause) {
        this.ack = ack;
        this.cause = cause;
    }
    
}
