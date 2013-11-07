package productionserver;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Client;
import dbDataObjects.Order;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import productionLib.CancelRequest;
import productionLib.LoginRequest;
import productionLib.LoginResponse;
import productionLib.OrderRequest;
import productionLib.Response;

/**
 *
 * @author mike
 */
public class ProductionServer {
    static Socket clientSocket;
    static ServerSocket servSocket;
    static boolean mustStop = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client clientLogin = null;
        PipedOutputStream posWindow = null;
        ThreadWorking threadWorking;
        ThreadStore threadStore;
        ThreadOrder threadOrder;
        ContainerAccess dbAccess;
        dbAccess = ContainerAccess.getInstance();
        
        PipedInputStream pisWorking, pisStore, pisOrder;
        PipedOutputStream posWorking, posOrder;
        
        try{
            pisWorking = new PipedInputStream();
            pisStore = new PipedInputStream();
            pisOrder = new PipedInputStream();
            posOrder = new PipedOutputStream(pisWorking);
            posWorking = new PipedOutputStream(pisStore);
            posWindow = new PipedOutputStream(pisOrder);
            
            threadOrder = new ThreadOrder(pisOrder, posOrder);
            threadWorking = new ThreadWorking(pisWorking, posWorking);
            threadStore = new ThreadStore(pisStore);
            threadOrder.start();
            threadWorking.start();
            threadStore.start();
        }
        catch (IOException e){
            
        }
        
        try {
            servSocket = new ServerSocket(50000);
            System.out.println("ProductionServer > Server running");
        } catch(SocketException se) {
            System.err.println("Error creating serverSocket");
        } catch(IOException e) {
            System.err.println("Error creating serverSocket");
        }
        
        while (!mustStop) {
            try {
                System.out.println("ProductionServer > Waiting for a client");
                clientSocket = servSocket.accept();
                System.out.println("ProductionServer > Client connected");
            } catch(SocketException se) {
                System.err.println("Error creating serverSocket");
            } catch(IOException e) {
                System.err.println("Error creating serverSocket");
            }

            if (clientSocket != null) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    while(clientSocket.isConnected()) {
                        Object req = ois.readObject();
                        System.out.println("ProductionServer > Request received from the client");
                        if (req instanceof LoginRequest) {
                            LoginRequest request = (LoginRequest) req;
                            clientLogin = new Client(request.login, request.password);
                            if (clientLogin.isAuthorized()) {
                                Response resp = new LoginResponse(true, null);
                                oos.writeObject(resp);
                            } else {
                                Response resp = new LoginResponse(false, "Wrond id");
                                oos.writeObject(resp);
                                // TODO add closing the connection
                            }
                        } else if (req instanceof OrderRequest) {
                            OrderRequest request = (OrderRequest) req;
                            if(clientLogin != null){
                                ContainerAccess db = ContainerAccess.getInstance();
                                ObjectOutputStream toPipe = new ObjectOutputStream(posWindow);
                                Client customer = db.getClient(clientLogin.login);
                                int lastProdOrderId = dbAccess.getLastProdOrderId();
                                Order newOrder = new Order(lastProdOrderId + 1, new Date(), customer.getId(),
                                    request.partsType, request.quantity);
                                toPipe.writeObject(newOrder);
                                dbAccess.sendProductionOrder(newOrder);
                                System.out.println("ProductionServer > A new order has been sent");

                            } else {
                                //JOptionPane.showMessageDialog(this, "Unknown client, please register via the web application");
                                System.out.println("ProductionServer > Unknown client");
                            }
                        } else if (req instanceof CancelRequest) {
                            CancelRequest request = (CancelRequest) req;
                            System.out.println(request.OrderId);
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);


                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("ProductionServer > Client disconnected");
                }
            }
        }
        if (mustStop) {
            // TODO shutdown production threads
        }
    }
}
