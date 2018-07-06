package com.unicom.request;

import io.swagger.annotations.ApiModelProperty;

public class BrowseRequest {

	@ApiModelProperty(value = "渠道编码")
	private int channel;
	@ApiModelProperty(value = "产品ID")
	private int productId;
	@ApiModelProperty(value = "访问URL地址")
	private String url;
	@ApiModelProperty(value = "来源地址")
	private String referer;
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
}
