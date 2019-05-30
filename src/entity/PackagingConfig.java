package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import helper.Helper;
import java.util.List;
import org.hibernate.Query;
import helper.HQLHelper;
import hibernate.DAO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ui.UILog;

/**
 * 
 */
@Entity
@Table(name = "packaging_config")
public class PackagingConfig extends DAO implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packaging_config_id_seq")
    @SequenceGenerator(name = "packaging_config_id_seq", sequenceName = "packaging_config_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name="pack_master")
    private String packMaster;
    
    @Column(name="pack_item")
    private String packItem;
        
    @Column(name="coefficient")
    private float coefficient;
    
    public PackagingConfig() {
    }

    public PackagingConfig(String packMaster, String packItem, float coefficient) {
        this.packMaster = packMaster;
        this.packItem = packItem;
        this.coefficient = coefficient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackMaster() {
        return packMaster;
    }

    public void setPackMaster(String packMaster) {
        this.packMaster = packMaster;
    }

    public String getPackItem() {
        return packItem;
    }

    public void setPackItem(String packItem) {
        this.packItem = packItem;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }
    
    
    //######################################################################
    public List selectAllConfigs() {
        Helper.startSession();
        
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_CONFIGS);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }
    
    public PackagingConfig selectConfigById(int id) {
        Helper.startSession();        
        Query query = Helper.sess.createQuery(HQLHelper.GET_CONFIG_BY_ID);
        query.setParameter("id", id);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        if(!query.list().isEmpty())
            return (PackagingConfig) query.list().get(0);       
        return null;
    }
    
    

}