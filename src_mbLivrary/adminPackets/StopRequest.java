/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class StopRequest {
    public int seconds;
    
    public StopRequest(int seconds) {
        this.seconds = seconds;
    }
    
    public String networkString() {
        return "stop#" + String.valueOf(seconds) + "\r\n";
    }
}
