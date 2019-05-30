/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import helper.HQLHelper;
import helper.Helper;
import hibernate.DAO;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.Query;

/**
 *
 * @author user
 */
@Entity
@Table(name = "load_plan_destination")
public class LoadPlanDestination extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "load_plan_destination_id_seq")
    @SequenceGenerator(name = "load_plan_destination_id_seq", sequenceName = "load_plan_destination_id_seq", allocationSize = 1)
    private Integer id;
       
    @Column(name = "destination")
    private String destination;    
    
    @Column(name = "project")
    private String project;
    
    public LoadPlanDestination() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }       

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }   

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    
    public List selectDestinationByProject(String project) {
        Helper.startSession();
        
        Query query = Helper.sess.createQuery(HQLHelper.GET_LOAD_PLAN_DEST_PROJECT);
        query.setParameter("project", project);
        
        Helper.sess.getTransaction().commit();
        return query.list();
    }      
    
    
    
}
