package productionserver;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        while(!mustStop) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(output);

                Order order = (Order) dbAccess.getOrderToTreat();
                
                if (order != null) {
                    Calendar orderDate = Calendar.getInstance();
                    Calendar today = Calendar.getInstance();
                    orderDate.setTime(order.getDate());
                    today.setTime(new Date());
                    if (orderDate.before(today) || orderDate.equals(today)) {
                        oos.writeObject(order);
                        ServerLog.write("ThreadOrder > New order sent to production");
                    }
                }
                this.sleep(500);
            } catch(IOException ioe) {
                Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, "error", ioe);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	
}
