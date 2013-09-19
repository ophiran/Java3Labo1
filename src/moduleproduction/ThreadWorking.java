package moduleProduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadWorking extends Thread{
	
	OutputStream output;
	InputStream input;
	
	
	public ThreadWorking(InputStream source,OutputStream target) {
		output = target;
		input = source;
	}
	
	@Override
	public void run() {
		super.run();
		
		while(true) {
			try {
				wait();
				while(input.available() != 0 ){
					
				}
				notify();
			} catch(InterruptedException ie){
				
			} catch(IOException ioe) {
				
			}
		}
		
	}
	
}
