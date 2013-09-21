package moduleProduction;

import bddDataObjects.Order;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ThreadStore extends Thread{
	
	private OutputStream output;
	private InputStream input;
        private volatile boolean mustStop = false;
	
	public ThreadStore(InputStream source,OutputStream target) {
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
                        ObjectOutputStream oos = new ObjectOutputStream(output);
                        ObjectInputStream ois = new ObjectInputStream(input);
                        try{
                            ((Order)ois.readObject()).display();
                        }
                        catch (ClassNotFoundException cnfe){
                            
                        }
                        
	            }
	        } catch(IOException ioe) {
				
	        }
	    }
	}
}
