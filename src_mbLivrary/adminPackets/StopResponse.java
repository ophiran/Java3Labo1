/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class StopResponse {
    public boolean ack;
    public String cause;
    
    public StopResponse(boolean ack, String cause) {
        this.ack = ack;
        this.cause = cause;
    }
    
    public StopResponse(String data) {
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
            toRet = "stop#1\r\n";
        } else {
            toRet = "stop#0#" + this.cause + "\r\n";
        }
        
        return toRet;
    }
}
