/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.config;

import entity.PackagingConfig;
import entity.PackagingItems;
import entity.PackagingMaster;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.exception.ConstraintViolationException;
import ui.UILog;

/**
 *
 * @author Administrator
 */
public class CONFIG_UI0002_CONFIG_PACK_MASTERDATA_JPANEL extends javax.swing.JPanel {

    /**
     * Les méthodes JTable qitemsAuxi sitemsAuxivent doivent être dans
     * itemsAuxne class interface initGitemsAuxi()
     * initContainerTableDoitemsAuxbleClick load_items_table_header
     * reset_table_content disableEditingTable refresh
     *
     * Les 4 champs qitemsAuxi sitemsAuxivent doivent être dans itemsAuxne class
     * interface
     */
    Vector<String> pack_items_table_header = new Vector<String>();
    Vector<String> pack_masters_table_header = new Vector<String>();
    Vector<String> pack_config_table_header = new Vector<String>();

    List<String> items_table_header = Arrays.asList(
            "#",
            "Element",
            "Part number interne",
            "Description",
            "Dim UOM",
            "Hauteur",
            "Largeur",
            "Longueur",
            "Poids",
            "Poids UOM",
            "Quantité d'alerte",
            "Quantité stock"
    );

    List<String> masters_table_header = Arrays.asList(
            "#",
            "Pack Master",
            "Part number interne",
            "Description",
            "Dim UOM",
            "Hauteur",
            "Largeur",
            "Longueur",
            "Poids",
            "Poids UOM"
    );

    List<String> config_table_header = Arrays.asList(
            "#",
            "Pack Master",
            "Part Item",
            "Coefficient"
    );

    Vector pack_items_table_data = new Vector();
    Vector pack_masters_table_data = new Vector();
    Vector pack_config_table_data = new Vector();

    public List<Object[]> itemsResultList;
    public List<Object[]> mastersResultList;
    public List<Object[]> configsResultList;

    PackagingItems itemsAux;
    PackagingMaster mastersAux;
    PackagingConfig configAux;

    public CONFIG_UI0002_CONFIG_PACK_MASTERDATA_JPANEL(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        initGui();

    }

    private void initGui() {
        //Center the this dialog in the screen
//        Helper.centerJFrame(this);

        //Support double click on rows in container jtable to display history
        //Load table header
        initItemsTabUIjtableHeader();
        this.initItemsTabUI();

        initMastersTabUIjtableHeader();
        this.initMastersTabUI();

        initConfigTabUIjtableHeader();
        this.initConfigTabUI();

    }
    //refreshConfigs()

    private void initItemsTabUI() {
        resetItemsJtableContent();
        refreshItems();
        initItemsjTableDoubleClick();
        disableItemsEditingTable();
    }

    private void initMastersTabUI() {
        resetMastersJtableContent();
        refreshMasters();
        initMastersjTableDoubleClick();
        disableMastersEditingTable();
        //init stock qty of packaging items  field
        stock_quantity_txtbox.setEditable(false);
    }

    private void initConfigTabUI() {
        //Initialize jbox 1 time
        //init config pack items jbox
        initPackItemsBox();
        //init config pack Master jbox
        pack_master_box = PackagingMaster.initPackMasterJBox(this, pack_master_box, false);
        
        resetConfigJtableContent();
        refreshConfigs();
        initConfigsjTableDoubleClick();
        disableConfigsEditingTable();

        //Init coefficient textBox
        coefficient_txt.setText("1.00");
    }

    private void initPackItemsBox() {
        pack_items_box.removeAllItems();
        List result = new PackagingItems().selectAllPackItems();
        //Map project data in the list
        for (Object o : result) {
            PackagingItems pc = (PackagingItems) o;
            //pack_items_box.addItem(new ComboItem(pc.getPackItem(), pc.getPackItem()));
            pack_items_box.addItem(pc.getPackItem());
        }

    }

    /**
     * Reset the content of Packagin Items jtable header
     */
    private void initItemsTabUIjtableHeader() {
        for (Iterator<String> it = items_table_header.iterator(); it.hasNext();) {
            pack_items_table_header.add(it.next());
        }

        pack_items_jtable.setModel(new DefaultTableModel(pack_items_table_data, pack_items_table_header));
    }

    /**
     * Reset the content of Packagin Items jtable header
     */
    private void initMastersTabUIjtableHeader() {
        for (Iterator<String> it = masters_table_header.iterator(); it.hasNext();) {
            pack_masters_table_header.add(it.next());
        }

        pack_masters_jtable.setModel(new DefaultTableModel(pack_masters_table_data, pack_masters_table_header));
    }

    /**
     * Reset the content of Packagin Items jtable header
     */
    private void initConfigTabUIjtableHeader() {
        for (Iterator<String> it = config_table_header.iterator(); it.hasNext();) {
            pack_config_table_header.add(it.next());
        }

        config_jtable.setModel(new DefaultTableModel(pack_config_table_data, pack_config_table_header));
    }

