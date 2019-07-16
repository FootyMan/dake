package com.unicom.request;

/**
 * zop订单同步对象
 */
public class OrderSyncZopBodyRequest {
    /**
     * 订单id
     */
    private String orderID;
    /**
     * 产品标识：咨询联通商城管理员
     */
    private String productType;
    /**
     * 订单状态0正常订单 默认传0
     */
    private String state;
    /**
     * 订单类型:0-物流配送;1-营业厅自提
     */
    private String orderType;
    /**
     * 号码省份2位编码
     */
    private String provinceCode;
    /**
     * 号码地市2位编码
     */
    private String cityCode;
    /**
     * 号码
     */
    private String phoneNum;
    /**
     * 联系电话
     */
    private String contactNum;
    /**
     * 入网人姓名
     */
    private String certName;
    /**
     * 入网人身份证号码（身份证中的X要求大写
     */
    private String certNo;
    /**
     * 收货省份，物流配送订单必传
     */
    private String postProvinceCode;
    /**
     * 收货地市，物流配送订单必传
     */
    private String postCityCode;
    /**
     * 收货区县，物流配送订单必传
     */
    private String postDistrictCode;
    /**
     * 收货区县，物流配送订单必传
     */
    private String postAddr;
    /**
     * 收货人姓名，物流配送订单必传
     */
    private String postName;
    /**
     * 渠道编码：联通业务需求方指定分配
     */
    private String channel;
    /**
     * 营业厅编码，营业厅自提订单必传
     */
    private String storeCode;
    /**
     * 订单创建时间，格式：yyyy-mm-dd hh24:mi:ss
     */
    private String creatTime;
    /**
     * 订单更新时间，格式：yyyy-mm-dd hh24:mi:ss
     */
    private String updateTime;
    /**
     * 号码预占关键字,随机数，需以“99999”开头，最长16位数字
     */
    private String custId;
    /**
     * 手淘uid
     */
    private String uid;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getPostProvinceCode() {
        return postProvinceCode;
    }

    public void setPostProvinceCode(String postProvinceCode) {
        this.postProvinceCode = postProvinceCode;
    }

    public String getPostCityCode() {
        return postCityCode;
    }

    public void setPostCityCode(String postCityCode) {
        this.postCityCode = postCityCode;
    }

    public String getPostDistrictCode() {
        return postDistrictCode;
    }

    public void setPostDistrictCode(String postDistrictCode) {
        this.postDistrictCode = postDistrictCode;
    }

    public String getPostAddr() {
        return postAddr;
    }

    public void setPostAddr(String postAddr) {
        this.postAddr = postAddr;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
