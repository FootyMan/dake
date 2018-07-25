package com.unicom.dao;

import com.unicom.bean.LiantongOrders;

public abstract interface LiantongOrdersMapper {

	public abstract int InsertOrder(LiantongOrders order);

	public abstract int UpdateOrder(LiantongOrders order);

	public abstract LiantongOrders SelectOrderByorderId(Long liantong_orderno);
	public abstract int selectOrderCount(String str);
	/**
	 * 更新联通订单号
	 * @return
	 */
	public abstract int UpdateLiantongOrderNumber(LiantongOrders order);
}
