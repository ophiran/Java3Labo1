package moduleproduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ThreadWorking extends Thread{
	
	OutputStream output;
	InputStream input;
	
	
	public ThreadWorking(OutputStream target,InputStream source) {
		output = target;
		input = source;
	}
	
	@Override
	public void run() {
		super.run();
		
		
	}
	
}
