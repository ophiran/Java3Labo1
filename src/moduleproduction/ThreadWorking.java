package moduleproduction;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ThreadWorking extends Thread{
	
	PipedOutputStream output;
	PipedInputStream input;
	
	
	public ThreadWorking(PipedOutputStream target) {
		try {
			input = new PipedInputStream(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		output = new PipedOutputStream();
		//TODO send the ouputStream to StoreThread
		
		
	}
	
	@Override
	public void run() {
		super.run();
		
		
	}
	
}
