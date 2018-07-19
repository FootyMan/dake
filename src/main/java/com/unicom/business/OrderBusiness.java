package com.unicom.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.phw.eop.api.EopClient;
import org.phw.eop.api.EopReq;
import org.phw.eop.api.EopRsp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unicom.request.BaseVerificationReq;
import com.unicom.request.OrderLogResponse;
import com.unicom.request.OrderRequest;
import com.unicom.request.ReqBody;
import com.unicom.request.ReqHead;
import com.unicom.request.ReqObj;
import com.unicom.request.VerificationResponse;
import com.unicom.utils.EopConfig;
import com.unicom.utils.LogWrite;
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
			if (ver.getaCode().equals("0000") || ver.getaCode().equals("560-0001")) {
				if (ver.getbCode().equals("0000") || (!ver.getbCode().equals("0000") && !ver.getbCode().equals("0001")
						&& !ver.getbCode().equals("0002") && !ver.getbCode().equals("0003"))) {
					// 成功
					isSuccess = true;
				}
			}
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
}
