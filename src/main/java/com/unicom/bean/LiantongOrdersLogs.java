package com.unicom.bean;

public class LiantongOrdersLogs {

	private int id;
	private int channel;
	private int product_id;
	private String orderid;
	private long liantong_orderno;
	private int state;
	private String remarks;
	private int create_time;
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
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public long getLiantong_orderno() {
		return liantong_orderno;
	}
	public void setLiantong_orderno(long liantong_orderno) {
		this.liantong_orderno = liantong_orderno;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

}
