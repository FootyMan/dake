package com.unicom.request;


public class baseRequest<T> {

	/**
	 * ʱ���
	 */
//	private String timeStamp;
//	/**
//	 * �豸����
//	 */
//	private String deviceType;
	/**
	 * �ͻ��˰汾
	 */
	private String clientVersion;
	/**
	 * key+ʱ���=����
	 */
//	private String Sign;
//	/**
//	 * �û�ID
//	 */
//	private String userId;
	private T body;
//	public String getTimeStamp() {
//		return timeStamp;
//	}
//	public void setTimeStamp(String timeStamp) {
//		this.timeStamp = timeStamp;
//	}
//	public String getDeviceType() {
//		return deviceType;
//	}
//	public void setDeviceType(String deviceType) {
//		this.deviceType = deviceType;
//	}
	public String getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
//	public String getSign() {
//		return Sign;
//	}
//	public void setSign(String sign) {
//		Sign = sign;
//	}
//	public String getUserId() {
//		return userId;
//	}
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
	public T getbody() {
		return body;
	}
	public void setbody(T body) {
		this.body = body;
	}
}
