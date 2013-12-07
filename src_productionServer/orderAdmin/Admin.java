/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orderAdmin;

import adminPackets.ListRequest;
import adminPackets.ListResponse;
import adminPackets.PauseRequest;
import adminPackets.PauseResponse;
import adminPackets.StopRequest;
import adminPackets.StopResponse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mike
 */
public class Admin extends javax.swing.JFrame implements ActionListener{

    public boolean adminIsLogged = false;
    public int serverPort = 2001;
    public Socket socket;
    public DataOutputStream dos;
    public DataInputStream dis;
    public String serverState;
    public boolean serverPaused = false;
    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
        this.setLocationByPlatform(true);
        this.connectItem.addActionListener(this);
        this.quitItem.addActionListener(this);
        this.listItem.addActionListener(this);
        this.pauseButton.addActionListener(this);
        this.stopButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(connectItem)) {
            LoginDialog dial = new LoginDialog(this, true);
            dial.setVisible(true);
        }
        if (e.getSource().equals(quitItem)) {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.dispose();
        }
        if (adminIsLogged) {
            if (e.getSource().equals(listItem)) {
                try {
                    ListRequest req = new ListRequest();
                    dos.write(req.networkString().getBytes());
                    String receivedStr = readNetwork();
                    String vectStr[] = receivedStr.split("#");

                    if (vectStr[0].equals("lclients")) {
                        ListResponse resp = new ListResponse(receivedStr);
                        if (resp.ack == true) {
                            System.out.println("Displaying the list of connected clients in a new window");
                            ListClientsDialog dial = new ListClientsDialog(this, true, resp.listClients);
                            dial.setVisible(true);
                        } else {
                            System.err.println("List error: " + resp.cause);
                        }
                    } else {
                        System.err.println("List error: Received unexpected packet");
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (e.getSource().equals(pauseButton)) {
                try {
                    PauseRequest req = new PauseRequest();
                    dos.write(req.networkString().getBytes());
                    String receivedStr = readNetwork();
                    String vectStr[] = receivedStr.split("#");

                    if (vectStr[0].equals("pause")) {
                        PauseResponse resp = new PauseResponse(receivedStr);
                        if (resp.ack == true) {
                            this.serverPaused = resp.serverPaused;
                            if (this.serverPaused == true) {
                                this.stateLabel.setText("Paused");
                                this.pauseButton.setText("Resume");
                            } else {
                                this.stateLabel.setText("Running");
                                this.pauseButton.setText("Pause");
                            }
                        } else {
                            System.err.println("Pause error: " + resp.cause);
                        }
                    } else {
                        System.err.println("Pause error: Received unexpected packet");
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (e.getSource().equals(stopButton)) {
                try {
                    String secondsStr = JOptionPane.showInputDialog("How many seconds before shutdown ?");
                    int seconds = Integer.parseInt(secondsStr);
                    if (seconds < 0) {
                        seconds = 10;
                    }
                    StopRequest req = new StopRequest(seconds);
                    dos.write(req.networkString().getBytes());
                    String receivedStr = readNetwork();
                    String vectStr[] = receivedStr.split("#");

                    if (vectStr[0].equals("stop")) {
                        StopResponse resp = new StopResponse(receivedStr);
                        if (resp.ack == true) {
                            System.out.println("Server shutdown: closing admin...");
                            dos.close();
                            this.dispose();
                            
                        } else {
                            System.err.println("Stop error: " + resp.cause);
                        }
                    } else {
                        System.err.println("Stop error: Received unexpected packet");
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
private String readNetwork() {
    String data = null;
    boolean endingEncountered = false;
    boolean halfEndingEncountered = false;

    if (socket.isConnected()) {
        StringBuffer receiveMsg = new StringBuffer();
        while (!endingEncountered) {
            try {
                byte b = dis.readByte();
                if (b == (byte) '\r') {
                    halfEndingEncountered = true;
                } else if (halfEndingEncountered && (b == (byte) '\n')) {
                    endingEncountered = true;
                } else {
                    receiveMsg.append((char) b);
                    halfEndingEncountered = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        data = receiveMsg.toString().trim();
    }
    return data;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        pauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        connectItem = new javax.swing.JMenuItem();
        quitItem = new javax.swing.JMenuItem();
        listItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Server state: ");

        stateLabel.setText("Stopped");

        pauseButton.setText("Pause");

        stopButton.setText("Stop");

        jMenu1.setText("File");

        connectItem.setText("Connect");
        jMenu1.add(connectItem);

        quitItem.setText("Quit");
        jMenu1.add(quitItem);

        listItem.setText("List clients");
        jMenu1.add(listItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pauseButton)
                        .addGap(55, 55, 55)
                        .addComponent(stopButton)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(stateLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseButton)
                    .addComponent(stopButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem connectItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem listItem;
    private javax.swing.JButton pauseButton;
    private javax.swing.JMenuItem quitItem;
    public javax.swing.JLabel stateLabel;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

    
}
