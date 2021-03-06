package moduleProduction;

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
        while(!mustStop) {
            try {
                
                ObjectOutputStream oos = new ObjectOutputStream(output);
                ObjectInputStream ois = new ObjectInputStream(input);
                Order newOrder = (Order)ois.readObject();
                System.out.println("Working on a new set of parts");
                Part partToBuild = accessContainer.getInfoParts(newOrder.getType());
                Production newProduction = new Production(new Date(), newOrder.getIdOrder(), partToBuild.getIdPart(),  
                        newOrder.getQuantity());
                
                for(int i = 0; i < newOrder.getQuantity(); i++){
                    System.out.println("Estimated time : " + partToBuild.getfabricationTime()/1000.0 + "s");
                    sleep(partToBuild.getfabricationTime());
                    if(Math.random() > (1 - defectivePercentage)){
                        newProduction.addDefectPart();
                    }
                }
                
                System.out.println("Finished working");
                oos.writeObject(newProduction);
            } catch(IOException ioe) {

            } catch(InterruptedException ie){
                mustStop = true;
            } catch(ClassNotFoundException cnfe){

            }
        }
    }
	
}
