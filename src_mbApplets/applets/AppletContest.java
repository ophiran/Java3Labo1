/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package applets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class AppletContest extends javax.swing.JApplet implements ActionListener {

    /**
     * Initializes the applet AppletContest
     */
    @Override
    public void init() {
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
            java.util.logging.Logger.getLogger(AppletContest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppletContest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppletContest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppletContest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        getTicketButton.addActionListener(this);
        backButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(getTicketButton)) {
            try {
                URL currentPage = getDocumentBase();
                int port = currentPage.getPort();
                String protocol = currentPage.getProtocol();
                String host = currentPage.getHost();
                String servletAddress = "/eCommerceApp/ServletContest";
                URL servletUrl = new URL(protocol, host, port, servletAddress);
                URLConnection connection = servletUrl.openConnection();
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setDoOutput(true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
                PrintWriter pw = new PrintWriter(baos, true);

                String toSend = new String("newTicket");
                pw.print(toSend);
                pw.flush();

                String sentLength = String.valueOf(baos.size());
                connection.setRequestProperty("Content-Length", sentLength);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                baos.writeTo(connection.getOutputStream());
                baos.close();


                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = in.readLine();
                resultLabel.setText(result);
            } catch (MalformedURLException ex) {
                Logger.getLogger(AppletContest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AppletContest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(e.getSource().equals(backButton)) {
            try {
                URL currentPage = getDocumentBase();
                int port = currentPage.getPort();
                String protocol = currentPage.getProtocol();
                String host = currentPage.getHost();
                String pageUrl = "/eCommerceApp/jspInit.jsp";
                getAppletContext().showDocument(new URL(protocol, host, port, pageUrl));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AppletContest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        getTicketButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        resultLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        getTicketButton.setText("Get a ticket");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("New Contest !");

        backButton.setText("Come back");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(getTicketButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(resultLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(backButton)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(getTicketButton)
                .addGap(33, 33, 33)
                .addComponent(resultLabel)
                .addGap(39, 39, 39)
                .addComponent(backButton)
                .addContainerGap(54, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton getTicketButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel resultLabel;
    // End of variables declaration//GEN-END:variables


}
