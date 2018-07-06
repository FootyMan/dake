package com.unicom.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.phw.eop.api.ApiException;
import org.phw.eop.api.EopClient;
import org.phw.eop.api.EopReq;
import org.phw.eop.api.EopRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.unicom.bean.LiantongOrders;
import com.unicom.bean.LiantongOrdersLogs;
import com.unicom.bean.VsitData;
import com.unicom.impl.LiantongOrdersLogsServiceImpl;
import com.unicom.impl.LiantongOrdersServiceImpl;
import com.unicom.impl.VsitDataServiceImpl;
import com.unicom.request.BaseVerificationReq;
import com.unicom.request.BrowseRequest;
import com.unicom.request.OrderLogBody;
import com.unicom.request.OrderLogResponse;
import com.unicom.request.OrderRequest;
import com.unicom.request.ReqBody;
import com.unicom.request.ReqHead;
import com.unicom.request.ReqObj;
import com.unicom.request.UnicomOrderResponse;
import com.unicom.request.VerificationResponse;
import com.unicom.response.BaseResponse;
import com.unicom.utils.CommonMethod;
import com.unicom.utils.DateUtil;
import com.unicom.utils.EopConfig;
import com.unicom.utils.IPUtil;
import com.unicom.utils.LogWrite;
import com.unicom.utils.Md5Util;
import com.unicom.utils.SecurityTool;
import com.unicom.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
// @RequestMapping(value = "/order", consumes = {
// MediaType.APPLICATION_JSON_VALUE }, produces = {
// MediaType.APPLICATION_JSON_VALUE })
@RequestMapping(value = "/order")
@Api(tags = "订单接口")
public class OrderController {

	@Autowired
	private LiantongOrdersServiceImpl ordersServiceImpl;
	@Autowired
	private VsitDataServiceImpl dataServiceImpl;
	// @Autowired
	// private LiantongOrdersLogsServiceImpl ordersLogsServiceImpl;

