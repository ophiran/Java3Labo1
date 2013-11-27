package productionserver;

import dbDataObjects.Order;
import dbDataObjects.Part;
import dbDataObjects.Production;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

import containerDbAccess.ContainerAccess;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadWorking extends Thread{
	
    private OutputStream output;
    private InputStream input;
    private ContainerAccess accessContainer;
    private volatile boolean mustStop = false;
    private double defectivePercentage = 0.21;


    public ThreadWorking(InputStream source,OutputStream target) {
        output = target;
        input = source;
        accessContainer = ContainerAccess.getInstance();
    }

    public void terminate(){
        mustStop = true;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(output);
            ois = new ObjectInputStream(input);
            while(!mustStop) {
                try {
                    
                    Order newOrder = (Order)ois.readObject();
                    ServerLog.write("ThreadWorking > Working on a new set of parts");
                    Part partToBuild = accessContainer.getInfoParts(newOrder.getType());
                    Production newProduction = new Production(new Date(), newOrder.getIdOrder(), partToBuild.getIdPart(),  
                            newOrder.getQuantity());
                    
                    for(int i = 0; i < newOrder.getQuantity(); i++){
                        ServerLog.write("ThreadWorking > Estimated time : " + partToBuild.getfabricationTime()/1000.0 + "s");
                        sleep(partToBuild.getfabricationTime());
                        if(Math.random() > (1 - defectivePercentage)){
                            newProduction.addDefectPart();
                        }
                    }
                    
                    ServerLog.write("ThreadWorking > Finished working");
                    oos.writeObject(newProduction);
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                } catch(InterruptedException ie){
                    mustStop = true;
                } catch(ClassNotFoundException cnfe){

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadWorking.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadWorking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
	
}
