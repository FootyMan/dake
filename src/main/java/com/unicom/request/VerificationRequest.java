package com.unicom.request;

import io.swagger.annotations.ApiModelProperty;

public class VerificationRequest {

	@ApiModelProperty(value = "省份编码")
	private String province;
	@ApiModelProperty(value = "城市编码")
	private String city;
	@ApiModelProperty(value = "姓名")
	private String certName;
	@ApiModelProperty(value = "身份证号")
	private String certNum;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getCertNum() {
		return certNum;
	}
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	
}
