/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package __main__;

import entity.ConfigBarcode;
import entity.ConfigProject;
import entity.ConfigWarehouse;
import helper.CloseTabButtonComponent;
import helper.ComboItem;
import helper.HQLHelper;
import helper.Helper;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.hibernate.Query;
import ui.UILog;
import ui.config.ConfigMsg;
import ui.error.ErrorMsg;

/**
 *
 * @author Oussama EZZIOURI
 */
public class GlobalMethods {

    /**
     *
     * @return
     */
    public static String convertToStandardDate(Date d) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(d);
    }

    /**
     *
     * @return
     */
    public static String convertDateToStringFormat(Date d, String format) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)

        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    /**
     *
     * @return
     */
    public static String getStrTimeStamp() {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format "yyyy/MM/dd" or "yy.MM.dd"
     * @return
     */
    public static String getStrDateStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (year/month/day)
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format : Patter of datetime example : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getStrTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format
     * @return
     */
    public static Date getTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(reportDate);
        } catch (ParseException ex) {
            Logger.getLogger(GlobalMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInHours(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInHr = TimeUnit.MILLISECONDS.toHours(duration);
        return diffInHr;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInMinutes(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diffInMin;
    }

    /**
     * Add new tab to the parent JTabbedPane
     *
     * @param title The title of the new tab
     * @param parent The parent JTabbedPane
     * @param newTab The new tab object
     * @param evt MouseEvent that trigger this method
     */
    public static void addNewTabToParent(String title, JTabbedPane parent, JPanel newTab, MouseEvent evt) {
        parent.addTab(title, null, newTab,
                title);

        parent.setMnemonicAt(0, KeyEvent.VK_1);

        parent.setTabComponentAt(parent.getTabCount() - 1,
                new CloseTabButtonComponent(parent));
        parent.setSelectedIndex(parent.getTabCount() - 1);
    }

    /**
     * Add new tab to the parent JTabbedPane
     *
     * @param title The title of the new tab
     * @param parent The parent JTabbedPane
     * @param newTab The new tab object
     * @param evt ActionEvent that trigger this method
     */
    public static void addNewTabToParent(String title, JTabbedPane parent, JPanel newTab, AWTEvent evt) {
        parent.addTab(title, null, newTab,
                title);

        parent.setMnemonicAt(0, KeyEvent.VK_1);

        parent.setTabComponentAt(parent.getTabCount() - 1,
                new CloseTabButtonComponent(parent));
        parent.setSelectedIndex(parent.getTabCount() - 1);
    }

    /**
     *
     * @param harnessType : Harness Type to filter on (Volvo, Ducati)
     */
    public static void loadDotMatrixCodePatterns(String harnessType) {
        //System.out.println("Loading DATAMATRIX_PATTERN_LIST pattern list ... ");
        UILog.info(ConfigMsg.APP_CONFIG0002[0]);
        Helper.sess.beginTransaction();
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_KEYWORD_AND_HARNESSTYPE);
        query.setParameter("keyWord", "DOTMATRIX");
        query.setParameter("harnessType", harnessType);
        //PLASTIC_BAG_BARCODE

        UILog.info(query.getQueryString());
        Helper.sess.beginTransaction();
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        GlobalVars.DATAMATRIX_PATTERN_LIST = new String[query.list().size()];

        int i = 0;
        String patterns = "";
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            ConfigBarcode object = (ConfigBarcode) it.next();
            GlobalVars.DATAMATRIX_PATTERN_LIST[i] = object.getBarcodePattern();
            patterns += GlobalVars.DATAMATRIX_PATTERN_LIST[i] + "\n";

            i++;
        }
        UILog.info(patterns);

        UILog.info(ConfigMsg.APP_CONFIG0003[0], GlobalVars.DATAMATRIX_PATTERN_LIST.length + "", harnessType);
    }

    /**
     *
     * @param harnessType : Harness Type to filter on (Volvo, Ducati)
     */
    public static void loadPartNumberCodePatterns(String harnessType) {
        System.out.println("Loading PARTNUMBER_PATTERN_LIST pattern list ... ");
        Query query = Helper.sess.createQuery(HQLHelper.GET_PATTERN_BY_KEYWORD_AND_HARNESSTYPE);
        query.setParameter("keyWord", "PARTNUMBER");
        query.setParameter("harnessType", harnessType);
        //PLASTIC_BAG_BARCODE
        UILog.info(query.getQueryString());
        Helper.sess.beginTransaction();
        Helper.sess.getTransaction().commit();
        List resultList = query.list();
        GlobalVars.PARTNUMBER_PATTERN_LIST = new String[query.list().size()];

        int i = 0;
        String patterns = "";
        for (Iterator it = resultList.iterator(); it.hasNext();) {
            ConfigBarcode object = (ConfigBarcode) it.next();
            GlobalVars.PARTNUMBER_PATTERN_LIST[i] = object.getBarcodePattern();
            patterns += GlobalVars.PARTNUMBER_PATTERN_LIST[i] + "\n";

            i++;
        }

        UILog.info(patterns);

        UILog.info(ConfigMsg.APP_CONFIG0004[0], "" + GlobalVars.PARTNUMBER_PATTERN_LIST.length, harnessType);
    }

    /**
     *
     * @param parentUI
     * @param box
     * @param displayAll display the "ALL" value in the first item
     */
    public static void loadProjectsCombobox(Object parentUI, JComboBox box, boolean displayAll) {
        List result = new ConfigProject().select();
        if (result.isEmpty()) {
            UILog.severeDialog((Component) parentUI, ErrorMsg.APP_ERR0035);
            UILog.severe(ErrorMsg.APP_ERR0035[1]);
        } else { //Map project data in the list
            box.removeAllItems();
            if(displayAll)  box.addItem(new ComboItem("ALL", "ALL"));
            for (Object o : result) {
                ConfigProject p = (ConfigProject) o;
                box.addItem(new ComboItem(p.getProject(), p.getProject()));
            }
        }
    }

    /**
     *
     * @param parentUI
     * @param project
     * @param box
     */
    public static void setWarehouseComboboxByProject(Object parentUI, String project, JComboBox box) {
        if (!project.toUpperCase().equals("ALL")) {
            List result = new ConfigWarehouse().selectByProjectAndType(project, "FINISH_GOODS");
            if (result.isEmpty()) {
                UILog.severeDialog(null, ErrorMsg.APP_ERR0036);
                UILog.severe(ErrorMsg.APP_ERR0036[1]);
            } else { //Map project data in the list
                box.removeAllItems();
                for (Object o : result) {
                    ConfigWarehouse cp = (ConfigWarehouse) o;
                    box.addItem(new ComboItem(cp.getWarehouse(), cp.getWarehouse()));
                }

            }
        }
    }

}
