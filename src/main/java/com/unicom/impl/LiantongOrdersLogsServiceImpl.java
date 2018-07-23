package com.unicom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicom.bean.LiantongOrdersLogs;
import com.unicom.dao.LiantongOrdersLogsMapper;

@Service("liantongOrdersLogsServiceImpl")
public class LiantongOrdersLogsServiceImpl {

	@Autowired
	private LiantongOrdersLogsMapper OrdersLogsMapper;

	public int InsertOrderLog(LiantongOrdersLogs log) {
		return OrdersLogsMapper.InsertOrderLog(log);
	}

}
