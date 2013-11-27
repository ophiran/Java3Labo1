package productionserver;

import dbDataObjects.Production;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import containerDbAccess.ContainerAccess;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(input);
            while(!mustStop) {
                try {
                    
                    Production product = (Production)ois.readObject();
                    ServerLog.write("ThreadStore > Storing a production in BD");
                    accessContainer.sendProductionInfo(product);
                    ServerLog.write("ThreadStore > Finished storing");
                    
                } catch (IOException ioe) {
                    //ioe.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
