package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import __main__.GlobalVars;
import helper.HQLHelper;
import helper.Helper;
import hibernate.DAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * 
 */
@Entity
@Table(name = "packaging_stock_movement")
public class PackagingStockMovement extends DAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packaging_movement_id_seq")
    @SequenceGenerator(name = "packaging_movement_id_seq", sequenceName = "packaging_movement_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "pack_item")
    private String packItem;

    /**
     * Short description of pack item
     */
    @Column(name = "pack_master")
    private String packMaster;

    @Column(name = "quantity")
    private float qty;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "create_user")
    private String createUser;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "fifo_time")
    private Date fifoTime;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "comment", nullable = true)
    private String comment;

    public PackagingStockMovement() {
    }

    public PackagingStockMovement(
            String packItem,
            String packMaster,
            String documentId,
            String createUser,
            Date fifoTime,
            String toWarehouse,
            float qty,
            String comment) {
        this.packItem = packItem;
        this.packMaster = packMaster;
        this.documentId = documentId;
        this.createUser = createUser;
        this.fifoTime = fifoTime;
        this.warehouse = toWarehouse;
        this.qty = qty;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackItem() {
        return packItem;
    }

    public void setPackItem(String packItem) {
        this.packItem = packItem;
    }

    public String getPackMaster() {
        return packMaster;
    }

    public void setPackMaster(String packMaster) {
        this.packMaster = packMaster;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getFifoTime() {
        return fifoTime;
    }

    public void setFifoTime(Date fifoTime) {
        this.fifoTime = fifoTime;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Create a stock entry with the qty of items of master pack
     *
     * @param user The initiator of the transaction
     * @param packMaster : a master pack to book
     * @param qty : How many master pack to book
     * @param bookingType : IN for items reception, OUT for items out
     * @param fromWh
     * @param toWh
     * @param comment
     * @param documentId
     * @return
     */
    public int bookMasterPack(String user, String packMaster,
            float qty, String bookingType, String fromWh,
            String toWh, String comment, String documentId) {
        int returnCode = 0;

        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONFIG_ITEMS_BY_PACKMASTER);
        query.setParameter("packMaster", packMaster);

        Helper.sess.getTransaction().commit();
        List result = query.list();
        System.out.println("Objects found " + result.size());
        if (!result.isEmpty()) {
            for (Object obj : result) {
                PackagingConfig configItem = (PackagingConfig) obj;
                System.out.println("book of " + configItem.getPackItem());
                //Deducte the negative quantity of packaging
                //from the source warehouse
                PackagingStockMovement psm = new PackagingStockMovement(
                        configItem.getPackItem(),
                        configItem.getPackMaster(),
                        documentId,
                        user,
                        new Date(),
                        fromWh,
                        -(qty * configItem.getCoefficient()),
                        comment
                );
                psm.create(psm);

                //Add the positive quantity of packaging
                //into the destination warehouse
                psm = new PackagingStockMovement(
                        configItem.getPackItem(),
                        configItem.getPackMaster(),
                        documentId,
                        user,
                        new Date(),
                        toWh,
                        qty * configItem.getCoefficient(),
                        comment
                );
                psm.create(psm);

                Helper.startSession();

                //Si seuil alert atteint envoi du mail d'alerte
                String check_stock_item = "SELECT psv.pack_item, SUM(psv.quantity) AS onstock, pi.alert_qty  "
                        + " FROM packaging_stock_movement psv, packaging_items pi"
                        + " WHERE psv.pack_item = '" + configItem.getPackItem() + "'"
                        + " AND psv.pack_item = pi.pack_item "
                        + " AND psv.warehouse = '" + fromWh + "' "
                        + " GROUP BY psv.pack_item, pi.alert_qty";

                SQLQuery sqlQuery = Helper.sess.createSQLQuery(check_stock_item);
                if (sqlQuery.list().size() != 0) {
                    Object[] stockMvm = (Object[]) sqlQuery.list().get(0);
                    System.out.println("Quantité en stock " + Float.parseFloat(stockMvm[1].toString()));
                    System.out.println("Seuil d'alerte " + Float.parseFloat(stockMvm[2].toString()));
                    //If Available quantity is <) Alerte level
                    if (Float.parseFloat(stockMvm[1].toString()) <= Float.parseFloat(stockMvm[2].toString())
                            && "1".equals(GlobalVars.APP_PROP.getProperty("PACKAGING_ALERT_NOTIFICATOR").toString())) {

                        //String[] mailAddressTo = GlobalVars.APP_PROP.getProperty("PACKAGING_ALERT_EMAILS").toString().split("#");
                        //InternetAddress[] mailAddress_TO = new InternetAddress[mailAddressTo.length];
                        System.out.println("Seuil d'alerte atteint pour l'élément " + stockMvm[0].toString() + " ! ");
                        System.out.println("Stock " + stockMvm[0].toString() + " = " + stockMvm[1].toString());
                    }
                }

            }

        } else {
            JOptionPane.showMessageDialog(null, "Pack Master " + packMaster + " introuvable dans la table PackagingConfig");
            return -1;
        }

        return returnCode;
    }

}
