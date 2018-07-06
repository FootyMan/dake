package com.unicom.request;

/**
 * 联通下单返回参数
 * 
 * @author HCY
 *
 */
public class UnicomOrderResponse {

	/**
	 * 0000-传送成功; 0002-当前客户正在下单（仅在并发请求情况下出现，不是业务 逻辑异常）; 0005-号码查询服务失败;
	 * 9999-其他错误; 8888-订单已存在 注：针对0005和9999报错，建议第三方增加订单同步重试机
	 * 制，设置重试次数上限，如5。如都返回失败联系商城管理员走 运维处理
	 */
	private String RespCode	;
	
	/**
	 * 错误描述
	 */
	private String RespDesc;
	/**
	 * 订单号
	 */
	private long orderNo;

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

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	
	 
}
