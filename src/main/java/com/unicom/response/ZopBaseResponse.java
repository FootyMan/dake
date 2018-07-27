package com.unicom.response;

/**
 * zop接口返回公共信息
 * @author HCY
 *
 */
public class ZopBaseResponse {
	private String rspCode;
	private String rspDesc;
	private String uuid;
	private String body;
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspDesc() {
		return rspDesc;
	}
	public void setRspDesc(String rspDesc) {
		this.rspDesc = rspDesc;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
