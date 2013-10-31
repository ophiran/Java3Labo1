package productionserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            ServerSocket servSocket = new ServerSocket(50000);
            clientSocket = servSocket.accept();
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
                    if (req instanceof LoginRequest) {
                        LoginRequest request = (LoginRequest) req;
                        if (request.login.equals("mdavid") && request.password.equals("monpass")) {
                            Response resp = new LoginResponse(true, null);
                            oos.writeObject(resp);
                        } else {
                            Response resp = new LoginResponse(false, "Wrond id");
                        }
                    } else if (req instanceof OrderRequest) {
                        OrderRequest request = (OrderRequest) req;
                        System.out.println(request.partsType.toString() + " : " + request.quantity);
                    } else if (req instanceof CancelRequest) {
                        CancelRequest request = (CancelRequest) req;
                        System.out.println(request.OrderId);
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProductionServer.class.getName()).log(Level.SEVERE, null, ex);
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
