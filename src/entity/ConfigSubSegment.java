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
@Table(name = "config_sub_segment")
public class ConfigSubSegment extends DAO implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_sub_segment_id_seq")
    @SequenceGenerator(name = "config_sub_segment_id_seq", sequenceName = "config_sub_segment_id_seq", allocationSize = 1)
    private Integer id;
    
    @Column(name="segment")
    private String subSegment;

    public ConfigSubSegment() {
    }

    public ConfigSubSegment(String subSegment) {
        this.subSegment = subSegment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubSegment() {
        return subSegment;
    }

    public void setSubSegment(String subSegment) {
        this.subSegment = subSegment;
    }

    //######################################################################
    public List select() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_ALL_SUB_SEGMENTS);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }

}
