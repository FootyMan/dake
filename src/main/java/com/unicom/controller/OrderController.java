package com.unicom.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.ast.Var;
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
import com.unicom.business.OrderBusiness;
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
import com.unicom.utils.RSAUtils;
import com.unicom.utils.SecurityTool;
import com.unicom.utils.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

	@ResponseBody
	@RequestMapping(value = "/ordersync", method = RequestMethod.GET)
	public BaseResponse<?> OrderSync(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<Object> response = new BaseResponse<Object>();
		String referer = req.getHeader("Referer");
		if (referer != null && (referer.contains(EopConfig.qa_url) || referer.contains(EopConfig.pro_url) || referer.contains(EopConfig.comp_rul))) {

			OrderRequest request = new OrderRequest();
			request.setChannel(req.getParameter("channel") == null ? 0 : Integer.valueOf(req.getParameter("channel")));
			request.setProductId(
					req.getParameter("productId") == null ? 0 : Integer.valueOf(req.getParameter("productId")));
			request.setProductType(req.getParameter("productType") == null ? "" : req.getParameter("productType"));
			request.setOrderType(
					req.getParameter("orderType") == null ? 0 : Integer.valueOf(req.getParameter("orderType")));
			request.setProvinceCode(req.getParameter("provinceCode") == null ? "" : req.getParameter("provinceCode"));
			request.setCityCode(req.getParameter("cityCode") == null ? "" : req.getParameter("cityCode"));
			request.setPhoneNum(req.getParameter("phoneNum") == null ? "" : req.getParameter("phoneNum"));
			request.setContactNum(req.getParameter("contactNum") == null ? "" : req.getParameter("contactNum"));
			request.setCertName(req.getParameter("certName") == null ? "" : req.getParameter("certName"));
			request.setCertNo(req.getParameter("certNo") == null ? "" : req.getParameter("certNo"));
			request.setPostProvinceCode(
					req.getParameter("postProvinceCode") == null ? "" : req.getParameter("postProvinceCode"));
			request.setPostCityCode(req.getParameter("postCityCode") == null ? "" : req.getParameter("postCityCode"));
			request.setPostDistrictCode(
					req.getParameter("postDistrictCode") == null ? "" : req.getParameter("postDistrictCode"));
			request.setPostAddr(req.getParameter("postAddr") == null ? "" : req.getParameter("postAddr"));
			request.setPostName(req.getParameter("postName") == null ? "" : req.getParameter("postName"));

			// @RequestBody OrderRequest request
			System.out.println("传入：" + JSON.toJSONString(request));

			// 验证身份是否合法
			boolean isSuccess = OrderBusiness.VerificationIdentity(request);
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

		} else {
			response.setCode("401");
			response.setMsg("非法请求");
		}

		return response;
	}

	// @CrossOrigin(origins = { EopConfig.qa_url, EopConfig.pro_url }, maxAge =
	// 3600)
	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	// @RequestMapping(value = "/ordersyncexec", consumes = {
	// MediaType.APPLICATION_JSON_VALUE }, produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	@RequestMapping(value = "/ordersyncexec", method = RequestMethod.POST)
	@ApiOperation(nickname = "swagger-registe", value = "生成订单", notes = "生成订单")
	public BaseResponse<?> ordersyncexec(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<Object> response = new BaseResponse<Object>();
		String referer = req.getHeader("Referer");

		if (referer != null && (referer.contains(EopConfig.qa_url) || referer.contains(EopConfig.pro_url) || referer.contains(EopConfig.comp_rul))) {
			OrderRequest request = new OrderRequest();
			Map<String, String> map = new HashMap<String, String>();
			req.setCharacterEncoding("UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonObject = JSONObject.parseObject(sb.toString());

			System.out.println(sb.toString());
			System.out.println(jsonObject.get("channel"));
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

			// 验证身份是否合法
			boolean isSuccess = OrderBusiness.VerificationIdentity(request);
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
		} else {
			response.setCode("401");
			response.setMsg("非法请求");
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public void BrowsePage(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<?> response = new BaseResponse<Object>();
		String referer = req.getHeader("Referer");
		if (referer != null && (referer.contains(EopConfig.qa_url) || referer.contains(EopConfig.pro_url) || referer.contains(EopConfig.comp_rul))) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println("IP地址：" + req.getRemoteAddr());
			long ip = IPUtil.ipToLong(CommonMethod.getIp(req));
			// System.out.println("转换后IP地址：" + ip);
			// System.out.println("转换后int地址：" + ip);
			BrowseRequest dataRequest = new BrowseRequest();
			dataRequest
					.setChannel(req.getParameter("channel") == null ? 0 : Integer.valueOf(req.getParameter("channel")));
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
		}
		// } catch (Exception e) {
		// response.setCode(500);
		// response.setMsg(e.getMessage());
		// }

		// return response;
	}

	@ResponseBody
	@RequestMapping(value = "/orderTask", method = RequestMethod.GET)
	public BaseResponse<?> OrderTask(HttpServletRequest req, HttpServletResponse res) throws Exception {

		BaseResponse<?> obj = new BaseResponse<Object>();
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
		System.out.println(eopRsp.getResult());
		OrderLogResponse response = JSONObject.parseObject(eopRsp.getResult().toString(), OrderLogResponse.class);
		if (response.getRespCode().equals("0000") && response != null && response.getRespBody() != null) {
			for (OrderLogBody item : response.getRespBody()) {
				// 订单
				int state = Integer.valueOf(item.getState());
				Date date = new Date();
				LiantongOrders order = new LiantongOrders();
				order.setLiantong_orderno(item.getOrder());
				order.setState(state);
				order.setUpdate_time(DateUtil.getSecondTimestampTwo(date));
				// 日志信息
				LiantongOrdersLogs orderLogs = new LiantongOrdersLogs();
				orderLogs.setLiantong_orderno(item.getOrder());
				orderLogs.setState(state);
				orderLogs.setRemarks(response.getRespDesc());
				orderLogs.setCreate_time(DateUtil.getSecondTimestampTwo(date));
				int result = ordersServiceImpl.UpdateOrder(order, orderLogs);
				// if (result > 0) {
				ids += item.getId() + ",";
				// }
			}

			if (!StringUtils.isEmpty(ids)) {
				OrderBusiness.RemoveMessage(ids);
			}

			System.out.println();
		}
		LogWrite.Write(reqMap, eopRsp.getResult(), eopaction);
		return obj;
	}

}
