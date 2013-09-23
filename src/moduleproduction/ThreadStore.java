package moduleProduction;

import bddDataObjects.Production;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
                    sleep(500);
	            if(input.available() != 0 ){
                        DataOutputStream dos = new DataOutputStream(output);
                        ObjectInputStream ois = new ObjectInputStream(input);
                        
                        try{
                            dos.writeUTF(((Production)ois.readObject()).toString());
                        }
                        catch (ClassNotFoundException cnfe){
                            
                        }
                        
	            }
	        } catch(IOException ioe) {
				
	        } catch(InterruptedException ie) {
                    mustStop = true;
                }
	    }
	}
}
