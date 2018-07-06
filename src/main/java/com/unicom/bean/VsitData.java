package com.unicom.bean;


public class VsitData {

	/**
	 * 自增id
	 */
	private int id;
	/**
	 * 渠道
	 */
	private int channel;
	/**
	 * 产品
	 */
	private int product_id;
	/**
	 * IP
	 */
	private long ip;
	/**
	 * cookie
	 */
	private String cookie;
	/**
	 * date
	 */
	private String date;
	/**
	 * rul
	 */
	private String uri;
	/**
	 * uri
	 */
	private String referer;
	/**
	 * 记录时间
	 */
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
	public long getIp() {
		return ip;
	}
	public void setIp(long ip) {
		this.ip = ip;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	 
	
	
	
}
