package com.unicom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicom.bean.LiantongOrders;
import com.unicom.bean.LiantongOrdersLogs;
import com.unicom.dao.LiantongOrdersLogsMapper;
import com.unicom.dao.LiantongOrdersMapper;
import com.unicom.utils.DBContextHolder;

@Service("ordersServiceImpl")
public class LiantongOrdersServiceImpl {

	@Autowired
	private LiantongOrdersMapper orderMapper;
	@Autowired
	private LiantongOrdersLogsMapper ordersLogsMapper;

	public int InsertOrder(LiantongOrders order) {
		return orderMapper.InsertOrder(order);
	}

	public LiantongOrders SelectOrderByorderId(String orderid) {
		return orderMapper.SelectOrderByorderId(orderid);
	}

	// 更新
	@Transactional
	public int UpdateOrder(LiantongOrders order, LiantongOrdersLogs ordersLogs) {

		int resul = 0;
		int result = orderMapper.UpdateOrder(order);
		if (result > 0) {
			LiantongOrders liantongOrder = SelectOrderByorderId(order.getOrderid());
			if (liantongOrder != null) {
				ordersLogs.setChannel(liantongOrder.getChannel());
				ordersLogs.setProduct_id(liantongOrder.getProduct_id());
			}
			// 插入订单日志
			ordersLogsMapper.InsertOrderLog(ordersLogs);
			resul = ordersLogs.getId();
			System.out.println(resul);
			//throw new RuntimeException("抛出异常");
		}
		return resul;
	}
}
