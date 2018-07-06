package com.unicom.dao;

import com.unicom.bean.VsitData;

public abstract interface VsitDataMapper {

	public abstract int InsertData(VsitData data);
	public abstract VsitData SelectDataByIp(VsitData data);
}
