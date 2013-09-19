package moduleProduction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author mike
 */
public class ThreadOrder extends Thread{
    private InputStream input;
    private OutputStream output;
    
    public ThreadOrder(InputStream source, OutputStream target){
        this.input = source;
        this.output = target;
    }
    
    @Override
    public void run(){
        while(true) {
            try {
                if(input.available() != 0 ){
                    
                }
            } catch(IOException ioe) {
                
            }
        }
    }
    
}
