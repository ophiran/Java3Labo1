package moduleProduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class OrderWorkingMonitor {
	
    private InputStream input;
    private OutputStream output;

	public OrderWorkingMonitor(InputStream source, OutputStream target) {
        this.input = source;
        this.output = target;
	}
	
	public synchronized void writeStream(Object source) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(output);
	}
	
	public synchronized Object readStream() {
		return null;
	}
}
