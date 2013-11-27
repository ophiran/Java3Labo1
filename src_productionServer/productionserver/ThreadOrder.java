package productionserver;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadOrder extends Thread{
	
    private OutputStream output;
    private InputStream input;
    private volatile boolean mustStop = false;
    private ContainerAccess dbAccess;


    public ThreadOrder(InputStream source,OutputStream target) {
        output = target;
        input = source;
        dbAccess = ContainerAccess.getInstance();
    }

    public void terminate(){
        mustStop = true;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(output);
            while(!mustStop) {
                try {
                    Order order = (Order) dbAccess.getOrderToTreat();
                    
                    if (order != null) {
                        oos.writeObject(order);
                        oos.flush();
                        ServerLog.write("ThreadOrder > New order sent to production");
                    }
                    this.sleep(500);
                } catch(IOException ioe) {
                    Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, "error", ioe);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	
}
