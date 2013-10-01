package moduleProduction;

import bddDataObjects.Order;
import bddDataObjects.Part;
import bddDataObjects.Production;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

import containerBddAccess.ContainerAccess;

public class ThreadWorking extends Thread{
	
    private OutputStream output;
    private InputStream input;
    private ContainerAccess accessContainer;
    private volatile boolean mustStop = false;
    private double defectivePercentage = 0.01;


    public ThreadWorking(InputStream source,OutputStream target) {
        output = target;
        input = source;
        accessContainer = new ContainerAccess();
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
                Production newProduction = new Production(new Date(),accessContainer.getInfoParts(newOrder.getType()).getPartType(), newOrder.getQuantity());
                
                for(int i = 0; i < newOrder.getQuantity(); i++){
                    System.out.println("Estimated time : " + newOrder.getType().getBaseTime()/1000 + "s");
                    sleep(newOrder.getType().getBaseTime());
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
