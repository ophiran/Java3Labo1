/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class PauseRequest {
    public PauseRequest() {}
    
    public String networkString() {
        return "pause\r\n";
    }
    
}
