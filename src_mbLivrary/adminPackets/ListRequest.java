/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

/**
 *
 * @author mike
 */
public class ListRequest {
    public ListRequest() {}
    
    public String networkString() {
        return "lclients\r\n";
    }
}
