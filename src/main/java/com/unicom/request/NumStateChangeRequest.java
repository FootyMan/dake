package com.unicom.request;

public class NumStateChangeRequest {

	/**
	 * 省份编码,如:北京 11
	 */
	private String provinceCode;
	/**
	 * 地市编码，如：北京市 110
	 */
	private String cityCode;
	/**
	 * 号码
	 */
	private String serialNumber;
	
	/**
	 * 号码状态标识
	 */
	private String occupiedFlag;
	/**
	 * 预占时间标记
	 */
	private String occupiedTimeTag;
	/**
	 * 资源预占关键字，如：custId,登录id
	 */
	private String proKey;
	/**
	 * 身份证号码
	 */
	private String certCode;
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
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getOccupiedFlag() {
		return occupiedFlag;
	}
	public void setOccupiedFlag(String occupiedFlag) {
		this.occupiedFlag = occupiedFlag;
	}
	public String getOccupiedTimeTag() {
		return occupiedTimeTag;
	}
	public void setOccupiedTimeTag(String occupiedTimeTag) {
		this.occupiedTimeTag = occupiedTimeTag;
	}
	public String getProKey() {
		return proKey;
	}
	public void setProKey(String proKey) {
		this.proKey = proKey;
	}
	public String getCertCode() {
		return certCode;
	}
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	
	
}
