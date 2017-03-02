package com.bj.yt.newapplication.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dateutil {

	/** 日期格式 */
	private final static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/** 时间格式 */
	private final static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	/**
	 * 获取当前时间:Date
	 */
	public static Date getDate() {
		return new Date();
	}

	public static String getStrDate(){
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
		String date =sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 获取当前时间:Calendar
	 */
	public static Calendar getCal() {
		return Calendar.getInstance();
	}

	/**
	 * 日期转换为字符串:yyyy-MM-dd
	 */
	public static String dateToStr(Date date) {
		if (date != null)
			return dateFormat.get().format(date);
		return null;
	}

	/**
	 * 时间转换为字符串:yyyy-MM-dd HH:mm:ss
	 */
	public static String timeToStr(Date date) {
		if (date != null)
			return timeFormat.get().format(date);
		return null;
	}

	/**
	 * 字符串转换为日期:yyyy-MM-dd
	 */
	public static Date strToDate(String str) {
		Date date = null;
		try {
			date = dateFormat.get().parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换为时间:yyyy-MM-dd HH:mm:ss
	 */
	public static Date strToTime(String str) {
		Date date = null;
		try {
			date = timeFormat.get().parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 友好的方式显示时间
	 */
	public static String friendlyFormat(Date date) {
//		Date date = strToTime(str);
		if (date == null)
			return ":)";
		Calendar now = getCal();
		String time = new SimpleDateFormat("HH:mm").format(date);

		// 第一种情况，日期在同一天
		String curDate = dateFormat.get().format(now.getTime());
		String paramDate = dateFormat.get().format(date);
		if (curDate.equals(paramDate)) {
			int minute = (int) ((now.getTimeInMillis() - date.getTime()) / 60000);
			if (minute <= 2)
				return "刚刚";
			if (minute >= 2&&minute<60)
				return minute+"分钟前";
			int hour = (int) ((now.getTimeInMillis() - date.getTime()) / 3600000);
			if (hour >=1)
				return hour+"小时前";

			return "";
		}

		// 第二种情况，不在同一天
		int days = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000);
		if (days == 1)
			return "昨天";
		if (days == 2)
			return "前天";
		if (days >= 3 && days <= 30) {
			return days + "天前";
		}

		// 第二种情况，不在同一月
		int months = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000/30);
		if (months>=1 && months<=12)
			return months+"月前";

		// 第二种情况，不在同一年
		int years = (int) ((getBegin(getDate()).getTime() - getBegin(date).getTime()) / 86400000/12);
		if (years>=1)
			return years+"年前";

		return timeToStr(date);
	}

	/**
	 * 返回日期的0点:2012-07-07 20:20:20 --> 2012-07-07 00:00:00
	 */
	public static Date getBegin(Date date) {
		return strToTime(dateToStr(date) + " 00:00:00");
	}
}
