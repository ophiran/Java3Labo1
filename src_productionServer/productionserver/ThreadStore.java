package productionserver;

import dbDataObjects.Production;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import containerDbAccess.ContainerAccess;

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
                System.out.println("ThreadStore > Storing a production in BD");
                accessContainer.sendProductionInfo(product);
                System.out.println("ThreadStore > Finished storing");
                
            } catch (IOException ioe) {
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
