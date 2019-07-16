package com.unicom.bean;

public class LiantongOrders {

	/**
	 * 自增id
	 */
	private int id;
	/**
	 * 渠道
	 */
	private int channel;
/**
 * 订单id
 */
	private String orderid;
	/**
	 * 产品id
	 */
	private int product_id;
	/**
	 * 产品标识
	 */
	private String product_type;
	/**
	 * 订单类型:0-物流配送;1-营业厅自提
	 */
	private int order_type;
	/**
	 * 省份编码
	 */
	private String province_code;
	/**
	 * 城市编码
	 */
	private String city_code;
	/**
	 * 区县编码
	 */
	private String district_code;
	/**
	 * 号码
	 */
	private String phone_num;
	/**
	 * 联系电话
	 */
	private String contact_num;
	/**
	 * 入网人姓名
	 */
	private String cert_name;
	/**
	 * 入网人身份证号码(身份证中的X要求大写)
	 */
	private String cert_no;
	/**
	 * 订单状态：-1取消订单，0正常订单
	 */
	private int state;
	/**
	 * 创建时间
	 */
	private int create_time;
	/**
	 * 更新时间
	 */
	private int update_time;
	/**
	 * 联通订单
	 */
	private long liantong_orderno;
	
	/**
	 * 物流省份
	 */
	private int post_province_code;
	/**
	 * 城市省份
	 */
	private int post_city_code;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 下单状态 0下单成功 1联通返回订单号激活
	 */
	private int status;
	
	/**
	 * 下单失败原因
	 */
	private String remarks;
	/**
	 * 下单IP
	 */
	private String ip; 
	/**
	 * 下单
	 */
	private int otachannel;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getDistrict_code() {
		return district_code;
	}
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getContact_num() {
		return contact_num;
	}
	public void setContact_num(String contact_num) {
		this.contact_num = contact_num;
	}
	public String getCert_name() {
		return cert_name;
	}
	public void setCert_name(String cert_name) {
		this.cert_name = cert_name;
	}
	public String getCert_no() {
		return cert_no;
	}
	public void setCert_no(String cert_no) {
		this.cert_no = cert_no;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	public long getLiantong_orderno() {
		return liantong_orderno;
	}
	public void setLiantong_orderno(long liantong_orderno) {
		this.liantong_orderno = liantong_orderno;
	}
	public int getPost_province_code() {
		return post_province_code;
	}
	public void setPost_province_code(int post_province_code) {
		this.post_province_code = post_province_code;
	}
	public int getPost_city_code() {
		return post_city_code;
	}
	public void setPost_city_code(int post_city_code) {
		this.post_city_code = post_city_code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getOtachannel() {
		return otachannel;
	}
	public void setOtachannel(int otachannel) {
		this.otachannel = otachannel;
	}
	
}
