package com.unicom.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单请求
 * @author HCY
 *
 */
public class OrderRequest {

	@ApiModelProperty(value = "渠道编码")
	private int channel;
	@ApiModelProperty(value = "产品ID")
	private int productId;
	@ApiModelProperty(value = "产品标识：productType=37   天神卡(无限卡)／productType=38   天神卡(日租卡)")
	private String productType;
	@ApiModelProperty(value = "订单类型：订单类型:0-物流配送;1-营业厅自提")
	private int orderType;
	@ApiModelProperty(value = "号码省份 传区号")
	private String provinceCode;
	@ApiModelProperty(value = "号码地市 传区号")
	private String cityCode;
	@ApiModelProperty(value = "手机号码")
	private String phoneNum;
	@ApiModelProperty(value = "联系电话")
	private String contactNum;
	@ApiModelProperty(value = "入网人姓名")
	private String certName;
	@ApiModelProperty(value = "入网人身份证号码")
	private String certNo;
	@ApiModelProperty(value = "物流省份编码  订单类型为0 必须传！")
	private String postProvinceCode;
	@ApiModelProperty(value = "物流地市编码  订单类型为0 必须传！")
	private String postCityCode;
	@ApiModelProperty(value = "物流区县编码  订单类型为0 必须传！ 如果只到市不必传")
	private String postDistrictCode;
	@ApiModelProperty(value = "详细地址  订单类型为0 必须传！")
	private String postAddr;
	@ApiModelProperty(value = "收货人姓名  订单类型为0 必须传！")
	private String postName;
	
	@ApiModelProperty(value = "营业厅编码  订单类型为1 必须传！")
	private String storeCode;
	
	
	@ApiModelProperty(value = "给联通传送的渠道编码")
	private String otaChannel;
	
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
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

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getOtaChannel() {
		return otaChannel;
	}

	public void setOtaChannel(String otaChannel) {
		this.otaChannel = otaChannel;
	}
	

	 
	
}
