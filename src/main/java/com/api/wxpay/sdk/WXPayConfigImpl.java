package com.api.wxpay.sdk;

import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl extends WXPayConfig {

	private byte[] certData;
	private static WXPayConfigImpl INSTANCE;

	public WXPayConfigImpl() throws Exception {
		// String certPath = "D://CERT/common/apiclient_cert.p12";
		// File file = new File(certPath);
		// InputStream certStream = new FileInputStream(file);
		// this.certData = new byte[(int) file.length()];
		// certStream.read(this.certData);
		// certStream.close();
	}

	public static WXPayConfigImpl getInstance() throws Exception {
		if (INSTANCE == null) {
			synchronized (WXPayConfigImpl.class) {
				if (INSTANCE == null) {
					INSTANCE = new WXPayConfigImpl();
				}
			}
		}
		return INSTANCE;
	}

	public String getAppID() {
		return "wx81039e1a7a12c6a3";
	}

	public String getMchID() {
		return "1269287301";
	}

	public String getKey() {
		return "acd72d726b4f03b1b4c9d68b13987af9";
	}

	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 2000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	public String getPrimaryDomain() {
		return "api.mch.weixin.qq.com";
	}

	public String getAlternateDomain() {
		return "api2.mch.weixin.qq.com";
	}

	IWXPayDomain getWXPayDomain() {
		return WXPayDomainSimpleImpl.instance();
	}

	@Override
	public int getReportWorkerNum() {
		return 1;
	}

	@Override
	public int getReportBatchSize() {
		return 2;
	}
}
