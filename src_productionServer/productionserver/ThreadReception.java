/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionserver;

import containerDbAccess.ContainerAccess;
import dbDataObjects.Client;
import dbDataObjects.Order;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import productionLib.LoginRequest;
import productionLib.LoginResponse;
import productionLib.OrderRequest;
import productionLib.OrderResponse;

/**
 *
 * @author mike
 */
public class ThreadReception extends Thread{
    private volatile boolean mustStop = false;
    private ContainerAccess dbAccess;
    private Socket clientSocket;
    private ServerSocket servSocket;
    private Client clientLogin = null;


    public ThreadReception() {
        dbAccess = ContainerAccess.getInstance();

        try {
            servSocket = new ServerSocket(3000);
            ServerLog.write("ProductionServerOrder > Server running");
        } catch(SocketException se) {
            System.err.println("Error creating serverSocket");
        } catch(IOException e) {
            System.err.println("Error creating serverSocket");
        }
    }

    public void terminate(){
        mustStop = true;
        
        try {
            if (servSocket != null) {
                servSocket.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadReception.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void run() {
        while (!mustStop) {
            try {
                ServerLog.write("ProductionServerOrder > Waiting for a client");
                clientSocket = servSocket.accept();
                ServerLog.write("ProductionServerOrder > Client connected");
            } catch(SocketException se) {
                //System.err.println("Error creating serverSocket");
            } catch(IOException e) {
                System.err.println("Error creating serverSocket");
            }

            if (clientSocket != null) {
                try {
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    boolean endingEncountered = false;
                    boolean halfEndingEncountered = false;
                    
                    while(clientSocket.isConnected()) {
                        StringBuffer receiveMsg = new StringBuffer();
                        while (!endingEncountered) {
                            byte b = dis.readByte();
                            if (b == (byte) '\r') {
                                halfEndingEncountered = true;
                            } else if (halfEndingEncountered && (b == (byte) '\n')) {
                                endingEncountered = true;
                            } else {
                                receiveMsg.append((char) b);
                                halfEndingEncountered = false;
                            }
                        }
                        String data = receiveMsg.toString().trim();
                        ServerLog.write(data);
                        String vectStr[] = data.split("#");
                        
                        if (vectStr[0].equals("login")) {
                            LoginRequest request = new LoginRequest(data);
                            ServerLog.write("Login:" + request.login + " Pwd:" + request.password);
                            clientLogin = new Client(request.login, request.password);
                            if (clientLogin.isAuthorized()) {
                                LoginResponse resp = new LoginResponse(true, null);
                                dos.write(resp.networkString().getBytes());
                                ServerLog.write("ProductionServerOrder > Client " + request.login + " logged");
                            } else {
                                LoginResponse resp = new LoginResponse(false, "Wrong id");
                                dos.write(resp.networkString().getBytes());
                                ServerLog.write("ProductionServerOrder > Client " + request.login + " refused");
                            }
                        } else if (vectStr[0].equals("ordelem")) {
                            
                            OrderRequest request = new OrderRequest(data);
                            ServerLog.write("ProductionServerOrder > Order of " + request.quantity + " " + request.partsType
                                            + " for " + request.desiredDate);
                            if(clientLogin != null){
                                ContainerAccess db = ContainerAccess.getInstance();
                                Client customer = db.getClient(clientLogin.login);
                                int lastProdOrderId = dbAccess.getLastProdOrderId();
                                Order newOrder = new Order(lastProdOrderId + 1, request.desiredDate, customer.getId(),
                                    request.partsType, request.quantity, false);
                                dbAccess.sendProductionOrder(newOrder);
                                ServerLog.write("ProductionServerOrder > A new order has been sent");
                                OrderResponse resp = new OrderResponse(true, "", lastProdOrderId + 1);
                                dos.write(resp.networkString().getBytes());
                            } else {
                                ServerLog.write("ProductionServerOrder > Unknown client");
                                OrderResponse resp = new OrderResponse(false, "Client not logged", -1);
                                dos.write(resp.networkString().getBytes());
                            }
                            
                        } else if (vectStr[0].equals("canorder")) {
                            /*
                            CancelRequest request = (CancelRequest) req;
                            System.out.println(request.OrderId);
                            */
                        }
                        endingEncountered = false;
                    }
                    
                } catch (SocketException se) {
                    ServerLog.write("ProductionServerOrder > Server socket closed");
                } catch (IOException e) {
                    ServerLog.write("ProductionServerOrder > Client disconnected");
                }
            }
        }
    }
}
