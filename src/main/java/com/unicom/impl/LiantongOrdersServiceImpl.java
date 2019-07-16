package com.unicom.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicom.bean.LiantongOrders;
import com.unicom.bean.LiantongOrdersLogs;
import com.unicom.dao.LiantongOrdersLogsMapper;
import com.unicom.dao.LiantongOrdersMapper;
import com.unicom.request.OrderRequest;
import com.unicom.utils.CommonMethod;
import com.unicom.utils.DBContextHolder;
import com.unicom.utils.DateUtil;

@Service("ordersServiceImpl")
public class LiantongOrdersServiceImpl {

	@Autowired
	private LiantongOrdersMapper orderMapper;
	@Autowired
	private LiantongOrdersLogsMapper ordersLogsMapper;

	/***
	 * 入库
	 * 
	 * @param request
	 * @param orderNumber
	 * @param State
	 * @param date
	 * @return
	 */
	public int InsertOrder(OrderRequest request, String orderNumber, int State, Date date,HttpServletRequest req) {

		String ip = CommonMethod.getIp(req);
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
		order.setLiantong_orderno(0);// 订单号为0
		order.setPost_province_code(Integer.valueOf(request.getPostProvinceCode()));
		order.setPost_city_code(Integer.valueOf(request.getPostCityCode()));
		order.setAddress(request.getPostAddr());
		order.setStatus(0);
		order.setOtachannel(Integer.valueOf(request.getOtaChannel()));
		order.setIp(ip);
		orderMapper.InsertOrder(order);
		return order.getId();
	}

	public LiantongOrders SelectOrderByorderId(Long liantong_orderno) {
		return orderMapper.SelectOrderByorderId(liantong_orderno);
	}

	// 更新
	@Transactional
	public int UpdateOrder(LiantongOrders order, LiantongOrdersLogs ordersLogs) {

		int resul = 0;
		LiantongOrders liantongOrder = SelectOrderByorderId(order.getLiantong_orderno());
		if (liantongOrder != null) {
			ordersLogs.setChannel(liantongOrder.getChannel());
			ordersLogs.setProduct_id(liantongOrder.getProduct_id());
			ordersLogs.setOrderid(liantongOrder.getOrderid());
			order.setId(liantongOrder.getId());
			int result = orderMapper.UpdateOrder(order);
			if (result > 0) {

				// 插入订单日志
				ordersLogsMapper.InsertOrderLog(ordersLogs);
				resul = ordersLogs.getId();
				System.out.println(resul);
				// throw new RuntimeException("抛出异常");
			}
		}

		return resul;
	}

	/**
	 * 查询身份证号码下单次数
	 * 
	 * @param str
	 * @return
	 */
	public int selectOrderCount(String str) {
		return orderMapper.selectOrderCount(str);
	}

	/**
	 * 更新联通订单号
	 * 
	 * @return
	 */
	public int UpdateLiantongOrderNumber(LiantongOrders order) {
		return orderMapper.UpdateLiantongOrderNumber(order);
	}
}
