/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class PauseResponse {
    public boolean ack;
    public boolean serverPaused;
    public String cause;
    
    public PauseResponse(boolean serverPaused) {
        this.ack = true;
        this.serverPaused = serverPaused;
    }
    
    public PauseResponse(boolean ack, String cause) {
        this.ack = ack;
        this.cause = cause;
    }
    
    public PauseResponse(String data) {
        String vectStr[] = data.split("#");
        if (vectStr[1].equals("1")) {
            this.ack = true;
            if (vectStr[2].equals("1")) {
                this.serverPaused = true;
            } else {
                this.serverPaused = false;
            }
        } else {
            this.ack = false;
            this.cause = vectStr[2];
        }
    }
    
    public String networkString() {
        String toRet;
        if (ack) {
            toRet = "pause#1#";
            if (this.serverPaused == true) {
                toRet += "1\r\n";
            } else {
                toRet += "0\r\n";
            }
        } else {
            toRet = "pause#0#" + this.cause + "\r\n";
        }
        
        return toRet;
    }
}
