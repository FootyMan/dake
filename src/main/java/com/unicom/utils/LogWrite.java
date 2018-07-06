package com.unicom.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.unicom.bean.BjLog;
import com.unicom.impl.BjLogServiceImpl;

public class LogWrite {

	public static ExecutorService executors = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	/**
	 * 
	 * @param eopReq
	 * @param eopRsp
	 * @param inteface
	 */
	public static void Write(Object eopReq, Object eopRsp, String inteface) {
		BjLog log = new BjLog();
		log.setLogType(1);
		log.setReqMsg(JSON.toJSONString(eopReq));
		log.setResMsg(JSON.toJSONString(eopRsp));
		if (!inteface.equals("didicard.ordersync")) {
			log.setReqMsg(eopReq.toString());
			log.setResMsg(eopRsp.toString());
		}
		log.setIfName(inteface);
		executors.execute(new LogThread(log));
	}

	public static class LogThread implements Runnable {

		private BjLog log;

		public LogThread(BjLog log) {
			this.log = log;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			BjLogServiceImpl logServiceImpl = (BjLogServiceImpl) SpringBeanUtils.getBean("logServiceImpl");
			logServiceImpl.InsertLog(log);
		}

	}
}
