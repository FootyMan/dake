package com.unicom.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtil {
	private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
		return new SimpleDateFormat(parttern);
	}

	public static String cTime(String tt) {
		String date2 = tt;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.valueOf(date2).longValue());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(c.getTime());
	}

	private static int getInteger(Date date, int dateType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(dateType);
	}

	private static String addInteger(String date, int dateType, int amount) {
		String dateString = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			myDate = addInteger(myDate, dateType, amount);
			dateString = DateToString(myDate, dateStyle);
		}
		return dateString;
	}

	private static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	private static Date getAccurateDate(List<Long> timestamps) {
		Date date = null;
		long timestamp = 0L;
		Map map = new HashMap();
		List absoluteValues = new ArrayList();

		if ((timestamps != null) && (timestamps.size() > 0)) {
			if (timestamps.size() > 1) {
				for (int i = 0; i < timestamps.size(); i++) {
					for (int j = i + 1; j < timestamps.size(); j++) {
						long absoluteValue = Math
								.abs(((Long) timestamps.get(i)).longValue() - ((Long) timestamps.get(j)).longValue());
						absoluteValues.add(Long.valueOf(absoluteValue));
						long[] timestampTmp = { ((Long) timestamps.get(i)).longValue(),
								((Long) timestamps.get(j)).longValue() };
						map.put(Long.valueOf(absoluteValue), timestampTmp);
					}

				}

				long minAbsoluteValue = -1L;
				if (!absoluteValues.isEmpty()) {
					minAbsoluteValue = ((Long) absoluteValues.get(0)).longValue();
				}
				for (int i = 0; i < absoluteValues.size(); i++) {
					for (int j = i + 1; j < absoluteValues.size(); j++) {
						if (((Long) absoluteValues.get(i)).longValue() > ((Long) absoluteValues.get(j)).longValue())
							minAbsoluteValue = ((Long) absoluteValues.get(j)).longValue();
						else {
							minAbsoluteValue = ((Long) absoluteValues.get(i)).longValue();
						}
					}
				}

				if (minAbsoluteValue != -1L) {
					long[] timestampsLastTmp = (long[]) map.get(Long.valueOf(minAbsoluteValue));
					if (absoluteValues.size() > 1) {
						timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
					} else if (absoluteValues.size() == 1) {
						long dateOne = timestampsLastTmp[0];
						long dateTwo = timestampsLastTmp[1];
						if (Math.abs(dateOne - dateTwo) < 100000000000L) {
							timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
						} else {
							long now = new Date().getTime();
							if (Math.abs(dateOne - now) <= Math.abs(dateTwo - now))
								timestamp = dateOne;
							else
								timestamp = dateTwo;
						}
					}
				}
			} else {
				timestamp = ((Long) timestamps.get(0)).longValue();
			}
		}

		if (timestamp != 0L) {
			date = new Date(timestamp);
		}
		return date;
	}

	public static boolean isDate(String date) {
		boolean isDate = false;
		if ((date != null) && (StringToDate(date) != null)) {
			isDate = true;
		}

		return isDate;
	}

	public static DateStyle getDateStyle(String date) {
		DateStyle dateStyle = null;
		Map map = new HashMap();
		List timestamps = new ArrayList();
		for (DateStyle style : DateStyle.values()) {
			Date dateTmp = StringToDate(date, style.getValue());
			if (dateTmp != null) {
				timestamps.add(Long.valueOf(dateTmp.getTime()));
				map.put(Long.valueOf(dateTmp.getTime()), style);
			}
		}
		dateStyle = (DateStyle) map.get(Long.valueOf(getAccurateDate(timestamps).getTime()));
		return dateStyle;
	}

	public static Date StringToDate(String date) {
		DateStyle dateStyle = null;
		return StringToDate(date, dateStyle);
	}

	public static Date StringToDate(String date, String parttern) {
		Date myDate = null;
		if (date != null)
			try {
				myDate = getDateFormat(parttern).parse(date);
			} catch (Exception localException) {
			}
		return myDate;
	}

	public static Date StringToDate(String date, DateStyle dateStyle) {
		Date myDate = null;
		if (dateStyle == null) {
			List timestamps = new ArrayList();
			for (DateStyle style : DateStyle.values()) {
				Date dateTmp = StringToDate(date, style.getValue());
				if (dateTmp != null) {
					timestamps.add(Long.valueOf(dateTmp.getTime()));
				}
			}
			myDate = getAccurateDate(timestamps);
		} else {
			myDate = StringToDate(date, dateStyle.getValue());
		}
		return myDate;
	}

	public static String DateToString(Date date, String parttern) {
		String dateString = null;
		if (date != null)
			try {
				dateString = getDateFormat(parttern).format(date);
			} catch (Exception localException) {
			}
		return dateString;
	}

	public static String DateToString(Date date, DateStyle dateStyle) {
		String dateString = null;
		if (dateStyle != null) {
			dateString = DateToString(date, dateStyle.getValue());
		}
		return dateString;
	}

	public static String StringToString(String date, String parttern) {
		return StringToString(date, null, parttern);
	}

	public static String StringToString(String date, DateStyle dateStyle) {
		return StringToString(date, null, dateStyle);
	}

	public static String StringToString(String date, String olddParttern, String newParttern) {
		String dateString = null;
		if (olddParttern == null) {
			DateStyle style = getDateStyle(date);
			if (style != null) {
				Date myDate = StringToDate(date, style.getValue());
				dateString = DateToString(myDate, newParttern);
			}
		} else {
			Date myDate = StringToDate(date, olddParttern);
			dateString = DateToString(myDate, newParttern);
		}
		return dateString;
	}

	public static String StringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
		String dateString = null;
		if (olddDteStyle == null) {
			DateStyle style = getDateStyle(date);
			dateString = StringToString(date, style.getValue(), newDateStyle.getValue());
		} else {
			dateString = StringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
		}
		return dateString;
	}

	public static String addYear(String date, int yearAmount) {
		return addInteger(date, 1, yearAmount);
	}

	public static Date addYear(Date date, int yearAmount) {
		return addInteger(date, 1, yearAmount);
	}

	public static String addMonth(String date, int yearAmount) {
		return addInteger(date, 2, yearAmount);
	}

	public static Date addMonth(Date date, int yearAmount) {
		return addInteger(date, 2, yearAmount);
	}

	public static String addDay(String date, int dayAmount) {
		return addInteger(date, 5, dayAmount);
	}

	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, 5, dayAmount);
	}

	public static String addHour(String date, int hourAmount) {
		return addInteger(date, 11, hourAmount);
	}

	public static Date addHour(Date date, int hourAmount) {
		return addInteger(date, 11, hourAmount);
	}

	public static String addMinute(String date, int hourAmount) {
		return addInteger(date, 12, hourAmount);
	}

	public static Date addMinute(Date date, int hourAmount) {
		return addInteger(date, 12, hourAmount);
	}

	public static String addSecond(String date, int hourAmount) {
		return addInteger(date, 13, hourAmount);
	}

	public static Date addSecond(Date date, int hourAmount) {
		return addInteger(date, 13, hourAmount);
	}

	public static int getYear(String date) {
		return getYear(StringToDate(date));
	}

	public static int getYear(Date date) {
		return getInteger(date, 1);
	}

	public static int getMonth(String date) {
		return getMonth(StringToDate(date));
	}

	public static int getMonth(Date date) {
		return getInteger(date, 2);
	}

	public static int getDay(String date) {
		return getDay(StringToDate(date));
	}

	public static int getDay(Date date) {
		return getInteger(date, 5);
	}

	public static int getHour(String date) {
		return getHour(StringToDate(date));
	}

	public static int getHour(Date date) {
		return getInteger(date, 11);
	}

	public static int getMinute(String date) {
		return getMinute(StringToDate(date));
	}

	public static int getMinute(Date date) {
		return getInteger(date, 12);
	}

	public static int getYearWeek(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(3);
	}

	public static int getYearWeek(String dateStr) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(StringToDate(dateStr));
		return ca.get(3);
	}

	public static int getSecond(String date) {
		return getSecond(StringToDate(date));
	}

	public static int getSecond(Date date) {
		return getInteger(date, 13);
	}

	public static String getDate(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD);
	}

	public static String getNowDate() {
		return DateToString(new Date(), DateStyle.YYYYMMDD);
	}
	public static String getNowDateExt() {
		return DateToString(new Date(), DateStyle.YYYY_MM_DD);
	}


	public static String getDate(Date date) {
		return DateToString(date, DateStyle.YYYY_MM_DD);
	}

	public static String getDateTime(Date date) {
		return DateToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
	}

	public static String getTime(String date) {
		return StringToString(date, DateStyle.HH_MM_SS);
	}

	public static String getTime(Date date) {
		return DateToString(date, DateStyle.HH_MM_SS);
	}
	public static String getAlltime(Date date) {
		return DateToString(date, DateStyle.YYYYMMDDHHmmssSSS);
	}
	/**
	 * 获取时分
	 * @param date
	 * @return
	 */
	public static String getHH_MM(Date date) {
		return DateToString(date, DateStyle.HH_MM);
	}

	public static Week getWeek(String date) {
		Week week = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			week = getWeek(myDate);
		}
		return week;
	}

	public static Week getWeek(Date date) {
		Week week = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNumber = calendar.get(7) - 1;
		switch (weekNumber) {
		case 0:
			week = Week.SUNDAY;
			break;
		case 1:
			week = Week.MONDAY;
			break;
		case 2:
			week = Week.TUESDAY;
			break;
		case 3:
			week = Week.WEDNESDAY;
			break;
		case 4:
			week = Week.THURSDAY;
			break;
		case 5:
			week = Week.FRIDAY;
			break;
		case 6:
			week = Week.SATURDAY;
		}

		return week;
	}

	public static int getIntervalDays(String date, String otherDate) {
		return getIntervalDays(StringToDate(date), StringToDate(otherDate));
	}

	public static int getIntervalDays(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		return (int) (time / 86400000L);
	}

	public static int getIntervalHours(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		return (int) (time / 3600000L);
	}

	public static int getIntervalMinutes(Date date, Date otherDate) {
		long time = Math.abs(date.getTime() - otherDate.getTime());
		return (int) (time / 60000L);
	}

	public static String getYearTwo(Date strDate) {
		String date = null;
		if (strDate != null) {
			Calendar startTime = Calendar.getInstance();
			int year = startTime.get(Calendar.YEAR) - 20;
			// 这里初始化时间，然后设置年份。只以年份为基准，不看时间
			startTime.clear();
			startTime.set(Calendar.YEAR, year);
			SimpleDateFormat formatter = new SimpleDateFormat("yy");
			formatter.setLenient(false);
			formatter.set2DigitYearStart(startTime.getTime());
			try {
				date = formatter.format(strDate);
			} catch (Exception e) {
			}
		}
		return date;
	}
	
	/**
	 * 获取10位时间戳
	 * @param date
	 * @return
	 */
	public static int getSecondTimestampTwo(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime() / 1000);
		return Integer.valueOf(timestamp);
	}
	
	/**
	 * 10时间戳转Date
	 * @param timeLong
	 * @return
	 */
	public static Date TimesToDate(int timeLong)
	{
		return new Date(timeLong * 1000L);
	}

}
