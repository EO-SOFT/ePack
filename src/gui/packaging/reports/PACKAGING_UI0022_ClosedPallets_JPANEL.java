/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.packaging.reports;

import __main__.GlobalVars;
import entity.ConfigFamily;
import gui.packaging.PackagingVars;
import helper.ComboItem;
import helper.Helper;
import helper.JTableHelper;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author Administrator
 */
public class PACKAGING_UI0022_ClosedPallets_JPANEL extends javax.swing.JPanel {

    static JTabbedPane parent;

    public JTabbedPane getParent() {
        return parent;
    }

    public void setParent(JTabbedPane parent) {
        this.parent = parent;
    }
    
    /* "Pallet number" Column index in "container_table" */
    private static int PALLET_NUMBER_COLINDEX = 1;
    Vector<String> closed_result_table_header = new Vector<String>();
    Vector closed_result_table_data = new Vector();
    List<String> table_header = Arrays.asList(
            "Project",
            "Pack Number",
            "CPN",
            "Pack Type",
            "Pack Size",
            "Qty Read",
            "Index",
            "User",
            "Create time",
            "Closed time",
            "Working hours"
    );

    /**
     * Creates new form UI0011_ProdStatistics_
     */
    public PACKAGING_UI0022_ClosedPallets_JPANEL(JTabbedPane parent) {
        //super(parent, modal);
        initComponents();
        this.initGui(parent);
        this.refresh();
//        Helper.centerJDialog(this);
    }

    private void initGui(JTabbedPane parent) {
        this.parent = parent;

        //Desable table edition
        disableEditingTables();

        //Load table header
        load_table_header();

        //Init projects filter
        initProjectsFilter();

        //Initialize double clique on table row
        this.initContainerTableDoubleClick();

    }

