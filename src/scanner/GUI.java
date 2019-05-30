package scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GUI.java
 *
 * Created on 25-Dec-2010, 3:20:45 PM
 */


import java.awt.Color;
import java.awt.Frame;

public class GUI extends javax.swing.JDialog {
    //Communicator object
    Communicator communicator = null;
    //KeybindingController object
    KeybindingController keybindingController = null;

    public GUI(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        createObjects();
        communicator.searchForPorts();
        keybindingController.toggleControls();
        keybindingController.bindKeys();
    }
    
    
    
    /** Creates new form GUI */
//    public GUI() {        
//        initComponents();
//        createObjects();
//        communicator.searchForPorts();
//        keybindingController.toggleControls();
//        keybindingController.bindKeys();
//    }

    private void createObjects()
    {
        communicator = new Communicator(this);
        keybindingController = new KeybindingController(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblLeft = new javax.swing.JLabel();
        btnLeftAccel = new javax.swing.JButton();
        btnLeftDecel = new javax.swing.JButton();
        btnRightAccel = new javax.swing.JButton();
        lblRight = new javax.swing.JLabel();
        btnRightDecel = new javax.swing.JButton();
        cboxPorts = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        clearBtn = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Serial COM Port Configuration");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Throttle");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Left");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Right");

        lblLeft.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLeft.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLeft.setText("0");

        btnLeftAccel.setText("/\\");
            btnLeftAccel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLeftAccelActionPerformed(evt);
                }
            });

            btnLeftDecel.setText("\\/");
            btnLeftDecel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLeftDecelActionPerformed(evt);
                }
            });

            btnRightAccel.setText("/\\");
                btnRightAccel.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnRightAccelActionPerformed(evt);
                    }
                });

                lblRight.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                lblRight.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblRight.setText("0");

                btnRightDecel.setText("\\/");
                btnRightDecel.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnRightDecelActionPerformed(evt);
                    }
                });

                jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel5.setText("Select the COM Port from the list");

                btnConnect.setText("Connect");
                btnConnect.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnConnectActionPerformed(evt);
                    }
                });

                btnDisconnect.setText("Disconnect");
                btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        btnDisconnectActionPerformed(evt);
                    }
                });

                jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel6.setText("Controls");

                jLabel7.setText("Q - Accelerate Left");

                jLabel8.setText("A - Decelerate Left");

                jLabel9.setText("W - Accelerate Both");

                jLabel10.setText("S - Decelerate Both");

                jLabel11.setText("S - Decelerate Right");

                jLabel12.setText("W - Accelerate Right");

                jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel13.setText("Log");

                txtLog.setEditable(false);
                txtLog.setColumns(20);
                txtLog.setLineWrap(true);
                txtLog.setRows(5);
                txtLog.setFocusable(false);
                jScrollPane2.setViewportView(txtLog);

                clearBtn.setText("Clear");
                clearBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        clearBtnActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboxPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnConnect)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDisconnect))
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnLeftDecel)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblLeft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnLeftAccel, javax.swing.GroupLayout.Alignment.LEADING)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnRightDecel)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(lblRight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnRightAccel, javax.swing.GroupLayout.Alignment.LEADING)))))
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboxPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConnect)
                            .addComponent(btnDisconnect)
                            .addComponent(clearBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnLeftAccel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblLeft)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnLeftDecel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnRightAccel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblRight)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnRightDecel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel11))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                pack();
            }// </editor-fold>//GEN-END:initComponents

    private void btnLeftAccelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeftAccelActionPerformed
        keybindingController.setLeftThrottle(keybindingController.accelerate(keybindingController.getLeftThrottle()));
        keybindingController.updateLabels();
    }//GEN-LAST:event_btnLeftAccelActionPerformed

    private void btnLeftDecelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeftDecelActionPerformed
        keybindingController.setLeftThrottle(keybindingController.decelerate(keybindingController.getLeftThrottle()));
        keybindingController.updateLabels();
    }//GEN-LAST:event_btnLeftDecelActionPerformed

    private void btnRightAccelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRightAccelActionPerformed
        keybindingController.setRightThrottle(keybindingController.accelerate(keybindingController.getRightThrottle()));
        keybindingController.updateLabels();
    }//GEN-LAST:event_btnRightAccelActionPerformed

    private void btnRightDecelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRightDecelActionPerformed
        keybindingController.setRightThrottle(keybindingController.decelerate(keybindingController.getRightThrottle()));
        keybindingController.updateLabels();
    }//GEN-LAST:event_btnRightDecelActionPerformed

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        communicator.connect();
        if (communicator.getConnected() == true)
        {
            if (communicator.initIOStream() == true)
            {
                communicator.initListener();
            }
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        communicator.disconnect();
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        txtLog.setText("");
    }//GEN-LAST:event_clearBtnActionPerformed
    
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new GUI(this, true).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnConnect;
    public javax.swing.JButton btnDisconnect;
    public javax.swing.JButton btnLeftAccel;
    public javax.swing.JButton btnLeftDecel;
    public javax.swing.JButton btnRightAccel;
    public javax.swing.JButton btnRightDecel;
    public javax.swing.JComboBox cboxPorts;
    private javax.swing.JButton clearBtn;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    public javax.swing.JLabel lblLeft;
    public javax.swing.JLabel lblRight;
    public javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
