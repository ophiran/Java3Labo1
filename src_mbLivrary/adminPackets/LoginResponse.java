/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class LoginResponse{
    public boolean ack;
    public String cause;
    
    public LoginResponse(boolean ack, String cause) {
        this.ack = ack;
        this.cause = cause;
    }
    
    public LoginResponse(String data) {
        String vectStr[] = data.split("#");
        if (vectStr[1].equals("1")) {
            this.ack = true;
        } else {
            this.ack = false;
            this.cause = vectStr[2];
        }
    }
    
    public String networkString() {
        String toRet;
        if (ack) {
            toRet = "logina#1#\r\n";
        } else {
            toRet = "logina#0#" + this.cause + "\r\n";
        }
        
        return toRet;
    }
    
}
