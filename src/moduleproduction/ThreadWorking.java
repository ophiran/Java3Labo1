package moduleProduction;

import bddDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ThreadWorking extends Thread{
	
	private OutputStream output;
	private InputStream input;
        private volatile boolean mustStop = false;
	
	
	public ThreadWorking(InputStream source,OutputStream target) {
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
                    if(input.available() != 0 ){
                        System.out.println("Working on a new piece");
                        ObjectOutputStream oos = new ObjectOutputStream(output);
                        ObjectInputStream ois = new ObjectInputStream(input);
                        Order newOrder = (Order)ois.readObject();
                        int workDuration = newOrder.getType().getBaseTime() * newOrder.getQuantity();
                        System.out.println("Estimated duration :" + (double)workDuration / 1000);
                        sleep(workDuration);

                        oos.writeObject(newOrder);

                    }
                } catch(IOException ioe) {

                } catch(InterruptedException ie){
                    mustStop = true;
                } catch(ClassNotFoundException cnfe){

                }
            }
	}
	
}
