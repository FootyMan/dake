package com.unicom.request;

import java.util.List;

public class OrderLogResponse {

	private String RespCode;
	private String RespDesc;
	private List<OrderLogBody> RespBody;

	public String getRespCode() {
		return RespCode;
	}

	public void setRespCode(String respCode) {
		RespCode = respCode;
	}

	public String getRespDesc() {
		return RespDesc;
	}

	public void setRespDesc(String respDesc) {
		RespDesc = respDesc;
	}

	public List<OrderLogBody> getRespBody() {
		return RespBody;
	}

	public void setRespBody(List<OrderLogBody> respBody) {
		RespBody = respBody;
	}

}
