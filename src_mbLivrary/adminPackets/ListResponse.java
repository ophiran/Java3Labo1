/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adminPackets;

import java.util.Vector;

/**
 *
 * @author mike
 */
public class ListResponse {
    public boolean ack;
    public String cause;
    public Vector<String> listClients;
    
    public ListResponse(String data) {
        listClients = new Vector<String>();
        String vectStr[] = data.split("#");
        if (vectStr[1].equals("1")) {
            this.ack = true;
            for (int j = 2; j < vectStr.length; j++) {
                this.listClients.add(vectStr[j]);
            }
        } else {
            this.ack = false;
            this.cause = vectStr[2];
        }
    }
    
}
