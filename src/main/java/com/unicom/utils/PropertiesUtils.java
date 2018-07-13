package com.unicom.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public static String ReadProperties(String fileName, String key) {

		Properties prop = new Properties();
		try {
			// 当前线程路径
			String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
			InputStream in = new BufferedInputStream(new FileInputStream(path));
			prop.load(in); /// 加载属性列表
			return prop.getProperty(key);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return key;
	}
}
