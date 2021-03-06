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
import java.util.logging.Level;
import java.util.logging.Logger;
import productionLib.CancelRequest;
import productionLib.CancelResponse;
import productionLib.LoginRequest;
import productionLib.LoginResponse;
import productionLib.OrderRequest;
import productionLib.Response;

/**
 *
 * @author mike
 */
public class ProductionServer extends Thread{
    private Socket clientSocket;
    private ServerSocket servSocket;
    private volatile boolean mustStop = false;
    private int serverPort;
    private PipedOutputStream posWindow = null;
    
    public ProductionServer(int port) {
        this.serverPort = port;
    }    
    @Override
    public void run() {
        Client clientLogin = null;
        ThreadWorking threadWorking = null;
        ThreadStore threadStore = null;
        ThreadOrder threadOrder = null;
        ThreadReception threadReception = null;
        ContainerAccess dbAccess = ContainerAccess.getInstance();
        
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
            threadReception = new ThreadReception();
            threadOrder.start();
            threadWorking.start();
            threadStore.start();
            threadReception.start();
        }
        catch (IOException e){
            System.err.println("Failed to start threads");
        }
        
        try {
            servSocket = new ServerSocket(serverPort);
            ServerLog.write("ProductionServer > Server running");
        } catch(SocketException se) {
            System.err.println("Error creating serverSocket");
        } catch(IOException e) {
            System.err.println("Error creating serverSocket");
        }
        
        while (!mustStop) {
            try {
                ServerLog.write("ProductionServer > Waiting for a client");
                clientSocket = servSocket.accept();
                ServerLog.write("ProductionServer > Client connected");
            } catch(SocketException se) {
                //System.err.println("Error creating serverSocket");
            } catch(IOException e) {
                System.err.println("Error creating serverSocket");
            }

            if (clientSocket != null) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    while(clientSocket.isConnected()) {
                        Object req = ois.readObject();
                        ServerLog.write("ProductionServer > Request received from the client");
                        if (req instanceof LoginRequest) {
                            LoginRequest request = (LoginRequest) req;
                            clientLogin = new Client(request.login, request.password);
                            if (clientLogin.isAuthorized()) {
                                Response resp = new LoginResponse(true, null);
                                oos.writeObject(resp);
                                ServerLog.write("ProductionServer > Client " + request.login + " logged");
                            } else {
                                Response resp = new LoginResponse(false, "Wrong id");
                                oos.writeObject(resp);
                                clientSocket.close();
                            }
                        } else if (req instanceof OrderRequest) {
                            OrderRequest request = (OrderRequest) req;
                            if(clientLogin != null){
                                ContainerAccess db = ContainerAccess.getInstance();
                                //ObjectOutputStream toPipe = new ObjectOutputStream(posWindow);
                                Client customer = db.getClient(clientLogin.login);
                                int lastProdOrderId = dbAccess.getLastProdOrderId();
                                Order newOrder = new Order(lastProdOrderId + 1, request.desiredDate, customer.getId(),
                                    request.partsType, request.quantity, false);
                                //toPipe.writeObject(newOrder);
                                dbAccess.sendProductionOrder(newOrder);
                                ServerLog.write("ProductionServer > A new order has been sent");

                            } else {
                                ServerLog.write("ProductionServer > Unknown client");
                            }
                        } else if (req instanceof CancelRequest) {
                            CancelRequest request = (CancelRequest) req;
                            CancelResponse resp = null;
                            ServerLog.write("Trying to cancel order " + request.OrderId);
                            if (dbAccess.cancelOrder(request.OrderId)) {
                                resp = new CancelResponse(true, "");
                            } else {
                                resp = new CancelResponse(false, "Could not cancel this order");
                            }
                            oos.writeObject(resp);
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SocketException se) {
                    ServerLog.write("ProductionServer > Server socket closed");
                } catch (IOException e) {
                    ServerLog.write("ProductionServer > Client disconnected");
                }
            }
        }
        if (mustStop) {
            ServerLog.write("ProductionServer > Stopping...");
            try {
                posWindow.close();
                if (threadOrder != null) {
                    threadOrder.terminate();
                    threadOrder.join();
                }
                ServerLog.write("ProductionServer > ThreadOrder stopped");
                if (threadWorking != null) {
                        threadWorking.terminate();
                        threadWorking.join();
                }
                ServerLog.write("ProductionServer > ThreadWorking stopped...");
                if (threadStore != null) {
                    threadStore.terminate();
                    threadStore.join();
                }
                ServerLog.write("ProductionServer > ThreadStore stopped...");
                if (threadReception != null) {
                    threadReception.terminate();
                    threadReception.join();
                }
                ServerLog.write("ProductionServer > ThreadReception stopped...");
                ServerLog.write("ProductionServer > Stopped");

            } catch (InterruptedException ex) {
                    Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void terminate() {
        try {
            if (servSocket != null)
                servSocket.close();
            if (clientSocket != null)
                clientSocket.close();
            servSocket = null;
            clientSocket = null;
            mustStop = true;
        } catch (IOException ex) {
            Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
