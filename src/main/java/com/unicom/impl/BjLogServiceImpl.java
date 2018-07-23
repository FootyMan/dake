package com.unicom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicom.bean.BjLog;
import com.unicom.dao.BjLogMapper;

@Service("logServiceImpl")
public class BjLogServiceImpl {

	@Autowired
	private BjLogMapper logMapper;

	public int InsertLog(BjLog log) {
		// TODO Auto-generated method stub
		return logMapper.InsertLog(log);
	}

	public int UpdateLog(BjLog log) {
		// TODO Auto-generated method stub
		return logMapper.UpdateLog(log);
	}

}