    private void initItemsjTableDoubleClick() {
        //Packaging items jTable double clique
        this.pack_items_jtable.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_PACK_ITEM_BY_ID);
                    query.setParameter("id", Integer.valueOf(pack_items_jtable.getValueAt(pack_items_jtable.getSelectedRow(), 0).toString()));
                    Helper.sess.getTransaction().commit();
                    itemsAux = (PackagingItems) query.list().get(0);
                    id_lbl.setText(itemsAux.getId().toString());
                    pack_item_txtbox.setText(itemsAux.getPackItem());
                    internal_pn_txtbox.setText(itemsAux.getItemInternPN());
                    dim_uom_txtbox.setText(itemsAux.getDimensionUOM());
                    width_txtbox.setText(itemsAux.getItemWidth() + "");
                    height_txtbox.setText(itemsAux.getItemHeight() + "");
                    length_txtbox.setText(itemsAux.getItemLength() + "");
                    weight_uom_txtbox.setText(itemsAux.getWeightUOM());
                    weight_txtbox.setText(itemsAux.getItemWeight() + "");
                    alert_quantity_txtbox.setText(itemsAux.getAlertQty() + "");
                    //stock_quantity_txtbox.setText(itemsAux.getQty() + "");
                    description_txtbox.setText(itemsAux.getDescription());
                    master_delete_btn.setEnabled(true);
                    item_duplicate_btn.setEnabled(true);
                }
            }
        }
        );
    }

    private void initMastersjTableDoubleClick() {
        //Packaging master jTable double clique
        this.pack_masters_jtable.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_PACK_MASTER_BY_ID);
                    query.setParameter("id", Integer.valueOf(pack_masters_jtable.getValueAt(pack_masters_jtable.getSelectedRow(), 0).toString()));
                    Helper.sess.getTransaction().commit();
                    mastersAux = (PackagingMaster) query.list().get(0);
                    master_id_lbl.setText(mastersAux.getId().toString());
                    pack_master_txtbox.setText(mastersAux.getPackMaster());
                    master_internal_pn_txtbox.setText(mastersAux.getPackInternPN());
                    master_dim_uom_txtbox.setText(mastersAux.getDimensionUOM());
                    master_width_txtbox.setText(mastersAux.getPackWidth() + "");
                    master_height_txtbox.setText(mastersAux.getPackHeight() + "");
                    master_length_txtbox.setText(mastersAux.getPackLength() + "");
                    master_weight_uom_txtbox.setText(mastersAux.getWeightUOM());
                    master_weight_txtbox.setText(mastersAux.getPackWeight() + "");
                    master_description_txtbox.setText(mastersAux.getDescription());
                    master_volume_txtbox.setText(mastersAux.getVolume() + "");
                    master_delete_btn.setEnabled(true);
                    master_duplicate_btn.setEnabled(true);
                }
            }
        }
        );
    }

    private void initConfigsjTableDoubleClick() {
        this.config_jtable.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Helper.startSession();
                    Query query = Helper.sess.createQuery(HQLHelper.GET_CONFIG_BY_ID);
                    query.setParameter("id", Integer.valueOf(config_jtable.getValueAt(config_jtable.getSelectedRow(), 0).toString()));
                    Helper.sess.getTransaction().commit();
                    configAux = (PackagingConfig) query.list().get(0);
                    config_id_lbl.setText(configAux.getId().toString());
                    coefficient_txt.setText(configAux.getCoefficient() + "");

                    for (int i = 0; i < pack_master_box.getItemCount(); i++) {
                        if (pack_master_box.getItemAt(i).toString().equals(configAux.getPackMaster())) {
                            pack_master_box.setSelectedIndex(i);
                            break;
                        }
                    }

                    for (int i = 0; i < pack_items_box.getItemCount(); i++) {
                        if (pack_items_box.getItemAt(i).toString().equals(configAux.getPackItem())) {
                            pack_items_box.setSelectedIndex(i);
                            break;
                        }
                    }

                    config_delete_btn.setEnabled(true);
                }
            }
        }
        );
    }

    public void resetItemsJtableContent() {
        //Reset the content of packaging items jtable
        pack_items_table_data = new Vector();
        DefaultTableModel itemsDataModel = new DefaultTableModel(pack_items_table_data, pack_items_table_header);
        pack_items_jtable.removeAll();
        pack_items_jtable.setModel(itemsDataModel);

    }

    public void resetMastersJtableContent() {
        //Reset the content of packaging masters jtable
        pack_masters_table_data = new Vector();
        DefaultTableModel mastersDataModel = new DefaultTableModel(pack_masters_table_data, pack_masters_table_header);
        pack_masters_jtable.setModel(mastersDataModel);

    }

    public void resetConfigJtableContent() {
        //Reset the content of packaging masters jtable
        pack_config_table_data = new Vector();
        DefaultTableModel configDataModel = new DefaultTableModel(pack_config_table_data, pack_config_table_header);
        config_jtable.setModel(configDataModel);

    }

    public void disableItemsEditingTable() {
        //Disable Editing items table
        for (int c = 0; c < pack_items_jtable.getColumnCount(); c++) {
            Class<?> col_class = pack_items_jtable.getColumnClass(c);
            pack_items_jtable.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    public void disableMastersEditingTable() {
        //Disable Editing masters table
        for (int c = 0; c < pack_masters_jtable.getColumnCount(); c++) {
            Class<?> col_class = pack_masters_jtable.getColumnClass(c);
            pack_masters_jtable.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    public void disableConfigsEditingTable() {
        //Disable Editing masters table
        for (int c = 0; c < config_jtable.getColumnCount(); c++) {
            Class<?> col_class = config_jtable.getColumnClass(c);
            config_jtable.setDefaultEditor(col_class, null);        // remove editor            
        }
    }

    /**
     * @param table_data
     * @param table_header
     * @param jtable
     * @todo : reload_table_data a mettre dans une classe interface
     * @param resultList
     */
    public void reload_table_data(List<Object[]> resultList, Vector table_data, Vector<String> table_header, JTable jtable) {

        for (Object[] line : resultList) {
            @SuppressWarnings("UseOfObsoleteCollectionType")
            Vector<Object> oneRow = new Vector<Object>();
            for (Object cell : line) {
                oneRow.add(String.valueOf(cell));
            }
            table_data.add(oneRow);
        }

        jtable.setModel(new DefaultTableModel(table_data, table_header));
        jtable.setAutoCreateRowSorter(true);
    }

    private void clearPackItemsTabFields() {
        id_lbl.setText("");
        pack_item_txtbox.setText("");
        internal_pn_txtbox.setText("");
        dim_uom_txtbox.setText("cm");
        width_txtbox.setText("0.00");
        height_txtbox.setText("0.00");
        width_txtbox.setText("0.00");
        weight_uom_txtbox.setText("kg");
        weight_txtbox.setText("0.00");
        description_txtbox.setText("");
        stock_quantity_txtbox.setText("0.00");
        alert_quantity_txtbox.setText("0.00");

        master_delete_btn.setEnabled(false);
        item_duplicate_btn.setEnabled(false);
        items_tab_msg_lbl.setText("");
        itemsAux = null;
    }

    private void clearPackMastersTabFields() {
        master_id_lbl.setText("");
        pack_master_txtbox.setText("");
        master_internal_pn_txtbox.setText("");
        master_dim_uom_txtbox.setText("cm");
        master_width_txtbox.setText("0.00");
        master_height_txtbox.setText("0.00");
        master_width_txtbox.setText("0.00");
        master_weight_uom_txtbox.setText("kg");
        master_weight_txtbox.setText("0.00");
        master_description_txtbox.setText("");

        master_delete_btn.setEnabled(false);
        master_duplicate_btn.setEnabled(false);
        masters_tab_msg_lbl.setText("");
        master_volume_txtbox.setText("0.00");
        mastersAux = null;
    }

    private void clearConfigTabFields() {
        this.config_id_lbl.setText("");
        this.pack_master_box.setSelectedIndex(0);
        this.pack_items_box.setSelectedIndex(0);
        this.coefficient_txt.setText("1.00");
        configAux = null;
        config_delete_btn.setEnabled(false);
    }

    private void clearItemsSearchFields() {
        pack_item_search.setText("");
        internal_pn_txtbox_search.setText("");
    }

    private void clearMastersSearchFields() {
        pack_master_search.setText("");
        master_internal_pn_txtbox_search.setText("");
    }

    private void clearConfigSearchFields() {
        config_pack_master_search.setText("");
        config_pack_item_search.setText("");
    }

    private void refreshItems() {
        String query_str = " SELECT "
                + " u.id AS id, "
                + " u.pack_item AS pack_item, "
                + " u.item_intern_pn AS item_intern_pn, "
                + " u.description AS description, "
                + " u.dimension_uom AS dimension_uom, "
                + " u.item_height AS item_height, "
                + " u.item_width AS item_width, "
                + " u.item_length AS item_length, "
                + " u.weight_uom AS weight_uom, "
                + " u.item_weight AS item_weight, "
                + " u.alert_qty AS alert_qty "
                + " FROM Packaging_Items u WHERE 1=1 ";
        Helper.startSession();
        if (!pack_item_search.getText().trim().equals("")) {
            query_str += " AND pack_item LIKE '%" + pack_item_search.getText() + "%'";
        }
        if (!internal_pn_txtbox_search.getText().trim().equals("")) {
            query_str += " AND item_intern_pn LIKE '%" + internal_pn_txtbox_search.getText() + "%'";
        }

        query_str += " ORDER BY id ASC ";
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        itemsResultList = query.list();

        Helper.sess.getTransaction().commit();

        //Clear the items Jtable first
        this.resetItemsJtableContent();

        this.reload_table_data(itemsResultList, pack_items_table_data, pack_items_table_header, pack_items_jtable);

        this.disableItemsEditingTable();
    }

    private void refreshMasters() {
        String query_str = " SELECT "
                + " u.id AS id, "
                + " u.pack_master AS pack_master, "
                + " u.pack_intern_pn AS pack_intern_pn, "
                + " u.description AS description, "
                + " u.dimension_uom AS dimension_uom, "
                + " u.pack_height AS pack_height, "
                + " u.pack_width AS pack_width, "
                + " u.pack_length AS pack_length, "
                + " u.weight_uom AS weight_uom, "
                + " u.pack_weight AS pack_weight "
                + " FROM Packaging_Master u WHERE 1=1 ";
        Helper.startSession();
        if (!pack_master_search.getText().trim().equals("")) {
            query_str += " AND pack_master LIKE '%" + pack_master_search.getText() + "%'";
        }
        if (!master_internal_pn_txtbox_search.getText().trim().equals("")) {
            query_str += " AND pack_intern_pn LIKE '%" + master_internal_pn_txtbox_search.getText() + "%'";
        }

        query_str += " ORDER BY id ASC ";
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        mastersResultList = query.list();

        Helper.sess.getTransaction().commit();

        //Clear the masters Jtable first
        this.resetMastersJtableContent();

        this.reload_table_data(mastersResultList, pack_masters_table_data, pack_masters_table_header, pack_masters_jtable);

        this.disableMastersEditingTable();
    }

    private void refreshConfigs() {
        String query_str = " SELECT "
                + " u.id AS id, "
                + " u.pack_master AS pack_master, "
                + " u.pack_item AS pack_item, "
                + " u.coefficient AS coefficient "
                + " FROM Packaging_Config u WHERE 1=1 ";
        Helper.startSession();
        if (!config_pack_master_search.getText().trim().equals("")) {
            query_str += " AND pack_master LIKE '%" + config_pack_master_search.getText().trim() + "%'";
        }
        if (!config_pack_item_search.getText().trim().equals("")) {
            query_str += " AND pack_item LIKE '%" + config_pack_item_search.getText().trim() + "%'";
        }

        query_str += " ORDER BY pack_master ASC ";
        SQLQuery query = Helper.sess.createSQLQuery(query_str);
        configsResultList = query.list();

        Helper.sess.getTransaction().commit();

        //Clear the config Jtable first
        this.resetConfigJtableContent();

        this.reload_table_data(configsResultList, pack_config_table_data, pack_config_table_header, config_jtable);

        this.disableConfigsEditingTable();
    }

    /**
     * This method is called from within the constritemsAuxctor to initialize
     * the form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        fname_lbl4 = new javax.swing.JLabel();
        pack_master_txtbox = new javax.swing.JTextField();
        lname_lbl2 = new javax.swing.JLabel();
        master_dim_uom_txtbox = new javax.swing.JTextField();
        master_save_btn = new javax.swing.JButton();
        master_cancel_btn = new javax.swing.JButton();
        master_id_lbl = new javax.swing.JLabel();
        master_weight_uom_txtbox = new javax.swing.JTextField();
        pwd_lbl5 = new javax.swing.JLabel();
        master_weight_txtbox = new javax.swing.JTextField();
        pwd_lbl9 = new javax.swing.JLabel();
        master_width_txtbox = new javax.swing.JTextField();
        master_duplicate_btn = new javax.swing.JButton();
        masters_tab_msg_lbl = new javax.swing.JLabel();
        fname_lbl5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        master_description_txtbox = new javax.swing.JTextArea();
        fname_lbl6 = new javax.swing.JLabel();
        lname_lbl3 = new javax.swing.JLabel();
        pwd_lbl10 = new javax.swing.JLabel();
        master_length_txtbox = new javax.swing.JTextField();
        master_height_txtbox = new javax.swing.JTextField();
        pwd_lbl11 = new javax.swing.JLabel();
        master_internal_pn_txtbox = new javax.swing.JTextField();
        fname_lbl7 = new javax.swing.JLabel();
        pwd_lbl12 = new javax.swing.JLabel();
        master_volume_txtbox = new javax.swing.JTextField();
        master_cancel_btn1 = new javax.swing.JButton();
        user_table_scroll1 = new javax.swing.JScrollPane();
        pack_masters_jtable = new javax.swing.JTable();
        master_clear_search_btn = new javax.swing.JButton();
        master_refresh_btn = new javax.swing.JButton();
        master_internal_pn_txtbox_search = new javax.swing.JTextField();
        lname_lbl_search1 = new javax.swing.JLabel();
        pack_master_search = new javax.swing.JTextField();
        fname_lbl_search1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        fname_lbl8 = new javax.swing.JLabel();
        config_master_save_btn = new javax.swing.JButton();
        config_master_cancel_btn = new javax.swing.JButton();
        config_delete_btn = new javax.swing.JButton();
        config_id_lbl = new javax.swing.JLabel();
        fname_lbl9 = new javax.swing.JLabel();
        fname_lbl11 = new javax.swing.JLabel();
        pack_master_box = new javax.swing.JComboBox();
        pack_items_box = new javax.swing.JComboBox();
        coefficient_txt = new javax.swing.JTextField();
        fname_lbl12 = new javax.swing.JLabel();
        config_tab_msg_lbl = new javax.swing.JLabel();
        config_master_clear_search_btn = new javax.swing.JButton();
        config_master_refresh_btn = new javax.swing.JButton();
        config_pack_item_search = new javax.swing.JTextField();
        lname_lbl_search2 = new javax.swing.JLabel();
        config_pack_master_search = new javax.swing.JTextField();
        fname_lbl_search2 = new javax.swing.JLabel();
        user_table_scroll2 = new javax.swing.JScrollPane();
        config_jtable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        fname_lbl = new javax.swing.JLabel();
        pack_item_txtbox = new javax.swing.JTextField();
        lname_lbl = new javax.swing.JLabel();
        dim_uom_txtbox = new javax.swing.JTextField();
        item_save_btn = new javax.swing.JButton();
        item_cancel_btn = new javax.swing.JButton();
        master_delete_btn = new javax.swing.JButton();
        id_lbl = new javax.swing.JLabel();
        weight_uom_txtbox = new javax.swing.JTextField();
        pwd_lbl4 = new javax.swing.JLabel();
        weight_txtbox = new javax.swing.JTextField();
        login_lbl2 = new javax.swing.JLabel();
        alert_quantity_txtbox = new javax.swing.JTextField();
        pwd_lbl6 = new javax.swing.JLabel();
        width_txtbox = new javax.swing.JTextField();
        item_duplicate_btn = new javax.swing.JButton();
        items_tab_msg_lbl = new javax.swing.JLabel();
        fname_lbl1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description_txtbox = new javax.swing.JTextArea();
        stock_quantity_txtbox = new javax.swing.JTextField();
        login_lbl4 = new javax.swing.JLabel();
        fname_lbl2 = new javax.swing.JLabel();
        lname_lbl1 = new javax.swing.JLabel();
        pwd_lbl7 = new javax.swing.JLabel();
        length_txtbox = new javax.swing.JTextField();
        height_txtbox = new javax.swing.JTextField();
        pwd_lbl8 = new javax.swing.JLabel();
        internal_pn_txtbox = new javax.swing.JTextField();
        fname_lbl3 = new javax.swing.JLabel();
        fname_lbl_search = new javax.swing.JLabel();
        pack_item_search = new javax.swing.JTextField();
        lname_lbl_search = new javax.swing.JLabel();
        internal_pn_txtbox_search = new javax.swing.JTextField();
        refresh_btn = new javax.swing.JButton();
        clear_search_btn = new javax.swing.JButton();
        user_table_scroll = new javax.swing.JScrollPane();
        pack_items_jtable = new javax.swing.JTable();

        setAutoscrolls(true);

        tabs.setBackground(new java.awt.Color(204, 204, 204));
        tabs.setAutoscrolls(true);
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsStateChanged(evt);
            }
        });
        tabs.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabsComponentShown(evt);
            }
        });
        tabs.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tabsPropertyChange(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(36, 65, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Packaging Master", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setToolTipText("Packaging Master");
        jPanel2.setAutoscrolls(true);
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.setName(""); // NOI18N

        fname_lbl4.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl4.setText("Pack master *");

        pack_master_txtbox.setName("pack_item_txtbox"); // NOI18N

        lname_lbl2.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl2.setText("Dimension UOM cm *");

        master_dim_uom_txtbox.setText("0.00");
        master_dim_uom_txtbox.setName("fname_txtbox"); // NOI18N

        master_save_btn.setText("Enregistrer");
        master_save_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_save_btnActionPerformed(evt);
            }
        });

        master_cancel_btn.setText("Réinitialiser champs");
        master_cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_cancel_btnActionPerformed(evt);
            }
        });

        master_id_lbl.setBackground(new java.awt.Color(153, 204, 255));
        master_id_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        master_id_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        master_id_lbl.setRequestFocusEnabled(false);

        master_weight_uom_txtbox.setText("0.00");
        master_weight_uom_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl5.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl5.setText("Poids *");

        master_weight_txtbox.setText("0.00");
        master_weight_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl9.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl9.setText("Largeur*");

        master_width_txtbox.setText("0.00");
        master_width_txtbox.setName("fname_txtbox"); // NOI18N

        master_duplicate_btn.setText("Dupliquer");
        master_duplicate_btn.setEnabled(false);
        master_duplicate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_duplicate_btnActionPerformed(evt);
            }
        });

        masters_tab_msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        masters_tab_msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        fname_lbl5.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl5.setText("ID");

        master_description_txtbox.setColumns(10);
        master_description_txtbox.setRows(3);
        jScrollPane2.setViewportView(master_description_txtbox);

        fname_lbl6.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl6.setText("Description");

        lname_lbl3.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl3.setText("Poids UOM (kg) *");

        pwd_lbl10.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl10.setText("Longueur *");

        master_length_txtbox.setText("0.00");
        master_length_txtbox.setName("fname_txtbox"); // NOI18N

        master_height_txtbox.setText("0.00");
        master_height_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl11.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl11.setText("Hauteur *");

        master_internal_pn_txtbox.setName("pack_item_txtbox"); // NOI18N

        fname_lbl7.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl7.setText("Internal Part Number *");

        pwd_lbl12.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl12.setText("Volume m3");

        master_volume_txtbox.setEditable(false);
        master_volume_txtbox.setText("0.00");
        master_volume_txtbox.setName("fname_txtbox"); // NOI18N

        master_cancel_btn1.setBackground(new java.awt.Color(204, 0, 0));
        master_cancel_btn1.setForeground(new java.awt.Color(255, 255, 255));
        master_cancel_btn1.setText("Supprimer");
        master_cancel_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_cancel_btn1ActionPerformed(evt);
            }
        });

        pack_masters_jtable.setModel(new javax.swing.table.DefaultTableModel(
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
        pack_masters_jtable.setMinimumSize(new java.awt.Dimension(300, 64));
        user_table_scroll1.setViewportView(pack_masters_jtable);

        master_clear_search_btn.setText("Réinitialiser champs");
        master_clear_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_clear_search_btnActionPerformed(evt);
            }
        });

        master_refresh_btn.setText("Actualiser");
        master_refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_refresh_btnActionPerformed(evt);
            }
        });

        master_internal_pn_txtbox_search.setName("fname_txtbox"); // NOI18N
        master_internal_pn_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                master_internal_pn_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                master_internal_pn_txtbox_searchKeyTyped(evt);
            }
        });

        lname_lbl_search1.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl_search1.setText("Internal Part Number");

        pack_master_search.setName("fname_txtbox"); // NOI18N
        pack_master_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pack_master_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pack_master_searchKeyTyped(evt);
            }
        });

        fname_lbl_search1.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl_search1.setText("Pack Master");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(master_save_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(master_duplicate_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(master_cancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(125, 125, 125)
                                        .addComponent(master_cancel_btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(fname_lbl4)
                                            .addComponent(fname_lbl5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pack_master_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(55, 55, 55)
                                                .addComponent(fname_lbl7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(master_internal_pn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(master_id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lname_lbl2)
                                            .addComponent(lname_lbl3))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(master_dim_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(master_weight_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pwd_lbl5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(master_weight_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pwd_lbl9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(master_width_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pwd_lbl10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(master_length_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32)
                                                .addComponent(pwd_lbl11))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(pwd_lbl12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(master_volume_txtbox, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(master_height_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(432, 432, 432))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(user_table_scroll1)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(fname_lbl_search1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pack_master_search, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lname_lbl_search1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(master_internal_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(master_refresh_btn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(master_clear_search_btn))
                                    .addComponent(masters_tab_msg_lbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(195, 195, 195)))
                        .addGap(131, 131, 131))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(fname_lbl6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fname_lbl5))
                    .addComponent(master_id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(master_internal_pn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl7)
                    .addComponent(pack_master_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwd_lbl9)
                    .addComponent(master_width_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lname_lbl2)
                    .addComponent(master_dim_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl10)
                    .addComponent(master_length_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl11)
                    .addComponent(master_height_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(master_weight_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl5)
                    .addComponent(master_weight_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lname_lbl3)
                    .addComponent(pwd_lbl12)
                    .addComponent(master_volume_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fname_lbl6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(master_cancel_btn)
                    .addComponent(master_save_btn)
                    .addComponent(master_duplicate_btn)
                    .addComponent(master_cancel_btn1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(masters_tab_msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(master_refresh_btn)
                        .addComponent(master_clear_search_btn))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pack_master_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fname_lbl_search1)
                        .addComponent(lname_lbl_search1)
                        .addComponent(master_internal_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(user_table_scroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        tabs.addTab("Packaging Master", jPanel2);

        jPanel3.setBackground(new java.awt.Color(36, 65, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configuration Packaging", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setToolTipText("Configuration Packaging");
        jPanel3.setAutoscrolls(true);
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.setName(""); // NOI18N

        fname_lbl8.setBackground(new java.awt.Color(255, 255, 255));
        fname_lbl8.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl8.setText("Pack master *");

        config_master_save_btn.setText("Enregistrer");
        config_master_save_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_master_save_btnActionPerformed(evt);
            }
        });

        config_master_cancel_btn.setText("Réinitialiser champs");
        config_master_cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_master_cancel_btnActionPerformed(evt);
            }
        });

        config_delete_btn.setBackground(new java.awt.Color(255, 51, 51));
        config_delete_btn.setForeground(new java.awt.Color(255, 255, 255));
        config_delete_btn.setText("Supprimer");
        config_delete_btn.setEnabled(false);
        config_delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_delete_btnActionPerformed(evt);
            }
        });

        config_id_lbl.setBackground(new java.awt.Color(153, 204, 255));
        config_id_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        config_id_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        config_id_lbl.setRequestFocusEnabled(false);

        fname_lbl9.setBackground(new java.awt.Color(255, 255, 255));
        fname_lbl9.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl9.setText("ID");

        fname_lbl11.setBackground(new java.awt.Color(255, 255, 255));
        fname_lbl11.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl11.setText("Pack Item *");

        pack_master_box.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        pack_items_box.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pack_items_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pack_items_boxActionPerformed(evt);
            }
        });

        coefficient_txt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        coefficient_txt.setText("1");

        fname_lbl12.setBackground(new java.awt.Color(255, 255, 255));
        fname_lbl12.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl12.setText("Quantité pack item");

        config_tab_msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        config_tab_msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        config_master_clear_search_btn.setText("Réinitialiser champs");
        config_master_clear_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_master_clear_search_btnActionPerformed(evt);
            }
        });

        config_master_refresh_btn.setText("Actualiser");
        config_master_refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_master_refresh_btnActionPerformed(evt);
            }
        });

        config_pack_item_search.setName("fname_txtbox"); // NOI18N
        config_pack_item_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                config_pack_item_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                config_pack_item_searchKeyTyped(evt);
            }
        });

        lname_lbl_search2.setBackground(new java.awt.Color(255, 255, 255));
        lname_lbl_search2.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl_search2.setText("Pack Item");

        config_pack_master_search.setName("fname_txtbox"); // NOI18N
        config_pack_master_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                config_pack_master_searchActionPerformed(evt);
            }
        });
        config_pack_master_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                config_pack_master_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                config_pack_master_searchKeyTyped(evt);
            }
        });

        fname_lbl_search2.setBackground(new java.awt.Color(255, 255, 255));
        fname_lbl_search2.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl_search2.setText("Pack Master");

        config_jtable.setModel(new javax.swing.table.DefaultTableModel(
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
        user_table_scroll2.setViewportView(config_jtable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fname_lbl8)
                            .addComponent(fname_lbl9)
                            .addComponent(fname_lbl11, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fname_lbl12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coefficient_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(config_id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pack_master_box, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pack_items_box, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(config_tab_msg_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(config_master_save_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(config_master_cancel_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(355, 355, 355)
                                        .addComponent(config_delete_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(user_table_scroll2, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                            .addComponent(fname_lbl_search2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(config_pack_master_search, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(lname_lbl_search2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(config_pack_item_search, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(61, 61, 61)
                                            .addComponent(config_master_refresh_btn)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(config_master_clear_search_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(176, 176, 176)))
                        .addGap(183, 183, 183))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(config_id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(fname_lbl9)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(fname_lbl8)
                    .addComponent(pack_master_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fname_lbl11)
                    .addComponent(pack_items_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fname_lbl12)
                    .addComponent(coefficient_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(config_master_cancel_btn)
                    .addComponent(config_master_save_btn)
                    .addComponent(config_delete_btn))
                .addGap(18, 18, 18)
                .addComponent(config_tab_msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(config_master_refresh_btn)
                        .addComponent(config_master_clear_search_btn))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(config_pack_master_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fname_lbl_search2)
                        .addComponent(lname_lbl_search2)
                        .addComponent(config_pack_item_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(user_table_scroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        tabs.addTab("Configuration Packaging", jPanel3);

        jPanel1.setBackground(new java.awt.Color(36, 65, 86));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Packaging Items", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setToolTipText("Packaging Items");
        jPanel1.setAutoscrolls(true);
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName(""); // NOI18N
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentShown(evt);
            }
        });

        fname_lbl.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl.setText("Pack item *");

        lname_lbl.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl.setText("Dimension UOM *");

        dim_uom_txtbox.setText("cm");
        dim_uom_txtbox.setName("fname_txtbox"); // NOI18N

        item_save_btn.setText("Enregistrer");
        item_save_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_save_btnActionPerformed(evt);
            }
        });

        item_cancel_btn.setText("Réinitialiser champs");
        item_cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_cancel_btnActionPerformed(evt);
            }
        });

        master_delete_btn.setBackground(new java.awt.Color(255, 51, 51));
        master_delete_btn.setForeground(new java.awt.Color(255, 255, 255));
        master_delete_btn.setText("Supprimer");
        master_delete_btn.setEnabled(false);
        master_delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                master_delete_btnActionPerformed(evt);
            }
        });

        id_lbl.setBackground(new java.awt.Color(153, 204, 255));
        id_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        id_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        id_lbl.setRequestFocusEnabled(false);

        weight_uom_txtbox.setText("kg");
        weight_uom_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl4.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl4.setText("Poids *");

        weight_txtbox.setText("0.00");
        weight_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl2.setForeground(new java.awt.Color(255, 255, 255));
        login_lbl2.setText("Quantité d'alerte *");

        alert_quantity_txtbox.setText("0.00");
        alert_quantity_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl6.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl6.setText("Largeur*");

        width_txtbox.setText("0.00");
        width_txtbox.setName("fname_txtbox"); // NOI18N

        item_duplicate_btn.setText("Dupliquer");
        item_duplicate_btn.setEnabled(false);
        item_duplicate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                item_duplicate_btnActionPerformed(evt);
            }
        });

        items_tab_msg_lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        items_tab_msg_lbl.setForeground(new java.awt.Color(0, 0, 255));

        fname_lbl1.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl1.setText("ID");

        description_txtbox.setColumns(10);
        description_txtbox.setRows(3);
        jScrollPane1.setViewportView(description_txtbox);

        stock_quantity_txtbox.setEditable(false);
        stock_quantity_txtbox.setText("0.00");
        stock_quantity_txtbox.setName("fname_txtbox"); // NOI18N

        login_lbl4.setForeground(new java.awt.Color(255, 255, 255));
        login_lbl4.setText("Quantité Stock *");

        fname_lbl2.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl2.setText("Description");

        lname_lbl1.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl1.setText("Poids UOM *");

        pwd_lbl7.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl7.setText("Longueur *");

        length_txtbox.setText("0.00");
        length_txtbox.setName("fname_txtbox"); // NOI18N

        height_txtbox.setText("0.00");
        height_txtbox.setName("fname_txtbox"); // NOI18N

        pwd_lbl8.setForeground(new java.awt.Color(255, 255, 255));
        pwd_lbl8.setText("Hauteur *");

        internal_pn_txtbox.setName("pack_item_txtbox"); // NOI18N

        fname_lbl3.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl3.setText("Internal Part Number *");

        fname_lbl_search.setForeground(new java.awt.Color(255, 255, 255));
        fname_lbl_search.setText("Pack Item");

        pack_item_search.setName("fname_txtbox"); // NOI18N
        pack_item_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pack_item_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pack_item_searchKeyTyped(evt);
            }
        });

        lname_lbl_search.setForeground(new java.awt.Color(255, 255, 255));
        lname_lbl_search.setText("Internal Part Number");

        internal_pn_txtbox_search.setName("fname_txtbox"); // NOI18N
        internal_pn_txtbox_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                internal_pn_txtbox_searchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                internal_pn_txtbox_searchKeyTyped(evt);
            }
        });

        refresh_btn.setText("Actualiser");
        refresh_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_btnActionPerformed(evt);
            }
        });

        clear_search_btn.setText("Réinitialiser champs");
        clear_search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_search_btnActionPerformed(evt);
            }
        });

        pack_items_jtable.setModel(new javax.swing.table.DefaultTableModel(
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
        pack_items_jtable.setMaximumSize(new java.awt.Dimension(300, 64));
        pack_items_jtable.setMinimumSize(new java.awt.Dimension(300, 64));
        user_table_scroll.setViewportView(pack_items_jtable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fname_lbl)
                    .addComponent(fname_lbl1))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(id_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(876, 876, 876))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pack_item_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fname_lbl3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(internal_pn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fname_lbl2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(master_delete_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(item_save_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(item_duplicate_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(item_cancel_btn)
                                    .addGap(392, 392, 392))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(fname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pack_item_search, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lname_lbl_search)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(internal_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(refresh_btn)
                        .addGap(18, 18, 18)
                        .addComponent(clear_search_btn)
                        .addGap(604, 604, 604))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lname_lbl)
                        .addGap(214, 214, 214)
                        .addComponent(pwd_lbl7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(length_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(pwd_lbl8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(height_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(750, 750, 750))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lname_lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dim_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(weight_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pwd_lbl4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(weight_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pwd_lbl6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(width_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(login_lbl4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stock_quantity_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(login_lbl2)
                        .addGap(0, 0, 0)
                        .addComponent(alert_quantity_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(643, 643, 643))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(user_table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(items_tab_msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 746, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fname_lbl1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(fname_lbl))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fname_lbl3)
                                .addComponent(internal_pn_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pack_item_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwd_lbl6)
                    .addComponent(width_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lname_lbl)
                    .addComponent(dim_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl7)
                    .addComponent(length_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl8)
                    .addComponent(height_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weight_uom_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_lbl4)
                    .addComponent(weight_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lname_lbl1)
                    .addComponent(stock_quantity_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_lbl4)
                    .addComponent(alert_quantity_txtbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(login_lbl2))
                .addGap(18, 18, 18)
                .addComponent(fname_lbl2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(item_cancel_btn)
                        .addComponent(item_save_btn)
                        .addComponent(item_duplicate_btn))
                    .addComponent(master_delete_btn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(items_tab_msg_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refresh_btn)
                        .addComponent(clear_search_btn))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pack_item_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fname_lbl_search)
                        .addComponent(lname_lbl_search)
                        .addComponent(internal_pn_txtbox_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(user_table_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Packaging Items", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1104, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void master_clear_search_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_clear_search_btnActionPerformed
        clearMastersSearchFields();
        refreshMasters();
    }//GEN-LAST:event_master_clear_search_btnActionPerformed

    private void master_refresh_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_refresh_btnActionPerformed
        refreshMasters();
    }//GEN-LAST:event_master_refresh_btnActionPerformed

    private void master_internal_pn_txtbox_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_master_internal_pn_txtbox_searchKeyTyped
        refreshMasters();
    }//GEN-LAST:event_master_internal_pn_txtbox_searchKeyTyped

    private void master_internal_pn_txtbox_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_master_internal_pn_txtbox_searchKeyPressed
        refreshMasters();
    }//GEN-LAST:event_master_internal_pn_txtbox_searchKeyPressed

    private void pack_master_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_pack_master_searchKeyTyped
        refreshMasters();
    }//GEN-LAST:event_pack_master_searchKeyTyped

    private void pack_master_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_pack_master_searchKeyPressed
        refreshMasters();
    }//GEN-LAST:event_pack_master_searchKeyPressed

    private void master_duplicate_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_duplicate_btnActionPerformed
        master_id_lbl.setText("");
        pack_master_txtbox.setText(mastersAux.getPackMaster());
        master_internal_pn_txtbox.setText(mastersAux.getPackInternPN());
        master_dim_uom_txtbox.setText(mastersAux.getDimensionUOM());
        master_width_txtbox.setText(mastersAux.getPackWidth() + "");
        master_height_txtbox.setText(mastersAux.getPackHeight() + "");
        master_width_txtbox.setText(mastersAux.getPackWidth() + "");
        master_weight_uom_txtbox.setText(mastersAux.getWeightUOM());
        master_weight_txtbox.setText(mastersAux.getPackWeight() + "");
        master_description_txtbox.setText(mastersAux.getDescription());
        master_volume_txtbox.setText(mastersAux.getVolume() + "");
        this.mastersAux = null;
        master_delete_btn.setEnabled(false);
        masters_tab_msg_lbl.setText("Formulaire dupliqué !");
    }//GEN-LAST:event_master_duplicate_btnActionPerformed

    private void master_cancel_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_cancel_btnActionPerformed
        clearPackMastersTabFields();
    }//GEN-LAST:event_master_cancel_btnActionPerformed

    private void master_save_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_save_btnActionPerformed
        if (pack_master_txtbox.getText().isEmpty()
                || master_internal_pn_txtbox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Valeur null pour un ou plusieurs champs. \nMerci de remplir touts les champs requis !", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (master_id_lbl.getText().isEmpty()) { // ID Label is empty, then is a new Object
            //
            Helper.startSession();
            Query query = Helper.sess.createQuery(HQLHelper.GET_PACK_MASTER);
            query.setParameter("packMaster", pack_master_txtbox.getText().trim());
            Helper.sess.getTransaction().commit();

            if (!query.list().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Packaging " + pack_master_txtbox.getText().trim() + " existe déjà dans la base !", "Erreur création !", JOptionPane.ERROR_MESSAGE);
            } else {
                PackagingMaster pm = new PackagingMaster();

                pm.setPackMaster(pack_master_txtbox.getText());
                pm.setPackInternPN(master_internal_pn_txtbox.getText());
                pm.setDescription(master_description_txtbox.getText());
                pm.setDimensionUOM(master_dim_uom_txtbox.getText().trim());
                pm.setWeightUOM(master_weight_uom_txtbox.getText().trim());
                pm.setPackHeight(Float.valueOf(master_height_txtbox.getText()));
                pm.setPackWidth(Float.valueOf(master_width_txtbox.getText()));
                pm.setPackLength(Float.valueOf(master_length_txtbox.getText()));
                pm.setPackWeight(Float.valueOf(master_weight_txtbox.getText()));
                float volume = Float.valueOf(master_height_txtbox.getText()) * Float.valueOf(master_width_txtbox.getText()) * Float.valueOf(master_length_txtbox.getText());
                System.out.println("new volume " + volume / 1000000);
                pm.setVolume(volume / 1000000);
                pm.create(pm);

                clearPackMastersTabFields();
                masters_tab_msg_lbl.setText("Nouveaux données enregistrées !");
                refreshMasters();
            }
        } else { // ID Label is filed, then is an update
            mastersAux.setPackMaster(pack_master_txtbox.getText());
            mastersAux.setPackInternPN(master_internal_pn_txtbox.getText());
            mastersAux.setDescription(master_description_txtbox.getText());
            mastersAux.setDimensionUOM(master_dim_uom_txtbox.getText().trim());
            mastersAux.setWeightUOM(master_weight_uom_txtbox.getText().trim());
            mastersAux.setPackHeight(Float.valueOf(master_height_txtbox.getText()));
            mastersAux.setPackWidth(Float.valueOf(master_width_txtbox.getText()));
            mastersAux.setPackLength(Float.valueOf(master_length_txtbox.getText()));
            mastersAux.setPackWeight(Float.valueOf(master_weight_txtbox.getText()));
            float volume = Float.valueOf(master_height_txtbox.getText()) * Float.valueOf(master_width_txtbox.getText()) * Float.valueOf(master_length_txtbox.getText());
            System.out.println("new volume " + volume / 1000000);
            mastersAux.setVolume(volume / 1000000);
            mastersAux.update(mastersAux);
            clearPackMastersTabFields();
            masters_tab_msg_lbl.setText("Données mises à jour !");
            refreshMasters();
        }
    }//GEN-LAST:event_master_save_btnActionPerformed

    private void clear_search_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_clear_search_btnActionPerformed
        clearItemsSearchFields();
        refreshItems();
    }//GEN-LAST:event_clear_search_btnActionPerformed

    private void refresh_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_refresh_btnActionPerformed
        refreshItems();
    }//GEN-LAST:event_refresh_btnActionPerformed

    private void internal_pn_txtbox_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_internal_pn_txtbox_searchKeyTyped
        refreshItems();
    }//GEN-LAST:event_internal_pn_txtbox_searchKeyTyped

    private void internal_pn_txtbox_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_internal_pn_txtbox_searchKeyPressed

    }//GEN-LAST:event_internal_pn_txtbox_searchKeyPressed

    private void pack_item_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_pack_item_searchKeyTyped
        refreshItems();
    }//GEN-LAST:event_pack_item_searchKeyTyped

    private void pack_item_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_pack_item_searchKeyPressed

    }//GEN-LAST:event_pack_item_searchKeyPressed

    private void item_duplicate_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_item_duplicate_btnActionPerformed
        id_lbl.setText("");
        pack_item_txtbox.setText(itemsAux.getPackItem());
        internal_pn_txtbox.setText(itemsAux.getItemInternPN());
        dim_uom_txtbox.setText(itemsAux.getDimensionUOM());
        width_txtbox.setText(itemsAux.getItemWidth() + "");
        height_txtbox.setText(itemsAux.getItemHeight() + "");
        width_txtbox.setText(itemsAux.getItemWidth() + "");
        weight_uom_txtbox.setText(itemsAux.getWeightUOM());
        weight_txtbox.setText(itemsAux.getItemWeight() + "");
        description_txtbox.setText(itemsAux.getDescription());
        stock_quantity_txtbox.setText("0.00");
        alert_quantity_txtbox.setText(itemsAux.getAlertQty() + "");
        master_delete_btn.setEnabled(false);
        this.itemsAux = null;
        items_tab_msg_lbl.setText("Formulaire dupliqué !");
    }//GEN-LAST:event_item_duplicate_btnActionPerformed

    private void master_delete_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_master_delete_btnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null,
                String.format("ATTENTION : \nLa suppression de cet élément entraine la suppression de toutes les configurations associées !\n"
                        + " Confirmez-vous la suppression de cet élément [%s] ?",
                        this.itemsAux.getId()),
                "Suppression Packaging Item",
                JOptionPane.WARNING_MESSAGE);

        if (confirmed == 0) {

            Query query = Helper.sess.createQuery(HQLHelper.DEL_CONFIG_OF_PACK_ITEM);
            query.setParameter("packItem", this.itemsAux.getPackItem());
            int result = query.executeUpdate();
            itemsAux.delete(itemsAux);
            clearPackItemsTabFields();
            items_tab_msg_lbl.setText("Elément supprimé !");
            refreshItems();
        }
    }//GEN-LAST:event_master_delete_btnActionPerformed

    private void item_cancel_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_item_cancel_btnActionPerformed
        clearPackItemsTabFields();
    }//GEN-LAST:event_item_cancel_btnActionPerformed

    private void item_save_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_item_save_btnActionPerformed
        if (pack_item_txtbox.getText().isEmpty()
                || alert_quantity_txtbox.getText().isEmpty()
                || internal_pn_txtbox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Valeur null pour un ou plusieurs champs. \nMerci de remplir touts les champs requis !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (id_lbl.getText().isEmpty()) { // ID Label is empty, then is a new Object
                PackagingItems mu = new PackagingItems();

                mu.setPackItem(pack_item_txtbox.getText());
                mu.setItemInternPN(internal_pn_txtbox.getText());
                mu.setDescription(description_txtbox.getText());
                mu.setDimensionUOM(dim_uom_txtbox.getText().trim());
                mu.setWeightUOM(weight_uom_txtbox.getText().trim());
                mu.setItemHeight(Float.valueOf(height_txtbox.getText()));
                mu.setItemWidth(Float.valueOf(width_txtbox.getText()));
                mu.setItemLength(Float.valueOf(length_txtbox.getText()));
                mu.setItemWeight(Float.valueOf(weight_txtbox.getText()));
                mu.setAlertQty(Float.valueOf(alert_quantity_txtbox.getText().trim()));
                try {
                    mu.create(mu);
                    clearPackItemsTabFields();
                    items_tab_msg_lbl.setText("Nouveaux données enregistrées !");
                    refreshItems();
                } catch (ConstraintViolationException ex) {
                    UILog.causeDialog(this, ex.getSQLException().getNextException());

                    //UILog.severeSQLDialog(this, ex.getSQLException());
                    //UILog.exceptionDialog(this, ex);
                }

            } else { // ID Label is filed, then is an update
                itemsAux.setPackItem(pack_item_txtbox.getText());
                itemsAux.setItemInternPN(internal_pn_txtbox.getText());
                itemsAux.setDescription(description_txtbox.getText());
                itemsAux.setDimensionUOM(dim_uom_txtbox.getText().trim());
                itemsAux.setWeightUOM(weight_uom_txtbox.getText().trim());
                itemsAux.setItemHeight(Float.valueOf(height_txtbox.getText()));
                itemsAux.setItemWidth(Float.valueOf(width_txtbox.getText()));
                itemsAux.setItemLength(Float.valueOf(length_txtbox.getText()));
                itemsAux.setItemWeight(Float.valueOf(weight_txtbox.getText()));
                itemsAux.setAlertQty(Float.valueOf(alert_quantity_txtbox.getText().trim()));
                try {
                    itemsAux.update(itemsAux);
                    clearPackItemsTabFields();
                    items_tab_msg_lbl.setText("Données mise à jour !");
                    refreshItems();
                } catch (org.hibernate.exception.ConstraintViolationException ex) {
                    UILog.causeDialog(this, ex.getSQLException());
                }
            }
        }
    }//GEN-LAST:event_item_save_btnActionPerformed

    private void tabsComponentShown(ComponentEvent evt) {//GEN-FIRST:event_tabsComponentShown

    }//GEN-LAST:event_tabsComponentShown

    private void jPanel1ComponentShown(ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentShown

    }//GEN-LAST:event_jPanel1ComponentShown

    private void tabsPropertyChange(PropertyChangeEvent evt) {//GEN-FIRST:event_tabsPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tabsPropertyChange

    private void tabsStateChanged(ChangeEvent evt) {//GEN-FIRST:event_tabsStateChanged

        switch (tabs.getSelectedIndex()) {
            case 0:
                initItemsTabUI();
                break;
            case 1:
                initMastersTabUI();
                break;
            case 2:
                initConfigTabUI();
                break;

        }
    }//GEN-LAST:event_tabsStateChanged

    private void config_master_save_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_master_save_btnActionPerformed
        if (coefficient_txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Valeur null pour le champ coefficient. \nMerci de remplir touts les champs requis !", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (config_id_lbl.getText().isEmpty()) { // ID empty then is a new object
            try {
                float coeff = Float.parseFloat(coefficient_txt.getText().trim());
                PackagingConfig pc = new PackagingConfig(
                        pack_master_box.getSelectedItem().toString().trim(),
                        pack_items_box.getSelectedItem().toString().trim(),
                        coeff);
                pc.create(pc);
                config_tab_msg_lbl.setText("Nouveaux données enregistrées !");
                clearConfigTabFields();
                refreshConfigs();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Valeur non numérique pour le champ coefficient. \nMerci de saisir une valeur numérique (n.mn) !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else { // ID full the is an update
            try {
                float coeff = Float.parseFloat(coefficient_txt.getText().trim());

                configAux.setPackMaster(pack_master_box.getSelectedItem().toString().trim());
                configAux.setPackItem(pack_items_box.getSelectedItem().toString().trim());
                configAux.setCoefficient(coeff);
                configAux.update(configAux);
                config_tab_msg_lbl.setText("Données mises à jour !");

                clearConfigTabFields();
                refreshConfigs();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Valeur non numérique pour le champ coefficient. \nMerci de saisir une valeur numérique (n.mn) !", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


    }//GEN-LAST:event_config_master_save_btnActionPerformed

    private void config_master_cancel_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_master_cancel_btnActionPerformed
        clearConfigTabFields();
    }//GEN-LAST:event_config_master_cancel_btnActionPerformed

    private void config_delete_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_delete_btnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null,
                String.format("Confirmez-vous la suppression de cette relation [%s] ?",
                        this.configAux.getId()),
                "Suppression de la relation",
                JOptionPane.WARNING_MESSAGE);

        if (confirmed == 0) {
            configAux.delete(configAux);
            clearConfigTabFields();
            config_tab_msg_lbl.setText("Relation supprimée !");
            refreshConfigs();
        }
    }//GEN-LAST:event_config_delete_btnActionPerformed

    private void config_pack_master_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_config_pack_master_searchKeyPressed
        refreshConfigs();
    }//GEN-LAST:event_config_pack_master_searchKeyPressed

    private void config_pack_master_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_config_pack_master_searchKeyTyped
        refreshConfigs();
    }//GEN-LAST:event_config_pack_master_searchKeyTyped

    private void config_pack_item_searchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_config_pack_item_searchKeyPressed
        refreshConfigs();
    }//GEN-LAST:event_config_pack_item_searchKeyPressed

    private void config_pack_item_searchKeyTyped(KeyEvent evt) {//GEN-FIRST:event_config_pack_item_searchKeyTyped
        refreshConfigs();
    }//GEN-LAST:event_config_pack_item_searchKeyTyped

    private void config_master_refresh_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_master_refresh_btnActionPerformed
        refreshConfigs();
    }//GEN-LAST:event_config_master_refresh_btnActionPerformed

    private void config_master_clear_search_btnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_master_clear_search_btnActionPerformed
        clearConfigSearchFields();
        refreshConfigs();
    }//GEN-LAST:event_config_master_clear_search_btnActionPerformed

    private void config_pack_master_searchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_config_pack_master_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_config_pack_master_searchActionPerformed

    private void pack_items_boxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pack_items_boxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pack_items_boxActionPerformed

    private void master_cancel_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_master_cancel_btn1ActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null,
                String.format("ATTENTION : \nLa suppression de cet élément entraine la suppression de toutes les configurations associées !\n"
                        + " Confirmez-vous la suppression de cet élément [%s] ?",
                        this.mastersAux.getId()),
                "Suppression Packaging Master",
                JOptionPane.WARNING_MESSAGE);

        if (confirmed == 0) {

            Query query = Helper.sess.createQuery(HQLHelper.DEL_CONFIG_OF_PACK_MASTER);
            query.setParameter("packMaster", this.mastersAux.getPackMaster());
            int result = query.executeUpdate();
            mastersAux.delete(mastersAux);
            clearPackMastersTabFields();
            masters_tab_msg_lbl.setText("Elément supprimé !");
            refreshMasters();
        }
    }//GEN-LAST:event_master_cancel_btn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JTextField alert_quantity_txtbox;
    javax.swing.JButton clear_search_btn;
    javax.swing.JTextField coefficient_txt;
    javax.swing.JButton config_delete_btn;
    javax.swing.JLabel config_id_lbl;
    javax.swing.JTable config_jtable;
    javax.swing.JButton config_master_cancel_btn;
    javax.swing.JButton config_master_clear_search_btn;
    javax.swing.JButton config_master_refresh_btn;
    javax.swing.JButton config_master_save_btn;
    javax.swing.JTextField config_pack_item_search;
    javax.swing.JTextField config_pack_master_search;
    javax.swing.JLabel config_tab_msg_lbl;
    javax.swing.JTextArea description_txtbox;
    javax.swing.JTextField dim_uom_txtbox;
    javax.swing.JLabel fname_lbl;
    javax.swing.JLabel fname_lbl1;
    javax.swing.JLabel fname_lbl11;
    javax.swing.JLabel fname_lbl12;
    javax.swing.JLabel fname_lbl2;
    javax.swing.JLabel fname_lbl3;
    javax.swing.JLabel fname_lbl4;
    javax.swing.JLabel fname_lbl5;
    javax.swing.JLabel fname_lbl6;
    javax.swing.JLabel fname_lbl7;
    javax.swing.JLabel fname_lbl8;
    javax.swing.JLabel fname_lbl9;
    javax.swing.JLabel fname_lbl_search;
    javax.swing.JLabel fname_lbl_search1;
    javax.swing.JLabel fname_lbl_search2;
    javax.swing.JTextField height_txtbox;
    javax.swing.JLabel id_lbl;
    javax.swing.JTextField internal_pn_txtbox;
    javax.swing.JTextField internal_pn_txtbox_search;
    javax.swing.JButton item_cancel_btn;
    javax.swing.JButton item_duplicate_btn;
    javax.swing.JButton item_save_btn;
    javax.swing.JLabel items_tab_msg_lbl;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel2;
    javax.swing.JPanel jPanel3;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JScrollPane jScrollPane2;
    javax.swing.JTextField length_txtbox;
    javax.swing.JLabel lname_lbl;
    javax.swing.JLabel lname_lbl1;
    javax.swing.JLabel lname_lbl2;
    javax.swing.JLabel lname_lbl3;
    javax.swing.JLabel lname_lbl_search;
    javax.swing.JLabel lname_lbl_search1;
    javax.swing.JLabel lname_lbl_search2;
    javax.swing.JLabel login_lbl2;
    javax.swing.JLabel login_lbl4;
    javax.swing.JButton master_cancel_btn;
    javax.swing.JButton master_cancel_btn1;
    javax.swing.JButton master_clear_search_btn;
    javax.swing.JButton master_delete_btn;
    javax.swing.JTextArea master_description_txtbox;
    javax.swing.JTextField master_dim_uom_txtbox;
    javax.swing.JButton master_duplicate_btn;
    javax.swing.JTextField master_height_txtbox;
    javax.swing.JLabel master_id_lbl;
    javax.swing.JTextField master_internal_pn_txtbox;
    javax.swing.JTextField master_internal_pn_txtbox_search;
    javax.swing.JTextField master_length_txtbox;
    javax.swing.JButton master_refresh_btn;
    javax.swing.JButton master_save_btn;
    javax.swing.JTextField master_volume_txtbox;
    javax.swing.JTextField master_weight_txtbox;
    javax.swing.JTextField master_weight_uom_txtbox;
    javax.swing.JTextField master_width_txtbox;
    javax.swing.JLabel masters_tab_msg_lbl;
    javax.swing.JTextField pack_item_search;
    javax.swing.JTextField pack_item_txtbox;
    javax.swing.JComboBox pack_items_box;
    javax.swing.JTable pack_items_jtable;
    javax.swing.JComboBox pack_master_box;
    javax.swing.JTextField pack_master_search;
    javax.swing.JTextField pack_master_txtbox;
    javax.swing.JTable pack_masters_jtable;
    javax.swing.JLabel pwd_lbl10;
    javax.swing.JLabel pwd_lbl11;
    javax.swing.JLabel pwd_lbl12;
    javax.swing.JLabel pwd_lbl4;
    javax.swing.JLabel pwd_lbl5;
    javax.swing.JLabel pwd_lbl6;
    javax.swing.JLabel pwd_lbl7;
    javax.swing.JLabel pwd_lbl8;
    javax.swing.JLabel pwd_lbl9;
    javax.swing.JButton refresh_btn;
    javax.swing.JTextField stock_quantity_txtbox;
    javax.swing.JTabbedPane tabs;
    javax.swing.JScrollPane user_table_scroll;
    javax.swing.JScrollPane user_table_scroll1;
    javax.swing.JScrollPane user_table_scroll2;
    javax.swing.JTextField weight_txtbox;
    javax.swing.JTextField weight_uom_txtbox;
    javax.swing.JTextField width_txtbox;
    // End of variables declaration//GEN-END:variables

}
