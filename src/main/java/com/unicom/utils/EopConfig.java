package com.unicom.utils;

public class EopConfig {

	/**
	 * 文件名称
	 */
	public static String fileName = "System.properties";

	/**
	 * -------------------------------------------EOP配置
	 */

	/**
	 * 测试地址，生产地址见下面
	 */
	public final static String eop_url = PropertiesUtils.ReadProperties(fileName, "eop_url");

	/**
	 * 联通商城提供
	 */
	public final static String appcode = PropertiesUtils.ReadProperties(fileName, "appcode");

	/**
	 * 联通商城提供
	 */
	public final static String signKey = PropertiesUtils.ReadProperties(fileName, "signKey");

	/********************** 身份验证配置 ZOP配置 ************************************/
	public final static String APP_CODE = PropertiesUtils.ReadProperties(fileName, "APP_CODE");
	public final static String zop_url = PropertiesUtils.ReadProperties(fileName, "zop_url");
	public final static String HMAC = PropertiesUtils.ReadProperties(fileName, "HMAC");
	public final static String AES = PropertiesUtils.ReadProperties(fileName, "AES");

	/**
	 * 无限卡
	 */
	public final static String infinite_card=PropertiesUtils.ReadProperties(fileName, "infinite_card");
	/**
	 * 日租卡
	 */
	public final static String daily_rent_card=PropertiesUtils.ReadProperties(fileName, "daily_rent_card");
	/**
	 * 预占关键字
	 */
	public final static String pre_keyword = "9999923764746";
	/**
	 * 恩 ～ 指定来源
	 * 
	 * http://139.129.97.162:8080/10010/?channel=1 测试
	 * 
	 * http://www.m-campaign.net/10010/?channel=1 正式
	 */
	// 测试地址
	public final static String qa_url = "http://139.129.97.162:8080";
	/**
	 * 测试服务器域名
	 */
	public final static String qa_domain = "test.m-campaign.net";
	// 生产地址
	public final static String pro_url = "www.m-campaign.net";
	/**
	 * 公司网址
	 */
	public final static String comp_rul = "thongfu.com";

}
