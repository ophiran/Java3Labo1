/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionLib;

/**
 *
 * @author mike
 */
public class CancelRequest implements Request {
    public int OrderId;
    
    public CancelRequest(int OrderId) {
        this.OrderId = OrderId;
    }
}
