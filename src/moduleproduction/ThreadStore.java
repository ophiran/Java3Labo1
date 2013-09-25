package moduleProduction;

import bddDataObjects.Production;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
                ObjectInputStream ois = new ObjectInputStream(input);

                try{
                    bw.write(((Production)ois.readObject()).toString());
                    bw.newLine();
                    bw.close();
                } catch (ClassNotFoundException cnfe){

                }
            } catch (IOException ioe) {
            }
        }
    }
}
