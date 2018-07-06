package com.unicom.request;

public class BaseVerificationReq {

	private String appCode;
	private ReqObj reqObj;
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public ReqObj getReqObj() {
		return reqObj;
	}
	public void setReqObj(ReqObj reqObj) {
		this.reqObj = reqObj;
	}
	
}
