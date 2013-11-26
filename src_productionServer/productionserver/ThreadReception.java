/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionserver;

import containerDbAccess.ContainerAccess;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class ThreadReception extends Thread{
    private volatile boolean mustStop = false;
    private ContainerAccess dbAccess;
    private Socket clientSocket;
    private ServerSocket servSocket;


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
                    while(clientSocket.isConnected()) {
                        
                    }
                } catch (SocketException se) {
                    ServerLog.write("ProductionServer > Server socket closed");
                } catch (IOException e) {
                    ServerLog.write("ProductionServer > Client disconnected");
                }
            }
        }
    }
}