    private void initContainerTableDoubleClick() {
        this.closed_result_table.addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (PackagingVars.context.getUser().getAccessLevel() == GlobalVars.PROFIL_ADMIN || PackagingVars.context.getUser().getAccessLevel() == GlobalVars.PROFIL_WAREHOUSE_AGENT) {
                        new PACKAGING_UI0010_PalletDetails_JPANEL(parent, String.valueOf(closed_result_table.getValueAt(closed_result_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), "", 1, true, true, true).setVisible(true);
                    } else {
                        new PACKAGING_UI0010_PalletDetails_JPANEL(parent, String.valueOf(closed_result_table.getValueAt(closed_result_table.getSelectedRow(), PALLET_NUMBER_COLINDEX)), "", 1, false, false, false).setVisible(true);
                    }
                }
            }
        }
        );
    }

    private void load_table_header() {
        this.reset_table_content();

        for (Iterator<String> it = table_header.iterator(); it.hasNext();) {
            closed_result_table_header.add(it.next());
        }

        closed_result_table.setModel(new DefaultTableModel(closed_result_table_data, closed_result_table_header));
    }

    private void initProjectsFilter() {
        List result = new ConfigFamily().select();
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(null, Helper.ERR0014_NO_PROJECT_FOUND, "Configuration error !", ERROR_MESSAGE);
            System.err.println(Helper.ERR0014_NO_PROJECT_FOUND);
        } else { //Map project data in the list
            for (Object o : result) {
                ConfigFamily cp = (ConfigFamily) o;
                harness_type_filter.addItem(new ComboItem(cp.getHarnessType(), cp.getHarnessType()));
            }
        }
    }

    public void reset_table_content() {
        closed_result_table_data = new Vector();
        DefaultTableModel openDataModel = new DefaultTableModel(closed_result_table_data, closed_result_table_header);
        closed_result_table.setModel(openDataModel);
    }

    public void disableEditingTables() {
        for (int c = 0; c < closed_result_table.getColumnCount(); c++) {
            // remove editor   
            Class<?> col_class2 = closed_result_table.getColumnClass(c);
            closed_result_table.setDefaultEditor(col_class2, null);
        }
    }

    @SuppressWarnings("empty-statement")
    public void reload_closed_result_table_data(List<Object[]> resultList) {

        for (Object[] obj : resultList) {

            Vector<Object> oneRow = new Vector<Object>();

            oneRow.add(String.valueOf(obj[0])); // Project
            oneRow.add(String.valueOf(obj[1])); // Pallet Number
            oneRow.add(String.valueOf(obj[2])); // Harness Part                        
            oneRow.add(String.valueOf(obj[3])); // Pack Type
            oneRow.add(String.valueOf(obj[4])); // Qty Expected
            oneRow.add(String.valueOf(obj[5])); // Qty Read
            oneRow.add(String.valueOf(obj[6])); // Index
            oneRow.add(String.valueOf(obj[7])); // USER            
            oneRow.add(String.valueOf(obj[8])); // Create Time            
            oneRow.add(String.valueOf(obj[9])); // Closed Time
            oneRow.add(String.valueOf(obj[10])); // Working hours                        
            closed_result_table_data.add(oneRow);
        }
        closed_result_table.setModel(new DefaultTableModel(closed_result_table_data, closed_result_table_header));
        closed_result_table.setFont(new Font(String.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONT")), Font.BOLD, Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_FONTSIZE"))));
        closed_result_table.setRowHeight(Integer.valueOf(GlobalVars.APP_PROP.getProperty("JTABLE_ROW_HEIGHT")));
    }

    private void refresh() {

        //Clear all tables
        this.reset_table_content();

        List<Object> projects = new ArrayList<Object>();
        projects.add("1");

        if (String.valueOf(harness_type_filter.getSelectedItem()).equals("ALL")) {
            for (int i = 0; i < harness_type_filter.getItemCount(); i++) {
                projects.add(String.valueOf(harness_type_filter.getItemAt(i)));
            }
        } else {
            projects.add(String.valueOf(harness_type_filter.getSelectedItem()));
        }

        try {

            //################# Dropped Harness Data #################### 
            Helper.startSession();
            String query_str = " SELECT bc.harness_type AS project, "
                    + " bc.pallet_number AS pack_number, "
                    + " bc.harness_part AS harness_part, "
                    + " bc.pack_type AS pack_type, "
                    + " bc.qty_expected AS pack_size, "
                    + " bc.qty_read AS qty_read, "
                    + " bc.harness_index AS index, "
                    + " bc.m_user AS user, "
                    + " bc.create_time as create_time, "
                    + " bc.closed_time as closed_time, "
                    + " DATE_PART('hours', closed_time - create_time ) AS working_hours "
                    + " FROM base_container bc "
                    + " WHERE bc.harness_type IN (:projects) "
                    + " AND bc.container_state = '%s' ORDER BY project ASC, working_hours ASC ";

            query_str = String.format(query_str, GlobalVars.PALLET_CLOSED);
            SQLQuery query = Helper.sess.createSQLQuery(query_str);

            query.addScalar("project", StandardBasicTypes.STRING)
                    .addScalar("pack_number", StandardBasicTypes.STRING)
                    .addScalar("harness_part", StandardBasicTypes.STRING)
                    .addScalar("pack_type", StandardBasicTypes.STRING)
                    .addScalar("pack_size", StandardBasicTypes.INTEGER)
                    .addScalar("qty_read", StandardBasicTypes.INTEGER)
                    .addScalar("index", StandardBasicTypes.STRING)
                    .addScalar("user", StandardBasicTypes.STRING)
                    .addScalar("create_time", StandardBasicTypes.TIMESTAMP)
                    .addScalar("closed_time", StandardBasicTypes.TIMESTAMP)
                    //.addScalar("closed_time", StandardBasicTypes.TIMESTAMP)
                    //.addScalar("create_time", StandardBasicTypes.TIMESTAMP)
                    .addScalar("working_hours", StandardBasicTypes.DOUBLE)
                    .setParameterList("projects", projects);

            List<Object[]> closedResultList = query.list();

            Helper.sess.getTransaction().commit();

            this.reload_closed_result_table_data(closedResultList);

            this.disableEditingTables();
            JTableHelper.sizeColumnsToFit(closed_result_table);

        } catch (HibernateException e) {
            if (Helper.sess.getTransaction() != null) {
                Helper.sess.getTransaction().rollback();
            }
            e.printStackTrace();
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

        north_panel = new javax.swing.JPanel();
        refresh_btn = new javax.swing.JButton();
        result_table_scroll = new javax.swing.JScrollPane();
        closed_result_table = new javax.swing.JTable();
        harness_type_filter = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(36, 65, 86));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        north_panel.setBackground(new java.awt.Color(36, 65, 86));
        north_panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                north_panelKeyPressed(evt);
            }
        });

        refresh_btn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refresh_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/refresh.png"))); // NOI18N
        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        closed_result_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        result_table_scroll.setViewportView(closed_result_table);

        harness_type_filter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        harness_type_filter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL" }));
        harness_type_filter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                harness_type_filterItemStateChanged(evt);
            }
        });
        harness_type_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harness_type_filterActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Liste des palettes fermées");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Projet");

        javax.swing.GroupLayout north_panelLayout = new javax.swing.GroupLayout(north_panel);
        north_panel.setLayout(north_panelLayout);
        north_panelLayout.setHorizontalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(result_table_scroll)
            .addComponent(jSeparator1)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, north_panelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(harness_type_filter, 0, 1, Short.MAX_VALUE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(685, 685, 685))
        );
        north_panelLayout.setVerticalGroup(
            north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(north_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(north_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(harness_type_filter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(refresh_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(result_table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(north_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(north_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refresh_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed

        refresh();

    }//GEN-LAST:event_refresh_btnActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_formKeyPressed

    private void north_panelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_north_panelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_north_panelKeyPressed

    private void harness_type_filterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_harness_type_filterItemStateChanged
        //
    }//GEN-LAST:event_harness_type_filterItemStateChanged

    private void harness_type_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harness_type_filterActionPerformed
        refresh();
    }//GEN-LAST:event_harness_type_filterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable closed_result_table;
    private javax.swing.JComboBox harness_type_filter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel north_panel;
    private javax.swing.JButton refresh_btn;
    private javax.swing.JScrollPane result_table_scroll;
    // End of variables declaration//GEN-END:variables
}
