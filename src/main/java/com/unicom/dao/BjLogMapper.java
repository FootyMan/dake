package com.unicom.dao;

import com.unicom.bean.BjLog;

public abstract interface BjLogMapper {

	public abstract int InsertLog(BjLog log); 
	public abstract int UpdateLog(BjLog log); 
}
