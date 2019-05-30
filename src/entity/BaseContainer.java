package entity;
// Generated 6 f�vr. 2016 21:43:55 by Hibernate Tools 3.6.0

import __main__.GlobalMethods;
import __main__.GlobalVars;
import gui.packaging.PackagingVars;
import helper.Helper;
import helper.HQLHelper;
import hibernate.DAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.Query;
import ui.UILog;
import ui.error.ErrorMsg;

/**
 * BaseContainer generated by hbm2java
 */
@Entity
@Table(name = "base_container")
public class BaseContainer extends DAO implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_container_id_seq")
    @SequenceGenerator(name = "base_container_id_seq", sequenceName = "base_container_id_seq", allocationSize = 1)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "write_time")
    private Date fifoTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "stored_time")
    private Date storedTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "shipped_time")
    private Date shippedTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "reserved_time")
    private Date reservedTime;

    @Column(name = "create_id")
    private Integer createId;

    @Column(name = "write_id")
    private Integer writeId;

    @Column(name = "m_user")
    private String user;

    @Column(name = "create_user")
    private String createUser;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "closed_time")
    private Date closedTime;

    @Column(name = "work_time")
    private Float workTime;

    @Column(name = "pallet_number")
    private String palletNumber;

    @Column(name = "harness_part")
    private String harnessPart;

    @Column(name = "harness_index")
    private String harnessIndex;

    @Column(name = "supplier_part_number")
    private String supplierPartNumber;

    @Column(name = "qty_expected")
    private Integer qtyExpected;

    @Column(name = "qty_read")
    private Integer qtyRead;

    @Column(name = "container_state")
    private String containerState;

    @Column(name = "container_state_code")
    private String containerStateCode;

    @Column(name = "pack_type")
    private String packType;

    @Column(name = "harness_type")
    private String harnessType;

    @Column(name = "std_time", nullable = false, columnDefinition = "float default 0.00")
    private Double stdTime;

    @Column(name = "price", nullable = false, columnDefinition = "float default 0.00")
    private Double price;

    @Column(name = "pack_workstation")
    private String packWorkstation;

    /**
     * Order number of the packaging, got from FORS with mask KUOT
     *
     */
    @Column(name = "order_no", nullable = true)
    private String order_no;

    /**
     * Packaging for special orders, used to prInteger a specific packaging
     * label 1 = Specific order 0 = Standard order
     */
    @Column(name = "special_order", nullable = true)
    private Integer special_order;

    /**
     * Print an A5 label for each scanned piece. This label is different from
     * open and closing sheet labels, If set to true, it will be printed once
     * the user scan the QR code of a harness.';
     */
    @Column(name = "label_per_piece", nullable = true)
    private Boolean labelPerPiece;

    /**
     * Comment in case of specific orders
     */
    @Column(name = "comment", nullable = true)
    private String comment;

    /**
     * Is the global area that englob many carrousels or fixed boards
     */
    @Column(name = "segment")
    private String segment;

    /**
     * Is the area where the harness is been produced, it can be a group of
     * fixed board or a carrousel
     */
    @Column(name = "workplace")
    private String workplace;

    /**
     * Specify the lifes of the related UCS config, usefull for special order
     * with packaging qty out of standard if -1 = The ucs config can live
     * forever if great than 0, it will decreased each time a pallet is closed
     * until it reach 1 then it must be deleted.
     */
    @Column(name = "ucs_lifes")
    private Integer ucsLifes;

    /**
     * Id of config_ucs line
     */
    @Column(name = "ucs_id")
    private Integer ucsId;

    /**
     * Dispatch label serial number
     */
    @Column(name = "dispatch_label_no", nullable = true)
    private String dispatchLabelNo;

    /**
     * The final destination
     */
    @Column(name = "destination", nullable = true)
    private String destination;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "dispatch_time")
    private Date dispatchTime;

    /**
     * Consignement number
     */
    @Column(name = "consign_no", nullable = true)
    private String consignNo;

    /**
     * Invoice number
     */
    @Column(name = "invoice_no", nullable = true)
    private String invoiceNo;

    @Column(name = "net_weight")
    private double netWeight;

    @Column(name = "gross_weight")
    private double grossWeight;

    @Column(name = "volume")
    private double volume;

    @Column(name = "eng_change")
    private String engChange;

    @Column(name = "article_desc")
    private String articleDesc;

    @Column(name = "project")
    private String project;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "eng_change_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date engChangeDate;

    @Column(name = "priority")
    private int priority;

    @Column(name = "openning_sheet_copies")
    private int openningSheetCopies;

    @Column(name = "closing_sheet_copies")
    private int closingSheetCopies;
    
    @Column(name = "closing_sheet_format")
    private int closingSheetFormat;

    public int getClosingSheetFormat() {
        return closingSheetFormat;
    }

    public void setClosingSheetFormat(int closingSheetFormat) {
        this.closingSheetFormat = closingSheetFormat;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOpenningSheetCopies() {
        return openningSheetCopies;
    }

    public void setOpenningSheetCopies(int openningSheetCopies) {
        this.openningSheetCopies = openningSheetCopies;
    }

    public int getClosingSheetCopies() {
        return closingSheetCopies;
    }

    public void setClosingSheetCopies(int closingSheetCopies) {
        this.closingSheetCopies = closingSheetCopies;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "container", cascade = CascadeType.ALL)
    private Set<BaseHarness> harnessList = new HashSet<BaseHarness>(0);

    ////########################################################################
    ////########################### Transient columns ##########################
    ////########################################################################
    @Transient
    private transient String _harnessCounter;

    @Transient
    private transient String _choosenPackType;

    public BaseContainer() {
    }

    public BaseContainer setDefautlVals() {
        /*
         Set default values of this object 
         from the global mode2_context values
         */
        this.startTime = this.createTime = this.fifoTime = GlobalMethods.getTimeStamp(null);
        this.createId = PackagingVars.context.getUser().getId();
        this.writeId = PackagingVars.context.getUser().getId();
        this.user = PackagingVars.context.getUser().getLogin();
        this.createUser = PackagingVars.context.getUser().getFirstName() + " " + PackagingVars.context.getUser().getLastName();
        return this;
    }

    public BaseContainer(String palletNumber, String harnessPart, String harnessIndex,
            String supplierPartNumber, Integer qtyExpected, Integer qtyRead,
            String state, String state_code, String packType, String harnessType,
            Double stdTime, Double price, Integer special_order, String segment,
            String workplace, Integer ucsLifes, String comment, String orderNo,
            String packWorkstation, Integer ucsId, boolean labelPerPiece) {

        setDefautlVals();
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.containerState = state;
        this.containerStateCode = state_code;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.packType = packType;
        this.harnessType = harnessType;
        this.stdTime = stdTime;
        this.price = price;
        this.special_order = special_order;
        this.segment = segment;
        this.workplace = workplace;
        this.ucsLifes = ucsLifes;
        this.comment = comment;
        this.order_no = orderNo;
        this.packWorkstation = packWorkstation;
        this.ucsId = ucsId;
        this.labelPerPiece = labelPerPiece;

    }

    public BaseContainer(Date createTime, Date writeTime, Integer createId,
            Integer writeId, String user, Date startTime, String palletNumber,
            String harnessPart, String harnessIndex, String supplierPartNumber,
            Integer qtyExpected, Integer qtyRead, String packType, String harnessType,
            Double stdTime, Double price, boolean labelPerPiece) {
        this.startTime = this.createTime = GlobalMethods.getTimeStamp(null);
        this.fifoTime = GlobalMethods.getTimeStamp(null);
        this.createId = PackagingVars.context.getUser().getId();
        this.writeId = PackagingVars.context.getUser().getId();
        this.user = PackagingVars.context.getUser().getFirstName() + " " + PackagingVars.context.getUser().getLastName() + " / " + PackagingVars.context.getUser().getLogin();
        this.createUser = PackagingVars.context.getUser().getFirstName() + " " + PackagingVars.context.getUser().getLastName();
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.packType = packType;
        this.harnessType = harnessType;
        this.stdTime = stdTime;
        this.price = price;
        this.labelPerPiece = labelPerPiece;
    }

    public BaseContainer(Date createTime, Date writeTime, Integer createId,
            Integer writeId, String user, String createUser, Date startTime,
            Date closedTime, Float workTime, String palletNumber,
            String harnessPart, String harnessIndex, String supplierPartNumber,
            Integer qtyExpected, Integer qtyRead, String state, String state_code,
            String packType, String harnessType, Double stdTime, Double price,
            boolean labelPerPiece) {
        this.createTime = createTime;
        this.fifoTime = writeTime;
        this.createId = createId;
        this.writeId = writeId;
        this.user = user;
        this.createUser = createUser;
        this.startTime = startTime;
        this.closedTime = closedTime;
        this.workTime = workTime;
        this.palletNumber = palletNumber;
        this.harnessPart = harnessPart;
        this.harnessIndex = harnessIndex;
        this.supplierPartNumber = supplierPartNumber;
        this.qtyExpected = qtyExpected;
        this.qtyRead = qtyRead;
        this.containerState = state;
        this.containerStateCode = state_code;
        this.packType = packType;
        this.harnessType = harnessType;
        this.stdTime = stdTime;
        this.price = price;
        this.labelPerPiece = labelPerPiece;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Double getPrice() {
        if (price == null) {
            return 0.00;
        } else {
            return price;
        }
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @SuppressWarnings("UnusedAssignment")
    public String getCreateTimeString(String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(this.createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFifoTime() {
        return this.fifoTime;
    }

    public void setFifoTime(Date fifoTime) {
        this.fifoTime = fifoTime;
    }

    public Integer getCreateId() {
        return this.createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getWriteId() {
        return this.writeId;
    }

    public void setWriteId(Integer writeId) {
        this.writeId = writeId;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getClosedTime() {
        return this.closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }

    public Date getStoredTime() {
        return storedTime;
    }

    public void setStoredTime(Date storedTime) {
        this.storedTime = storedTime;
    }

    public Date getShippedTime() {
        return shippedTime;
    }

    public void setShippedTime(Date shippedTime) {
        this.shippedTime = shippedTime;
    }

    public Date getReservedTime() {
        return reservedTime;
    }

    public void setReservedTime(Date reservedTime) {
        this.reservedTime = reservedTime;
    }

    public Float getWorkTime() {
        return this.workTime;
    }

    public void setWorkTime(Float workTime) {
        this.workTime = workTime;
    }

    public String getPalletNumber() {
        return this.palletNumber;
    }

    public void setPalletNumber(String palletNumber) {
        this.palletNumber = palletNumber;
    }

    public String getHarnessPart() {
        return this.harnessPart;
    }

    public void setHarnessPart(String harnessPart) {
        this.harnessPart = harnessPart;
    }

    public String getHarnessIndex() {
        return this.harnessIndex;
    }

    public void setHarnessIndex(String harnessIndex) {
        this.harnessIndex = harnessIndex;
    }

    public String getSupplierPartNumber() {
        return this.supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public Integer getQtyExpected() {
        return this.qtyExpected;
    }

    public Double getQtyExpectedDouble() {
        return Double.valueOf(this.qtyExpected);
    }

    public void setQtyExpected(Integer qtyExpected) {
        this.qtyExpected = qtyExpected;
    }

    public Integer getQtyRead() {
        return this.qtyRead;
    }

    public Double getQtyReadDouble() {
        return Double.valueOf(this.qtyRead);
    }

    public void setQtyRead(Integer qtyRead) {
        this.qtyRead = qtyRead;
    }

    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
    }

    public String getContainerStateCode() {
        return containerStateCode;
    }

    public void setContainerStateCode(String containerStateCode) {
        this.containerStateCode = containerStateCode;
    }

    public String getPackType() {
        return this.packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getHarnessType() {
        return harnessType;
    }

    public void setHarnessType(String harnessType) {
        this.harnessType = harnessType;
    }

    public Set<BaseHarness> getHarnessList() {
        return harnessList;
    }

    public void setHarnessList(Set<BaseHarness> harnessList) {
        this.harnessList = harnessList;
    }

    public double getStdTime() {
        if (stdTime == null) {
            return 0.0;
        }
        return stdTime;
    }

    public void setStdTime(Double stdTime) {
        this.stdTime = stdTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPackWorkstation() {
        return packWorkstation;
    }

    public void setPackWorkstation(String packWorkstation) {
        this.packWorkstation = packWorkstation;
    }

    public String getHarnessCounter() {
        return _harnessCounter;
    }

    public void setHarnessCounter(String _harnessCounter) {
        this._harnessCounter = _harnessCounter;
    }

    public String getChoosenPackType() {
        return _choosenPackType;
    }

    public void setChoosenPackType(String _choosenPackType) {
        this._choosenPackType = _choosenPackType;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getSpecial_order() {
        return special_order;
    }

    public void setSpecial_order(Integer special_order) {
        this.special_order = special_order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUcsLifes() {
        return ucsLifes;
    }

    public void setUcsLifes(Integer ucsLifes) {
        this.ucsLifes = ucsLifes;
    }

    public Integer getUcsId() {
        return ucsId;
    }

    public void setUcsId(Integer ucsId) {
        this.ucsId = ucsId;
    }

    public String getDispatchLabelNo() {
        return dispatchLabelNo;
    }

    public void setDispatchLabelNo(String dispatchLabelNo) {
        this.dispatchLabelNo = dispatchLabelNo;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getConsignNo() {
        return consignNo;
    }

    public void setConsignNo(String consignNo) {
        this.consignNo = consignNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getEngChange() {
        return engChange;
    }

    public void setEngChange(String engChange) {
        this.engChange = engChange;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getFGwarehouse() {
        return warehouse;
    }

    public void setFGwarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Date getEngChangeDate() {
        return engChangeDate;
    }

    public String getEngChangeDate(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (year/month/day)
        DateFormat df = new SimpleDateFormat(format);

        return df.format(engChangeDate);
    }

    public void setEngChangeDate(Date engChangeDate) {
        this.engChangeDate = engChangeDate;
    }

    public Boolean isLabelPerPiece() {
        return labelPerPiece;
    }

    public void setLabelPerPiece(Boolean labelPerPiece) {
        this.labelPerPiece = labelPerPiece;
    }

    public void createHistoryLine(BaseContainer c, ManufactureUsers u, String feedback) {
        HisBaseContainer hbc = new HisBaseContainer().parseContainerData(c, feedback);
        hbc.create(hbc);
        super.update(c);
    }

    @Override
    public void update(Object obj) {
        String feedback = "-";
        BaseContainer bc = (BaseContainer) obj;
        bc.setWriteId(GlobalVars.connectedUser.getId());
        if (!bc.getContainerState().equals(GlobalVars.PALLET_OPEN)) {
            //##############################################################    
            if (bc.getContainerState().equals(GlobalVars.PALLET_WAITING)) {
                feedback = "Pallet waiting to be closed.";
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_CLOSED)) {
                feedback = "Pallet successfully closed.";
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_QUARANTAINE)) {
                feedback = "Set quarantaine with comment : " + PackagingVars.mode2_context.getFeedback();
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_STORED)) {
                feedback = "Pallet successfully stored.";
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_RESERVED)) {
                feedback = "Pallet successfully reserved.";
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_DISPATCHED)) {
                feedback = "Pallet successfully dispatched.";
                super.update(obj);
            } else if (bc.getContainerState().equals(GlobalVars.PALLET_DROPPED)) {
                feedback = "Pallet dropped with comment : " + PackagingVars.mode2_context.getFeedback();
                super.update(obj);
            }
            //##############################################################    
            //Save Container History Line
            HisBaseContainer hbc = new HisBaseContainer().parseContainerData(bc, feedback);
            hbc.create(hbc);
            super.update(obj);

        }
    }

    @Override
    public int create(Object obj) {
        //##############################################################
        //Save Container History Line
        HisBaseContainer hbc = new HisBaseContainer().parseContainerData((BaseContainer) obj, "New pallet created.");
        hbc.create(hbc);
        //##############################################################
        return super.create(obj);
    }

    public BaseContainer getBaseContainer(String palletNumber) {
        //Start transaction
        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
        query.setParameter("palletNumber", palletNumber);
        Helper.sess.getTransaction().commit();
        if (query.list().isEmpty()) {
            return null;
        } else {
            return (BaseContainer) query.list().get(0);
        }
    }

    //######################################################################
    public List select() {
        Helper.startSession();
        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_STATE);
        query.setParameter("state", GlobalVars.PALLET_OPEN);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        return query.list();
    }

    public static boolean isPalletNumberExist(String palletNumber) {
        //Tester si le isPalletNumberExist exist dans la base BaseContainer       
        UILog.info(String.format("Searching Container [%s] in BaseContainer.", palletNumber));
        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NUMBER);
        query.setParameter("palletNumber", palletNumber);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        if (query.list().isEmpty()) {
            //Great!!! Is a new counter
            return false;
        } else {
            UILog.severeDialog(null, ErrorMsg.APP_ERR0020, "" + palletNumber);
            UILog.severe(ErrorMsg.APP_ERR0020[0], "" + palletNumber);
            return true;

        }
    }

    @Override
    public String toString() {
        return "BaseContainer{" + "\nid=" + id + ",\n createTime=" + createTime + ",\n fifoTime=" + fifoTime + ",\n storedTime=" + storedTime + ",\n shippedTime=" + shippedTime + ",\n reservedTime=" + reservedTime + ",\n createId=" + createId + ",\n writeId=" + writeId + ",\n user=" + user + ",\n createUser=" + createUser + ",\n startTime=" + startTime + ",\n closedTime=" + closedTime + ",\n workTime=" + workTime + ",\n palletNumber=" + palletNumber + ",\n harnessPart=" + harnessPart + ",\n harnessIndex=" + harnessIndex + ",\n supplierPartNumber=" + supplierPartNumber + ",\n qtyExpected=" + qtyExpected + ",\n qtyRead=" + qtyRead + ",\n containerState=" + containerState + ",\n containerStateCode=" + containerStateCode + ",\n packType=" + packType + ",\n harnessType=" + harnessType + ",\n stdTime=" + stdTime + ",\n price=" + price + ",\n packWorkstation=" + packWorkstation + ",\n order_no=" + order_no + ",\n special_order=" + special_order + ",\n labelPerPiece=" + labelPerPiece + ",\n comment=" + comment + ",\n segment=" + segment + ",\n workplace=" + workplace + ",\n ucsLifes=" + ucsLifes + ",\n ucsId=" + ucsId + ",\n dispatchLabelNo=" + dispatchLabelNo + ",\n destination=" + destination + ",\n dispatchTime=" + dispatchTime + ",\n consignNo=" + consignNo + ",\n invoiceNo=" + invoiceNo + ",\n netWeight=" + netWeight + ",\n grossWeight=" + grossWeight + ",\n volume=" + volume + ",\n engChange=" + engChange + ",\n articleDesc=" + articleDesc + ",\n project=" + project + ",\n warehouse=" + warehouse + ",\n engChangeDate=" + engChangeDate + ",\n priority=" + priority + ",\n openningSheetCopies=" + openningSheetCopies + ",\n closingSheetCopies=" + closingSheetCopies + ",\n closingSheetFormat=" + closingSheetFormat + '}';
    }

    
    

    public BaseContainer checkContainerByState(String palletNumber, String state) {
        //Tester si contenaire exist dans la base BaseContainer        
        UILog.info(String.format("Searching Container [%s] in BaseContainer.", palletNumber));
        Helper.startSession();

        Query query = Helper.sess.createQuery(HQLHelper.GET_CONTAINER_BY_NAME_AND_STATE);
        query.setParameter("palletNumber", palletNumber);
        query.setParameter("state", state);
        UILog.info(query.getQueryString());
        Helper.sess.getTransaction().commit();
        if (!query.list().isEmpty()) {
            return (BaseContainer) query.list().get(0);
        } else {
            return null;
        }
    }

    public static Boolean isHarnessPartExist(String hp) {
        //Tester si le harness part exist dans la base UCS        

        String[] part = hp.split(GlobalVars.HARN_PART_PREFIX);

        UILog.info(String.format("Searching Harness part [%s] in ConfigUCS.", part[1]));
        List resultList = new ConfigUcs().select(part[1]);
        if (!resultList.isEmpty()) {
            return true;
        } else {
            UILog.severeDialog(null, ErrorMsg.APP_ERR0005, "" + part[1]);
            UILog.severe(ErrorMsg.APP_ERR0005[0], "" + part[1]);
            return false;
        }
    }

}
