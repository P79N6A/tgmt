package com.tbds.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.model.eo.ErrorEvent;
import com.tbds.model.eo.ValueEvent;
import com.tbds.util.StrUtil;

public class AnalyticsService {

	private static final Logger log = Logger.getLogger(AnalyticsService.class);
	
	/**
	 * 按列车号、OBCU列车端进行分组统计所有错误数据
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticTrainErrorGroupByTrainOBCU(Date startDate, Date endDate, String train, String obcu) {
		List<Record> result = null;
		
		String selectSql = " SELECT train, obcu, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by train, obcu order by train asc ";
		
		List<Object> paras = new  ArrayList<Object>();
		
		if(startDate != null) {
			whereSql += " and event_datetime >= ? ";
			paras.add(startDate);
			
		} 
		
		if(endDate != null) {
			whereSql += " and event_datetime <= ? ";
			paras.add(endDate);
		}
		
		if(StrUtil.notBlank(train)) {
			whereSql += " and train like concat('%', ?, '%')";
			paras.add(train);
		}
		
		if(StrUtil.notBlank(obcu)) {
			whereSql += " and obcu like concat('%', ?, '%')";
			paras.add(obcu);
		}
		
		String sql = selectSql + whereSql + groupBySql;
		if(null == paras || paras.isEmpty()) {
			result = Db.find(sql);
		} else {
			if(paras.size() == 1) {
				result = Db.find(sql, paras.get(0));
			} else if(paras.size() == 2) {
				result = Db.find(sql, paras.get(0), paras.get(1));
			} else if(paras.size() == 3) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2));
			} else if(paras.size() == 4) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3));
			}
			
			//failed as belows: 
			//result = Db.find(sql, paras);
		}
		
		
		return result;
	}

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
	
	/**
	 * 按OBCU分组统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticTrainErrorGroupByOBCU(Date startDate, Date endDate) {
		List<Record> result = null;
		if(startDate == null && endDate == null) {
			result = Db.find("SELECT obcu, count(*) as errtimes FROM tbds_event_error group by obcu order by obcu asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT obcu, count(*) as errtimes FROM tbds_event_error where event_datetime between ? and ? group by obcu order by obcu asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT obcu, count(*) as errtimes FROM tbds_event_error where event_datetime <= ? group by obcu order by obcu asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT obcu, count(*) as errtimes FROM tbds_event_error where event_datetime >= ? group by obcu order by obcu asc", startDate);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 按硬件模块进行分组统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticTrainErrorGroupByItem(Date startDate, Date endDate) {
		List<Record> result = null;
		
		if(startDate == null && endDate == null) {
			result = Db.find("SELECT count(*) as errtimes, item FROM tbds_event_error group by item order by item asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, item FROM tbds_event_error where event_datetime between ? and ? group by item order by item asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, item FROM tbds_event_error where event_datetime <= ? group by item order by item asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT count(*) as errtimes, item FROM tbds_event_error where event_datetime >= ? group by item order by item asc", startDate);
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * 按硬件故障元素进行分组统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticTrainErrorGroupByElement(Date startDate, Date endDate) {
		List<Record> result = null;
		
		if(startDate == null && endDate == null) {
			result = Db.find("SELECT count(*) as errtimes, element FROM tbds_event_error group by element order by element asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, element FROM tbds_event_error where event_datetime between ? and ? group by element order by element asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, element FROM tbds_event_error where event_datetime <= ? group by element order by element asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT count(*) as errtimes, element FROM tbds_event_error where event_datetime >= ? group by element order by element asc", startDate);
			}
		}
		
		return result;
		
	}
	
	
	/**
	 * 按硬件故障元素进行分组统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticTrainErrorGroupByErrType(Date startDate, Date endDate) {
		List<Record> result = null;
		
		if(startDate == null && endDate == null) {
			result = Db.find("SELECT count(*) as errtimes, error_type as errType FROM tbds_event_error group by error_type order by error_type asc");
		} else {
			if(startDate != null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, error_type as errType FROM tbds_event_error where event_datetime between ? and ? group by error_type order by error_type asc", startDate, endDate);
			} else if(startDate == null && endDate != null) {
				result = Db.find("SELECT count(*) as errtimes, error_type as errType FROM tbds_event_error where event_datetime <= ? group by error_type order by error_type asc", endDate);
			} else if(startDate != null && endDate == null) {
				result = Db.find("SELECT count(*) as errtimes, error_type as errType FROM tbds_event_error where event_datetime >= ? group by error_type order by error_type asc", startDate);
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
