package moduleproduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadStore extends Thread{
	
	OutputStream output;
	InputStream input;
	
	public ThreadStore(OutputStream target, InputStream source) {
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
