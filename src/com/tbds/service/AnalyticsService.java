package com.tbds.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.model.eo.ErrorEvent;
import com.tbds.model.eo.ValueEvent;

public class AnalyticsService {

	private static final Logger log = Logger.getLogger(AnalyticsService.class);

	/**
	 *  @see 按列车号进行统计指定时间段发生故障频率
	 *  @return 统计结果Record列表
	 */
	public static List<Record> statisticTrainErrorGroupByTrainNumber(Date startDate, Date endDate) {
		List<Record> result = null; 
		
		if(startDate == null && endDate == null) {
			result = Db.find(
					"SELECT train, count(*) as errtimes FROM tbds_event_error group by train order by train asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT train, count(*) as errtimes FROM tbds_event_error where event_datetime between ? and ? group by train order by train asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT train, count(*) as errtimes FROM tbds_event_error where event_datetime <= ? group by train order by train asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT train, count(*) as errtimes FROM tbds_event_error where event_datetime >= ? group by train order by train asc", startDate);
			}
		}
		return result;
	}
	
	
	public static List<Record> statisticTrainErrorGroupByTrainOBCU(Date startDate, Date endDate) {
		List<Record> result = null;
		if(startDate == null && endDate == null) {
			result = Db.find("SELECT train, obcu, count(*) as errtimes FROM tbds_event_error group by train, obcu order by train asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT train, obcu, count(*) as errtimes FROM tbds_event_error where event_datetime between ? and ? group by train, obcu order by train asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT train, obcu, count(*) as errtimes FROM tbds_event_error where event_datetime <= ? group by train, obcu order by train asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT train, obcu, count(*) as errtimes FROM tbds_event_error where event_datetime >= ? group by train, obcu order by train asc", startDate);
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * @see 按日期进行统计所有列车发生故障的频率
	 * @param startDate
	 * @param beginDate
	 * @return 发生
	 */
	public static List<Record> statisticTrainErrorGroupByEventDate(Date startDate, Date endDate) {
		return Db.find("SELECT event_date, count(*) as errtimes FROM joc.tbds_event_error group by event_date order by event_date asc");
	}

	public static List<ErrorEvent> loadErrorEventData() {
		return ErrorEvent.dao.find("select * from tbds_event_error");
	}

	public static List<ValueEvent> loadValueEventData() {
		return ValueEvent.dao.find("select * from tbds_event_value");
	}

}
