package productionserver;

import dbDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;

public class ThreadOrder extends Thread{
	
    private OutputStream output;
    private InputStream input;
    private volatile boolean mustStop = false;


    public ThreadOrder(InputStream source,OutputStream target) {
        output = target;
        input = source;
    }

    public void terminate(){
        mustStop = true;
    }

    @Override
    public void run() {
        while(!mustStop) {
            try {
                
                ObjectOutputStream oos = new ObjectOutputStream(output);
                ObjectInputStream ois = new ObjectInputStream(input);

                oos.writeObject(ois.readObject());
                System.out.println("New order received");
            } catch(IOException ioe) {

            } catch(ClassNotFoundException cnfe){

            }
        }
    }
	
}
