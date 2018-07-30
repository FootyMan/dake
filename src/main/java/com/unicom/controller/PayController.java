package com.unicom.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.wxpay.sdk.WXPay;
import com.api.wxpay.sdk.WXPayConfigImpl;
import com.api.wxpay.sdk.WXPayUtil;
import com.unicom.response.BaseResponse;

import io.swagger.annotations.Api;

@Controller

@RequestMapping(value = "/pay")
@Api(tags = "订单接口")
public class PayController {

	@ResponseBody
	@RequestMapping(value = "/wxpay", method = RequestMethod.GET)
	public BaseResponse<?> OrderSyncexec(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		WXPay wxPay = new WXPay(new WXPayConfigImpl());
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", "wx388cddff42cd57ec");//wx388cddff42cd57ec
		map.put("mch_id", "1510424721");
		map.put("nonce_str", WXPayUtil.generateNonceStr());
		map.put("sign_type", "MD5");
		map.put("body", "测试1");
		map.put("total_fee", "1");
		map.put("spbill_create_ip", "202.123.1.201");
		map.put("notify_url", "http://baidu.com");
		map.put("trade_type", "MWEB");
		map.put("out_trade_no", "dake20180723094318193207918123");
		map.put("scene_info","{\"h5_info\": {\"type\":\"Wap\",\"wap_name\": \"测试\",\"wap_url\":\"https://pay.qq.com\"}}");
		String sign = WXPayUtil.generateSignature(map, "0019767a40872c04164fac430b2ca866");
		map.put("sign", sign);
		Map<String, String> resultMap = wxPay.unifiedOrder(map);
		System.out.println(resultMap.get("return_msg"));
		BaseResponse<Object> response=new BaseResponse<Object>();
		response.setMsg(resultMap.get("return_msg"));
		response.setData(resultMap);
		return response;
	}
}
