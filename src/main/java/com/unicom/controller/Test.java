package com.unicom.controller;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.phw.eop.api.ApiException;
import org.phw.eop.api.EopClient;
import org.phw.eop.api.EopReq;
import org.phw.eop.api.EopRsp;
import org.springframework.aop.ThrowsAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unicom.request.BaseVerificationReq;
import com.unicom.request.BespeakBody;
import com.unicom.request.ReqBody;
import com.unicom.request.ReqHead;
import com.unicom.request.ReqObj;
import com.unicom.request.VerificationResponse;
import com.unicom.utils.EopConfig;
import com.unicom.utils.RSACrypto;
import com.unicom.utils.SecurityTool;

import jdk.nashorn.internal.ir.BreakableNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;;

public class Test {

	private static final String APP_CODE = "2ED13AE2598640C8B63407E0FC38646B";
	private static final String URL = "http://demo.mall.10010.com:8104/zop/";
	private static final String HMAC = "QW4bupY6wCXaVnYa89btWnmjgRHfgTf/Xvyj7skcDzYHVlvEFEAu7az+e8bv6Rj7XS8gR7myyaNGrom0L12Y9Q==";
	private static final String AES = "KN+m/EnRoj2YCmmGHM382w==";

	public static void main(String[] args) throws Exception {

		TestType3();
		// String str="";
		// for (int i = 0; i < 5; i++) {
		// testExecption(i);
		// str=str+i;
		// }
		// System.out.println(str);
		// try {
		// VerificationResponse ver = JSONObject.parseObject("",
		// VerificationResponse.class);
		//
		// System.out.println("asdfasf");
		// } catch (Exception e) {
		// System.out.println("asdfasf");
		// }
		// for (int i = 0; i < 1; i++) {
		// System.out.println(i);
		// if (i == 2) {
		//
		// break;
		// }
		//
		// }
		// System.out.println("11111");
		// String
		// result="{\"aCode\":\"560-0001\",\"aDesc\":\"110420180704111124797001020573:客户ID不存在\",\"bCode\":\"T9\",\"bDesc\":\"系统下游超时\",\"uuid\":\"0cc033d1-7d84-4f9f-b7b2-5f2528a55ea2\"}";
		// VerificationResponse ver = JSONObject.parseObject(result,
		// VerificationResponse.class);
		// if (ver.getaCode().equals("0000") ||
		// ver.getaCode().equals("560-0001")) {
		// if (ver.getbCode().equals("0000") || (!ver.getbCode().equals("0000")
		// && !ver.getbCode().equals("0001")
		// && !ver.getbCode().equals("0002"))) {
		// System.out.println("成功");
		//
		// }
		// }
		// BaseVerificationReq req = new BaseVerificationReq();
		// req.setAppCode(APP_CODE);
		//
		// ReqObj reqObj = new ReqObj();
		// String dateStr = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss.SSS").format(new Date());
		// ReqHead reqHead = new ReqHead();
		//
		// reqHead.setTimestamp(dateStr);
		// reqHead.setUuid(String.valueOf(UUID.randomUUID()));
		// reqHead.setSign(makeSign(reqHead, APP_CODE));// 验签
		//
		// ReqBody reqBody = new ReqBody();
		// reqBody.setProvince("11");
		// reqBody.setCity("110");
		// reqBody.setCertName("张三");
		// reqBody.setCertNum("1111111111111111111");
		//
		// reqObj.setBody(reqBody);
		// reqObj.setHead(reqHead);
		//
		// req.setReqObj(reqObj);
		//
		// // reOjb不需要加密时
		// String desStr = JSON.toJSONString(req);
		// System.out.println(desStr);
		//
		// JSONObject baseReq = new JSONObject();
		// baseReq.put("appCode", APP_CODE);
		// // reqObj节点需要加密时
		// baseReq.put("reqObj", SecurityTool.encrypt(AES,
		// JSON.toJSONString(reqObj)));
		// System.out.println(baseReq);
		// MediaType json = MediaType.parse("application/json; charset=utf-8");
		// OkHttpClient client = new OkHttpClient.Builder().readTimeout(5,
		// TimeUnit.SECONDS).build();
		// RequestBody requestBody = RequestBody.create(json,
		// JSON.toJSONString(baseReq));
		// Request request = new
		// Request.Builder().url(URL).post(requestBody).build();
		// Response response = client.newCall(request).execute();
		// if (response.isSuccessful()) {
		// System.out.println(response.body().string());
		// } else {
		// System.out.println(response.message());
		// }

		// TODO Auto-generated method stub
		// String orderNumber = StringUtils.GetUnicomOrderNumber();
		// System.out.println(orderNumber);
		// System.out.println(new Date().getTime());
		// System.out.println(System.currentTimeMillis());
		// System.out.println(Calendar.getInstance().getTimeInMillis());
		// System.out.println(getSecondTimestamp(new Date()));
		//
		// int timeLong =getSecondTimestampTwo(new Date());
		// Date date=new Date(timeLong * 1000L);
		//
		// System.out.println(date);

		// SimpleDateFormat sdf = new SimpleDateFormat(format);
		// String date = sdf.format(new Date(Integer.parseInt(str_num) *
		// 1000L));
		// LogUtil.debug("timestamp2Date" + "将10位时间戳:" + str_num + "转化为字符串:",
		// date);

	}

	public static int getSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Integer.valueOf(timestamp.substring(0, length - 3));
		} else {
			return 0;
		}
	}

	public static void testExecption(int i) throws Exception {
		if (i == 3) {
			throw new Exception("111");
		}
	}

	/**
	 * 获取10位时间戳
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecondTimestampTwo(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime() / 1000);
		return Integer.valueOf(timestamp);
	}

	/**
	 * 10时间戳转Date
	 * 
	 * @param timeLong
	 * @return
	 */
	public static Date TimesToDate(int timeLong) {
		return new Date(timeLong * 1000L);
	}

	public static String makeSign(ReqHead req, String appCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		// appCode+head节点（除sign节点,字母升序）+hmac密钥
		sb.append("appCode").append(appCode).append("timestamp").append(req.getTimestamp()).append("uuid")
				.append(req.getUuid()).append(HMAC);
		return SecurityTool.sign(HMAC, sb.toString());
	}

	public static void TestType3() throws Exception {
		String eopaction = "kingcard.message.get";
		EopClient client = new EopClient(EopConfig.url, EopConfig.appcode, EopConfig.signKey);
		client.setSignAlgorithm("HMAC");
		EopReq eopReq = new EopReq(eopaction);
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("channel", "tsy");
		reqMap.put("type", 3);
		reqMap.put("timestamp", System.currentTimeMillis());
		eopReq.put("ReqJson", reqMap); // 订单状态:0-正常订单

		EopRsp eopRsp = client.execute(eopReq);

		String ids = "";
		System.out.println(eopRsp.getResult());
	}
}