	@ResponseBody
	@RequestMapping(value = "/ordersync", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	@ApiOperation(nickname = "swagger-registe", value = "生成订单", notes = "生成订单")
	public BaseResponse<?> OrderSync(@RequestBody OrderRequest request) throws Exception {

		// @RequestBody OrderRequest request
		System.out.println("传入：" + JSON.toJSONString(request));
		BaseResponse<Object> response = new BaseResponse<Object>();
		// 验证身份是否合法
		boolean isSuccess = VerificationIdentity(request);
		if (isSuccess) {

			String eopaction = "didicard.ordersync";
			int channel = 1288;
			String orderNumber = "dake" + StringUtils.GetUnicomOrderNumber();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int State = 0;// 订单状态:0-正常订单 1-取消订单;
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

			Date date = new Date();
			reqMap.put("CreatTime", dateFormat.format(date));// 订单创建时间，格式：yyyy-mm-dd
																// hh24:mi:ss
			reqMap.put("UpdateTime", dateFormat.format(date));// 订单更新时间，格式：yyyy-mm-dd
																// hh24:mi:ss
			reqMap.put("CustId", 99999 + StringUtils.GetRandom());// 号码预占关键字,随机数，需以“99999”开头，最长16位数字

			eopReq.put("REQ_STR", reqMap);

			EopRsp eopRsp = client.execute(eopReq);
			String jsonString = JSON.toJSONString(eopRsp);
			// SONObject jsonObject = JSONObject.fromObject(reqMap);
			System.out.println(jsonString);

			UnicomOrderResponse result = JSON.parseObject(eopRsp.getResult().toString(), UnicomOrderResponse.class);
			// 0000-传送成功;
			if (result.getRespCode().equals("0000")) {

				LiantongOrders order = new LiantongOrders();
				order.setChannel(request.getChannel());
				order.setOrderid(orderNumber);
				order.setProduct_id(request.getProductId());
				order.setProduct_type(request.getProductType());
				order.setOrder_type(request.getOrderType());
				order.setProvince_code(request.getProvinceCode());
				order.setCity_code(request.getCityCode());
				order.setDistrict_code(request.getPostDistrictCode());
				order.setPhone_num(request.getPhoneNum());
				order.setContact_num(request.getContactNum());
				order.setCert_name(request.getCertName());
				order.setState(State);
				order.setCert_no(request.getCertNo());
				order.setCert_name(request.getCertName());
				order.setCreate_time(DateUtil.getSecondTimestampTwo(date));
				order.setUpdate_time(DateUtil.getSecondTimestampTwo(date));
				order.setLiantong_orderno(result.getOrderNo());
				order.setPost_province_code(Integer.valueOf(request.getPostProvinceCode()));
				order.setPost_city_code(Integer.valueOf(request.getPostCityCode()));
				order.setAddress(request.getPostAddr());
				ordersServiceImpl.InsertOrder(order);

			} else {
				response.setMsg(result.getRespDesc());
			}
			response.setCode(result.getRespCode());
			// response.setData(reqMap);
			LogWrite.Write(reqMap, eopRsp.getResult(), eopaction);
			// OrderParametersLog(reqMap, eopRsp.getResult(), eopaction);
		} else {
			response.setCode("6999");
			response.setMsg("身份验证不合格");
		}
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	// @ApiOperation(nickname = "swagger-registe", value = "浏览接口", notes =
	// "浏览接口")
	public BaseResponse<?> BrowsePage(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<?> response = new BaseResponse<Object>();
		// try {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("IP地址：" + req.getRemoteAddr());
		long ip = IPUtil.ipToLong(CommonMethod.getIp(req));
		// System.out.println("转换后IP地址：" + ip);
		// System.out.println("转换后int地址：" + ip);
		BrowseRequest dataRequest = new BrowseRequest();
		dataRequest.setChannel(req.getParameter("channel") == null ? 0 : Integer.valueOf(req.getParameter("channel")));
		dataRequest.setProductId(
				req.getParameter("productId") == null ? 0 : Integer.valueOf(req.getParameter("productId")));
		dataRequest.setUrl(req.getParameter("url") == null ? "" : req.getParameter("url"));
		dataRequest.setReferer(req.getParameter("referer") == null ? "" : req.getParameter("referer"));
		VsitData data = new VsitData();
		data.setIp(ip);
		data.setDate(dateFormat.format(new Date()));
		// 查询是否有记录
		VsitData vsitData = dataServiceImpl.SelectDataByIp(data);

		data.setChannel(dataRequest.getChannel());
		data.setProduct_id(dataRequest.getProductId());
		data.setUri(dataRequest.getUrl());
		data.setReferer(dataRequest.getReferer());
		data.setCreate_time(DateUtil.getSecondTimestampTwo(new Date()));

		// 此处cookie需要查询库里是否存在当天的记录
		data.setCookie(Md5Util.stringByMD5(ip + StringUtils.GetRandom()));
		if (vsitData != null) {
			// System.out.println("转换后"+IPUtil.longToIP(vsitData.getIp()));
			data.setCookie(vsitData.getCookie());
		}
		dataServiceImpl.InsertData(data);

		// } catch (Exception e) {
		// response.setCode(500);
		// response.setMsg(e.getMessage());
		// }

		return response;
	}

	/**
	 * 验证身份
	 * 
	 * @param request
	 * @return
	 */
	public boolean VerificationIdentity(OrderRequest model) {

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
			reqHead.setSign(makeSign(reqHead, EopConfig.APP_CODE));// 验签

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
			VerificationResponse ver = JSONObject.parseObject(result, VerificationResponse.class);
			if (ver.getaCode().equals("0000") || ver.getaCode().equals("560-0001")) {
				if (ver.getbCode().equals("0000") || (!ver.getbCode().equals("0000") && !ver.getbCode().equals("0001")
						&& !ver.getbCode().equals("0002"))) {
					// 成功
					isSuccess = true;
				}
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

	public String makeSign(ReqHead req, String appCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		// appCode+head节点（除sign节点,字母升序）+hmac密钥
		sb.append("appCode").append(appCode).append("timestamp").append(req.getTimestamp()).append("uuid")
				.append(req.getUuid()).append(EopConfig.HMAC);
		return SecurityTool.sign(EopConfig.HMAC, sb.toString());
	}

	@ResponseBody
	@RequestMapping(value = "/orderTask", method = RequestMethod.GET)
	// @ApiOperation(nickname = "swagger-registe", value = "浏览接口", notes =
	// "浏览接口")
	public BaseResponse<?> OrderTask(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<?> obj=new BaseResponse<Object>();
		String eopaction = "kingcard.message.get";
		EopClient client = new EopClient(EopConfig.url, EopConfig.appcode, EopConfig.signKey);
		client.setSignAlgorithm("HMAC");
		EopReq eopReq = new EopReq(eopaction);
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("channel", "tsy");
		reqMap.put("type", 4);
		reqMap.put("timestamp", System.currentTimeMillis());
		eopReq.put("ReqJson", reqMap); // 订单状态:0-正常订单

		EopRsp eopRsp = client.execute(eopReq);

		String ids = "";
		OrderLogResponse response = JSONObject.parseObject(eopRsp.getResult().toString(), OrderLogResponse.class);
		if (response.getRespCode().equals("0000") && response != null && response.getRespBody() != null) {
			for (OrderLogBody item : response.getRespBody()) {
				// 订单
				int state = Integer.valueOf(item.getState());
				Date date = new Date();
				LiantongOrders order = new LiantongOrders();
				order.setOrderid(item.getOpenId());
				order.setLiantong_orderno(item.getId());
				order.setState(state);
				order.setUpdate_time(DateUtil.getSecondTimestampTwo(date));
				// 日志信息
				LiantongOrdersLogs orderLogs = new LiantongOrdersLogs();
				orderLogs.setOrderid(item.getOpenId());
				orderLogs.setLiantong_orderno(item.getId());
				orderLogs.setState(state);
				orderLogs.setRemarks(response.getRespDesc());
				orderLogs.setCreate_time(DateUtil.getSecondTimestampTwo(date));
				int result = ordersServiceImpl.UpdateOrder(order,orderLogs);
				if (result > 0) {
					ids += item.getId() + ",";
				}
			}
			LogWrite.Write(reqMap, eopRsp.getResult(), eopaction);
			if (!StringUtils.isEmpty(ids)) {
				RemoveMessage(ids);
			}
			
			System.out.println();
		}

		return obj;
	}

	/**
	 * 刪除消息
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void RemoveMessage(String ids) {
		try {
			//去掉最后一个逗号
			ids=ids.substring(0, ids.length() - 1);
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

}
