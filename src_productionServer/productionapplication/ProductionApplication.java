/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package productionapplication;

import dbDataObjects.PartsType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import productionLib.CancelRequest;
import productionLib.CancelResponse;
import productionLib.OrderRequest;
import productionserver.ServerLauncher;

/**
 *
 * @author mike
 */
public class ProductionApplication extends javax.swing.JFrame implements ActionListener{
    
    public boolean clientIsLogged = false;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    private Properties appInfo;
    /**
     * Creates new form ProductionApplication
     */
    public ProductionApplication() {
        initComponents();
        this.setLocationByPlatform(true);
        partsTypeCb.setModel(new DefaultComboBoxModel(PartsType.values()));
        
        connectMenuItem.addActionListener(this);
        disconnectMenuItem.addActionListener(this);
        quitMenuItem.addActionListener(this);
        orderButtton.addActionListener(this);
        cancelButton.addActionListener(this);
        appInfo = new Properties();
        File serverInfoFile = new File(System.getProperty("user.dir")
                                     + System.getProperty("file.separator")
                                     + "appProdInfo");
        if (serverInfoFile.exists()) {
            try {
                appInfo.load(new FileInputStream(serverInfoFile));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ServerLauncher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            appInfo.setProperty("serverPort", "50000");
            try {
                appInfo.store(new FileOutputStream(serverInfoFile), "Production server informations");
            } catch (IOException ex) {
                Logger.getLogger(ServerLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(connectMenuItem)) {
            try {
                socket = new Socket("127.0.0.1", Integer.parseInt(appInfo.getProperty("serverPort")));
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                LoginDialog ld = new LoginDialog(this, rootPaneCheckingEnabled);
            ld.setVisible(rootPaneCheckingEnabled);
            }
            catch (UnknownHostException uhe) { 
                System.err.println("Error, could not find host [" + uhe + "]"); 
            }
            catch (IOException ioe) { 
                System.err.println("Error, no connection ? [" + ioe + "]"); 
            }
        }
        if (e.getSource().equals(disconnectMenuItem)) {
            try {
                oos.close();
                ois.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ProductionApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (e.getSource().equals(quitMenuItem)) {
            try {
                if (socket != null) {
                    oos.close();
                    ois.close();
                    socket.close();
                }
                this.dispose();
            } catch (IOException ex) {
                Logger.getLogger(ProductionApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (clientIsLogged) {
            if (e.getSource().equals(orderButtton)) {
                try {
                    String dateString = dateTf.getText();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    df.setLenient(false);
                    Date date = df.parse(dateString);
                    Date today = new Date();
                    if (date.compareTo(today) >= 0) {
                        OrderRequest req = new OrderRequest((PartsType) partsTypeCb.getSelectedItem(), 
                                Integer.parseInt(quantityTf.getText()), date);
                        oos.writeObject(req);
                    } else {
                        JOptionPane.showMessageDialog(this, "Please enter a date after today");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProductionApplication.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a date in the format jj/mm/aaaa");
                    
                }
            }
            if (e.getSource().equals(cancelButton)) {
                try {
                    String orderStr = (String)JOptionPane.showInputDialog(this,
                                    "Enter the order Id", "Cancel an order", JOptionPane.QUESTION_MESSAGE,
                                     null, null, "");
                    CancelRequest req = new CancelRequest(Integer.parseInt(orderStr));
                    oos.writeObject(req);
                    CancelResponse resp = (CancelResponse) ois.readObject();
                    if (resp.ack) {
                        System.out.println("Order canceled");
                    } else {
                        System.out.println("Cancel failed: " + resp.cause);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProductionApplication.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProductionApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        partsTypeCb = new javax.swing.JComboBox();
        quantityTf = new javax.swing.JTextField();
        orderButtton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dateTf = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        disconnectMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("MetalBuilding : Order metal parts");

        jLabel3.setText("Parts type:");

        jLabel4.setText("Quantity:");

        partsTypeCb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        orderButtton.setText("Order");

        cancelButton.setText("Cancel");

        jLabel2.setText("Date:");

        dateTf.setToolTipText("jj/dd/aaaa");

        jMenu1.setText("File");

        connectMenuItem.setText("Connect");
        jMenu1.add(connectMenuItem);

        disconnectMenuItem.setText("Disconnect");
        jMenu1.add(disconnectMenuItem);

        quitMenuItem.setText("Quit");
        jMenu1.add(quitMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(90, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(partsTypeCb, 0, 78, Short.MAX_VALUE)
                            .addComponent(quantityTf)
                            .addComponent(dateTf))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(orderButtton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(49, 49, 49))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(partsTypeCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderButtton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(quantityTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(dateTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(ProductionApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductionApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductionApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductionApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProductionApplication().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JTextField dateTf;
    private javax.swing.JMenuItem disconnectMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton orderButtton;
    private javax.swing.JComboBox partsTypeCb;
    private javax.swing.JTextField quantityTf;
    private javax.swing.JMenuItem quitMenuItem;
    // End of variables declaration//GEN-END:variables
}
