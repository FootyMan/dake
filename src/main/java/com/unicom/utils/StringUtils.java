package com.unicom.utils;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static boolean isNotEmptyString(String str) {
		return (str != null) && (!"".equals(str)) && (!"null".equalsIgnoreCase(str));
	}

	public static int strToInt(String str) {
		try {
			if (isEmpty(str)) {
				return 0;
			}
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	public static Double strToDou(String str) {
		try {
			if (isEmpty(str)) {
				return Double.valueOf(0.0D);
			}
			return Double.valueOf(Double.parseDouble(str));
		} catch (NumberFormatException e) {
		}
		return Double.valueOf(0.0D);
	}

	public static long strToLong(String str) {
		try {
			if (isEmpty(str)) {
				return 0L;
			}
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
		}
		return 0L;
	}

	public static int strToInt(String str, int defaultValue) {
		try {
			if (isEmpty(str)) {
				return defaultValue;
			}
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public static BigDecimal strToBigDecimal(String str) {
		try {
			if (isEmpty(str)) {
				return new BigDecimal(0);
			}
			return new BigDecimal(str);
		} catch (NumberFormatException e) {
		}
		return new BigDecimal(0);
	}

	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().length() == 0) || (str.equalsIgnoreCase(null))
				|| (str.equalsIgnoreCase("null"));
	}

	public static String encodeUTF8(String value) {
		try {
			return URLEncoder.encode(value, "utf-8");
		} catch (Exception e) {
			System.out.println("encodeUTF8" + e.toString());
		}
		return null;
	}

	public static String delComma(String str) {
		if (isNotEmptyString(str)) {
			if (str.endsWith(",")) {
				str = str.substring(0, str.length() - 1);
			}
			return str;
		}
		return null;
	}

	public static Date strToDate(String str) {
		Date date = null;
		try {
			if (isEmpty(str)) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			date = sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String getRandomNum(int maxValue, int minValue) {
		Integer num = Integer.valueOf((int) (minValue + Math.random() * (maxValue - minValue + 1)));
		return num.toString();
	}

	/**
	 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @param sourceDate
	 *            (前面加0的数字)
	 * @param formatLength
	 *            (要补多少位)
	 * @return 重组后的数据
	 */
	public static String frontCompWithZore(int sourceDate, int formatLength) {
		/*
		 * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
		 */
		String newString = String.format("%0" + formatLength + "d", sourceDate);
		return newString;
	}

	/**
	 * 订单号生产规则 年后两位+ 13位时间戳+用户ID(取前三位，不够前面补0) +产品ID(取前两位、不够补0)
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	public static String GetOrderNumber(int userId, int productId) {
		// 前缀年后两位开头
		String prefix = DateUtil.getYearTwo(new Date());
		// 时间戳
		String timeStamp = String.valueOf(new Date().getTime());
		String strUserId = String.valueOf(userId);
		int userIdN = 3, productIdN = 2;
		// 大于3截取后三位
		if (strUserId.length() > 3) {
			strUserId = strUserId.substring(strUserId.length() - userIdN, strUserId.length());
		} else {
			// 不大于3前面补0
			strUserId = frontCompWithZore(userId, userIdN);
		}

		String strProductId = String.valueOf(productId);
		if (strProductId.length() > 2) {
			strProductId = strProductId.substring(strProductId.length() - productIdN, strProductId.length());
		} else {
			// 不大于2前面补0
			strProductId = frontCompWithZore(productId, productIdN);
		}
		String orderNubmer = prefix + timeStamp + strUserId + strProductId;
		return orderNubmer;
	}

	/**
	 * 提现订单号 年后两位+ 13位时间戳+用户ID(取前三位，不够前面补0) +000(代表提现)
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	public static String GetOrderNumberWithdrawals(int userId) {
		// 前缀年后两位开头
		String prefix = DateUtil.getYearTwo(new Date());
		// 时间戳
		String timeStamp = String.valueOf(new Date().getTime());
		String strUserId = String.valueOf(userId);
		int userIdN = 3, productIdN = 2;
		// 大于3截取后三位
		if (strUserId.length() > 3) {
			strUserId = strUserId.substring(strUserId.length() - userIdN, strUserId.length());
		} else {
			// 不大于3前面补0
			strUserId = frontCompWithZore(userId, userIdN);
		}

		String strProductId = "000";// 代表提现
		String orderNubmer = prefix + timeStamp + strUserId + strProductId;
		return orderNubmer;
	}

	/**
	 * 获取联通订单号
	 * 
	 * @return
	 */
	public static String GetUnicomOrderNumber() {
		String prefix = DateUtil.getAlltime(new Date());
		prefix += GetRandom();
		return prefix;
	}

	/**
	 * 获取随机码
	 * 
	 * @return
	 */
	public static String GetRandom() {
		int max = 10000000;
		int min = 20000000;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return String.valueOf(s);
	}
}
