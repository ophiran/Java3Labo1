package productionserver;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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
                // TODO orders should be treated even when no new order is received
                ObjectOutputStream oos = new ObjectOutputStream(output);
                ObjectInputStream ois = new ObjectInputStream(input);

                dbAccess.sendProductionOrder((Order) ois.readObject()); 
                oos.writeObject(dbAccess.getOrderToTreat());
                ServerLog.write("ThreadOrder > New order received");
            } catch(IOException ioe) {

            } catch(ClassNotFoundException cnfe){

            }
        }
    }
	
}
