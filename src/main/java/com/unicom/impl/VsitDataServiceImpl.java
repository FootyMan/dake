package com.unicom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicom.bean.VsitData;
import com.unicom.dao.VsitDataMapper;
import com.unicom.utils.DBContextHolder;

@Service("dataServiceImpl")
public class VsitDataServiceImpl {

	@Autowired
	private VsitDataMapper dataMapper;

	public int InsertData(VsitData data) {
		return dataMapper.InsertData(data);
	}

	/**
	 * 根据IP查询当天是否有记录 有记录Cookie不更新
	 * @param data
	 * @return
	 */
	public VsitData SelectDataByIp(VsitData data) {
		return dataMapper.SelectDataByIp(data);
	}
}
