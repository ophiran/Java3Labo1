package moduleProduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThreadStore extends Thread{
	
	OutputStream output;
	InputStream input;
	
	public ThreadStore(InputStream source,OutputStream target) {
		output = target;
		input = source;
	}

	@Override
	public void run() {
		while(true) {
			try {
				if(input.available() != 0 ){
					
				}
			} catch(IOException ioe) {
				
			}
		}
	}
}
