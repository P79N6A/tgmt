package com.tbds.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/**
	 * 比较日期大小
	 * @param str_date1      第一个时间
	 * @param str_date2      第二个时间
	 * @param dateFormat 日期格式
	 * @return -100: 日期格式有误，1: 第一个日期大，0: 两个日期一样，-1: 第二个日期大
	 */
	public static int compare2dates(String str_date1, String str_date2, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			Date dt1 = df.parse(str_date1);
			Date dt2 = df.parse(str_date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return -100;
	}
	
	/**
	 * 比较两个时间相差多少天/小时/分钟
	 * @param date1
	 * @param date2
	 * @return hours = date2 - date1 或 date1 - date2
	 */
	public static long minus2dates(Date date1, Date date2) {
		
		long ltime1 = 0;
		if(null != date1) {
			ltime1 = date1.getTime();
		}
		
		long ltime2 = 0;
		if(null != date2) {
			ltime2 = date2.getTime();
		}
		
		long diff = 0;
		
		if(ltime2 >= ltime1) {
			diff = ltime2 - ltime1;
		} else {
			diff = ltime1 - ltime2;
		}
		
		long days = diff / (1000 * 60 * 60 * 24);
	    long hours = (diff - days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		
	    System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
	    
		return minutes;
	}
	
	
	/**
	 * 比较现在和date1之间相差多少分钟
	 * @param date1
	 * @return 
	 */
	public static long compare2now(Date date1) {
		long ltime1 = 0;
		if(null != date1) {
			ltime1 = date1.getTime();
		}
		long now = System.currentTimeMillis();
		long diff = 0;
		
		if(now >= ltime1) {
			System.out.println("+++");
			diff = now - ltime1;
		} else {
			System.out.println("---");
			diff = ltime1 - now;
		}
		
		long days = diff / (1000 * 60 * 60 * 24);
	    long hours = (diff - days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		
	    //System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
	    
	    return (days - 365) * 24 * 60 + hours * 60 + minutes;
	    
	}
	
	
	//for test
	public static void main(String[] args) {
		
		String date1 = "2018-12-03 19:00:35";
		String date2 = "2018-12-03 21:00:35";
		
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
		
		try {
			Date dt1 = df.parse(date1);
			//Date now = df.parse(date2);
			
			long mins = compare2now(dt1);
			
			System.out.println(mins);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
}
