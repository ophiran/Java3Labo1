package moduleProduction;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Production;

public class ThreadStore extends Thread{
	
    private InputStream input;
    private ContainerAccess accessContainer;
    private volatile boolean mustStop = false;

    public ThreadStore(InputStream source) {
        input = source;
        accessContainer = ContainerAccess.getInstance();
    }

    public void terminate(){
        mustStop = true;
    }

    @Override
    public void run() {
        while(!mustStop) {
            try {
                ObjectInputStream ois = new ObjectInputStream(input);
                Production product = (Production)ois.readObject();
                System.out.println("Storing a production in DB");
                accessContainer.sendProductionInfo(product);
                System.out.println("Finished storing");
                
            } catch (IOException ioe) {
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
