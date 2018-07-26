package com.unicom.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.phw.eop.api.ApiException;
import org.phw.eop.api.EopClient;
import org.phw.eop.api.EopReq;
import org.phw.eop.api.EopRsp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unicom.request.BaseVerificationReq;
import com.unicom.request.NumStateChangeRequest;
import com.unicom.request.OrderLogResponse;
import com.unicom.request.OrderRequest;
import com.unicom.request.ReqBody;
import com.unicom.request.ReqHead;
import com.unicom.request.ReqObj;
import com.unicom.request.UnicomOrderResponse;
import com.unicom.request.VerificationResponse;
import com.unicom.response.NumStateChangeResponse;
import com.unicom.utils.EopConfig;
import com.unicom.utils.LogWrite;
import com.unicom.utils.RSAUtils;
import com.unicom.utils.SecurityTool;
import com.unicom.utils.StringUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderBusiness {

	public static String makeSign(ReqHead req, String appCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		// appCode+head节点（除sign节点,字母升序）+hmac密钥
		sb.append("appCode").append(appCode).append("timestamp").append(req.getTimestamp()).append("uuid")
				.append(req.getUuid()).append(EopConfig.HMAC);
		return SecurityTool.sign(EopConfig.HMAC, sb.toString());
	}

	/**
	 * 刪除消息
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public static void RemoveMessage(String ids) {
		try {
			// 去掉最后一个逗号
			ids = ids.substring(0, ids.length() - 1);
			String eopaction = "kingcard.message.del";
			EopClient client = new EopClient(EopConfig.url, EopConfig.appcode, EopConfig.signKey);
			client.setSignAlgorithm("HMAC");
			EopReq eopReq = new EopReq(eopaction);
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("ids", ids);
			reqMap.put("type", 4);
			reqMap.put("timestamp", System.currentTimeMillis());
			eopReq.put("ReqJson", reqMap); // 订单状态:0-正常订单
			EopRsp eopRsp = client.execute(eopReq);
			OrderLogResponse response = JSONObject.parseObject(eopRsp.getResult().toString(), OrderLogResponse.class);
			if (response.getRespCode().equals("0000")) {

			}
			LogWrite.Write(reqMap, eopRsp.getResult(), eopaction);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 验证身份
	 * 
	 * @param request
	 * @return
	 */
	public static boolean VerificationIdentity(OrderRequest model) {

		boolean isSuccess = false;
		ReqObj reqObj = new ReqObj();
		JSONObject baseReq = new JSONObject();
		String result = "";
		try {
			BaseVerificationReq req = new BaseVerificationReq();
			req.setAppCode(EopConfig.APP_CODE);

			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
			ReqHead reqHead = new ReqHead();

			reqHead.setTimestamp(dateStr);
			reqHead.setUuid(String.valueOf(UUID.randomUUID()));
			reqHead.setSign(OrderBusiness.makeSign(reqHead, EopConfig.APP_CODE));// 验签

			ReqBody reqBody = new ReqBody();
			reqBody.setProvince(model.getProvinceCode());
			reqBody.setCity(model.getCityCode());
			reqBody.setCertName(model.getCertName());
			reqBody.setCertNum(model.getCertNo());

			reqObj.setBody(reqBody);
			reqObj.setHead(reqHead);

			req.setReqObj(reqObj);

			// reOjb不需要加密时
			String desStr = JSON.toJSONString(req);
			System.out.println(desStr);

			baseReq.put("appCode", EopConfig.APP_CODE);
			// reqObj节点需要加密时
			String beforeEnc = JSON.toJSONString(reqObj);// 加密前
			System.out.println("加密前" + beforeEnc);
			String afterEnc = SecurityTool.encrypt(EopConfig.AES, beforeEnc);// 加密后
			baseReq.put("reqObj", afterEnc);
			System.out.println(baseReq);
			okhttp3.MediaType json = okhttp3.MediaType.parse("application/json; charset=utf-8");
			OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
			okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(json, JSON.toJSONString(baseReq));
			Request request = new Request.Builder().url(EopConfig.URL).post(requestBody).build();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				result = response.body().string();
			} else {
				result = response.message();
			}

			// 返回空和超时----放过
			if (StringUtils.isEmpty(result)) {
				LogWrite.Write("加密前:" + JSONObject.toJSONString(reqObj) + "加密后:" + baseReq.toString(),
						"联通接口返回空" + result, "Verification");
				return true;
			}
			VerificationResponse ver = JSONObject.parseObject(result, VerificationResponse.class);
			// if (ver.getaCode().equals("0000") ||
			// ver.getaCode().equals("560-0001")) {
			if (ver.getbCode().equals("0000") || (!ver.getbCode().equals("0000") && !ver.getbCode().equals("0001")
					&& !ver.getbCode().equals("0002") && !ver.getbCode().equals("0003"))) {
				// 成功
				isSuccess = true;
			}
			// }
			if (ver.getaCode().equals("600-T9"))// 超时放过
			{
				isSuccess = true;
			}
			LogWrite.Write("加密前:" + JSONObject.toJSONString(reqObj) + "加密后:" + baseReq.toString(), result,
					"Verification");
		} catch (Exception e) {
			// TODO: handle exception
			LogWrite.Write("加密前:" + JSONObject.toJSONString(reqObj) + "加密后:" + baseReq.toString(), result,
					"Verification");
		}

		return isSuccess;
	}

	/**
	 * 参数解密
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static OrderRequest ParameterDecrypt(HttpServletRequest req) throws Exception {
		OrderRequest request = new OrderRequest();
		req.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JSONObject jsonObject = JSONObject.parseObject(sb.toString());

		// System.out.println(sb.toString());
		// System.out.println(jsonObject.get("channel"));
		request.setChannel(Integer.valueOf(RSAUtils.decryptBase64(jsonObject.get("channel").toString())));
		System.out.println("渠道：" + request.getChannel());
		request.setProductId(Integer.valueOf(RSAUtils.decryptBase64(jsonObject.get("productId").toString())));
		request.setProductType(RSAUtils.decryptBase64(jsonObject.get("productType").toString()));
		request.setOrderType(Integer.valueOf(RSAUtils.decryptBase64(jsonObject.get("orderType").toString())));
		request.setProvinceCode(RSAUtils.decryptBase64(jsonObject.get("provinceCode").toString()));
		request.setCityCode(RSAUtils.decryptBase64(jsonObject.get("cityCode").toString()));
		request.setPhoneNum(RSAUtils.decryptBase64(jsonObject.get("phoneNum").toString()));
		request.setContactNum(RSAUtils.decryptBase64(jsonObject.get("contactNum").toString()));
		request.setCertName(RSAUtils.decryptBase64(jsonObject.get("certName").toString()));
		request.setCertNo(RSAUtils.decryptBase64(jsonObject.get("certNo").toString()));
		request.setPostProvinceCode(RSAUtils.decryptBase64(jsonObject.get("postProvinceCode").toString()));
		request.setPostCityCode(RSAUtils.decryptBase64(jsonObject.get("postCityCode").toString()));
		request.setPostDistrictCode(RSAUtils.decryptBase64(jsonObject.get("postDistrictCode").toString()));
		request.setPostAddr(RSAUtils.decryptBase64(jsonObject.get("postAddr").toString()));
		request.setPostName(RSAUtils.decryptBase64(jsonObject.get("postName").toString()));
		return request;
	}

	/**
	 * 发送同步订单
	 * 
	 * @param request
	 * @param date
	 * @param State
	 * @return
	 * @throws Exception
	 */
	public static UnicomOrderResponse OrderSyncSend(OrderRequest request, Date date, int State, String orderNumber)
			throws Exception {
		String eopaction = "didicard.ordersync";
		int channel = 1288;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EopClient client = new EopClient(EopConfig.url, EopConfig.appcode, EopConfig.signKey);
		client.setSignAlgorithm("HMAC");
		EopReq eopReq = new EopReq(eopaction);
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("OrderID", orderNumber); // 业务参数封装
		reqMap.put("ProductType", request.getProductType()); // 产品标识：咨询联通商城管理员
		reqMap.put("State", State);
		reqMap.put("OrderType", request.getOrderType()); // 订单类型:0-物流配送;1-营业厅自提
		reqMap.put("ProvinceCode", request.getProvinceCode()); // 号码省份
		reqMap.put("CityCode", request.getCityCode()); // 号码地市
		reqMap.put("PhoneNum", request.getPhoneNum()); // 号码
		reqMap.put("ContactNum", request.getContactNum()); // 联系电话
		reqMap.put("CertName", request.getCertName()); // 入网人姓名
		reqMap.put("CertNo", request.getCertNo()); // 入网人身份证号码（身份证中的X要求大写
		reqMap.put("PostProvinceCode", request.getPostProvinceCode()); // 收货省份，物流配送订单必传
		reqMap.put("PostCityCode", request.getPostCityCode()); // 收货地市，物流配送订单必传
		reqMap.put("PostDistrictCode", request.getPostDistrictCode()); // 收货区县，物流配送订单必传
		reqMap.put("PostAddr", request.getPostAddr()); // 详细地址，物流配送订单必传
		reqMap.put("PostName", request.getPostName()); // 收货人姓名，物流配送订单必传
		reqMap.put("channel", channel);
		if (request.getOrderType() == 1) {
			reqMap.put("StoreCode", request.getStoreCode()); // 营业厅编码，营业厅自提订单必传
																// reqMap.put("PostName",
																// "张三");
			// 短信验证码，非必传
		}
		reqMap.put("CreatTime", dateFormat.format(date));// 订单创建时间，格式：yyyy-mm-dd
															// hh24:mi:ss
		reqMap.put("UpdateTime", dateFormat.format(date));// 订单更新时间，格式：yyyy-mm-dd
															// hh24:mi:ss
		reqMap.put("CustId", 99999 + StringUtils.GetRandom());// 号码预占关键字,随机数，需以“99999”开头，最长16位数字
		reqMap.put("Uid", orderNumber);// 手淘uid
		eopReq.put("REQ_STR", reqMap);

		UnicomOrderResponse result = null;

		for (int i = 0; i < 6; i++) {
			EopRsp eopRsp = client.execute(eopReq);
			String jsonString = JSON.toJSONString(eopRsp);
			// SONObject jsonObject = JSONObject.fromObject(reqMap);
			System.out.println(jsonString);

			result = JSON.parseObject(eopRsp.getResult().toString(), UnicomOrderResponse.class);
			LogWrite.Write(reqMap, eopRsp.getResult(), eopaction);
			if (result.getRespCode().equals("0005") || result.getRespCode().equals("9999")) {
				System.out.println("重试");
			} else {
				break;
			}

		}

		return result;
	}

	/**
	 * 号码选占
	 * 
	 * @param request
	 * @return
	 */
	public static NumStateChangeResponse NumStateChange(OrderRequest model) {

		NumStateChangeResponse res = new NumStateChangeResponse();
		ReqObj reqObj = new ReqObj();
		JSONObject baseReq = new JSONObject();
		String result = "";
		try {
			BaseVerificationReq req = new BaseVerificationReq();
			req.setAppCode(EopConfig.APP_CODE);

			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
			ReqHead reqHead = new ReqHead();

			reqHead.setTimestamp(dateStr);
			reqHead.setUuid(String.valueOf(UUID.randomUUID()));
			reqHead.setSign(OrderBusiness.makeSign(reqHead, EopConfig.APP_CODE));// 验签

			NumStateChangeRequest reqBody = new NumStateChangeRequest();
			reqBody.setCertCode(model.getCertNo());
			reqBody.setCityCode(model.getCityCode());
			reqBody.setProvinceCode(model.getProvinceCode());
			reqBody.setSerialNumber(model.getPhoneNum());
			reqBody.setOccupiedFlag("S");// S选占 D延时选占 R释放资源
			reqBody.setOccupiedTimeTag("S8");// S1选占30分钟 S2选占一小时 S3选占3天 S8选占一个月
												// D1延时选占到次日的23点, T+1
												// D2延时选占到后天的23点, T+2 D8延时选占一个月
												// D9延时选占，永久选占,2059年12月
			reqBody.setProKey(model.getCertNo());// 资源预占关键字
			reqObj.setBody(reqBody);
			reqObj.setHead(reqHead);

			req.setReqObj(reqObj);

			// reOjb不需要加密时
			String desStr = JSON.toJSONString(req);
			System.out.println(desStr);

			baseReq.put("appCode", EopConfig.APP_CODE);
			// reqObj节点需要加密时
			String beforeEnc = JSON.toJSONString(reqObj);// 加密前
			System.out.println("加密前" + beforeEnc);

			String afterEnc = SecurityTool.encrypt(EopConfig.AES, beforeEnc);// 加密后
			baseReq.put("reqObj", afterEnc);
			System.out.println(baseReq);

			okhttp3.MediaType json = okhttp3.MediaType.parse("application/json; charset=utf-8");
			OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
			okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(json, JSON.toJSONString(baseReq));
			Request request = new Request.Builder().url(EopConfig.ModelStateUrl).post(requestBody).build();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				result = response.body().string();
			} else {
				result = response.message();
			}
			res = JSONObject.parseObject(result, NumStateChangeResponse.class);

			LogWrite.Write("加密前:" + JSONObject.toJSONString(reqObj) + "加密后:" + baseReq.toString(), result,
					"NumStateChange");
		} catch (Exception e) {
			// TODO: handle exception
			LogWrite.Write("加密前:" + JSONObject.toJSONString(reqObj) + "加密后:" + baseReq.toString(), result,
					"NumStateChange");
		}
		return res;
	}

	/**
	 * 来源是否符合
	 * @param referer
	 * @return
	 */
	public static boolean isRefererOk(HttpServletRequest req)
	{
		String referer = req.getHeader("Referer");
		if (referer != null && (referer.contains(EopConfig.qa_url) || referer.contains(EopConfig.pro_url)
				|| referer.contains(EopConfig.comp_rul) || referer.contains(EopConfig.qa_domain))) {
			return true;
		}
		return false;
	}

}
