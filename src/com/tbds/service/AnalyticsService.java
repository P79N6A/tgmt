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
	 * @see 按列车号进行统计指定时间段发生故障频率
	 * @param startDate
	 * @param endDate
	 * @return 统计结果Record列表
	 */
	public static List<Record> statisticErrorByTrainNumber(Date startDate, Date endDate) {
		List<Record> result = null; 
		
		String selectSql = " SELECT train, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by train order by train asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, null, null, null, null, null);
		
		return result;
	}
	
	/**
	 *     按列车号Train、OBCU列车端进行分组统计所有错误数据
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByTrainNumAndOBCU(Date startDate, Date endDate, String train, String obcu) {
		List<Record> result = null;
		
		String selectSql = " SELECT train, obcu, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by train, obcu order by train asc ";
		
//		List<Object> paras = new  ArrayList<Object>();
//		
//		if(startDate != null) {
//			whereSql += " and event_datetime >= ? ";
//			paras.add(startDate);
//			
//		} 
//		
//		if(endDate != null) {
//			whereSql += " and event_datetime <= ? ";
//			paras.add(endDate);
//		}
//		
//		if(StrUtil.notBlank(train)) {
//			whereSql += " and train like concat('%', ?, '%')";
//			paras.add(train);
//		}
//		
//		if(StrUtil.notBlank(obcu)) {
//			whereSql += " and obcu like concat('%', ?, '%')";
//			paras.add(obcu);
//		}
//		
//		String sql = selectSql + whereSql + groupBySql;
//		if(null == paras || paras.isEmpty()) {
//			result = Db.find(sql);
//		} else {
//			if(paras.size() == 1) {
//				result = Db.find(sql, paras.get(0));
//			} else if(paras.size() == 2) {
//				result = Db.find(sql, paras.get(0), paras.get(1));
//			} else if(paras.size() == 3) {
//				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2));
//			} else if(paras.size() == 4) {
//				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3));
//			}
//			
//			//failed as belows: 
//			//result = Db.find(sql, paras);
//		}
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, null, null, null);
		
		
		return result;
	}
	
	/**
	 * 按OBCU分组统计
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByObcu(Date startDate, Date endDate, String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT obcu, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by obcu order by obcu asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
	}
	
	
	/**
	 * 按硬件模块进行分组统计
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByItem(Date startDate, Date endDate, String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT item, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by item order by item asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
		
	}
	
	
	/**
	 * 按硬件故障元素进行分组统计
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByElement(Date startDate, Date endDate, 
			String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT element, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by element order by element asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
		
	}
	
	
	/**
	 * 按硬件故障元素进行分组统计
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByErrType(Date startDate, Date endDate, String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT error_type as errType, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by error_type order by error_type asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
		
	}
	
	/**
	 *  按日期进行统计所有列车发生故障的频率
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return 查询结果List<Record>
	 */
	public static List<Record> statisticErrorByEventDate(Date startDate, Date endDate, String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT event_date as eventdate, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by event_date order by event_date asc ";
		
		result = internalQueryActionWithEventDate(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
	}
	
	private static List<Record> internalQueryActionWithEventDateTime(String selectSql, String whereSql, String groupBySql, Date startDate, Date endDate, 
			String train, String obcu, String item, String element, String errortype) {
		
		List<Record> result = null;
		
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
		
		if(StrUtil.notBlank(item)) {
			whereSql += " and item like concat('%', ?, '%')";
			paras.add(item);
		}
		
		if(StrUtil.notBlank(element)) {
			whereSql += " and element like concat('%', ?, '%')";
			paras.add(element);
		}
		
		if(StrUtil.notBlank(errortype)) {
			whereSql += " and error_type like concat('%', ?, '%')";
			paras.add(errortype);
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
			} else if(paras.size() == 5) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4));
			} else if(paras.size() == 6) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4), paras.get(5));
			} else if(paras.size() == 7) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4), paras.get(5), paras.get(6));
			}
		}
		
		return result;
	}
	
	private static List<Record> internalQueryActionWithEventDate(String selectSql, String whereSql, String groupBySql, Date startDate, Date endDate, 
			String train, String obcu, String item, String element, String errortype) {
		
		List<Record> result = null;
		
		List<Object> paras = new  ArrayList<Object>();
		
		if(startDate != null) {
			whereSql += " and event_date >= ? ";
			paras.add(startDate);
			
		} 
		
		if(endDate != null) {
			whereSql += " and event_date <= ? ";
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
		
		if(StrUtil.notBlank(item)) {
			whereSql += " and item like concat('%', ?, '%')";
			paras.add(item);
		}
		
		if(StrUtil.notBlank(element)) {
			whereSql += " and element like concat('%', ?, '%')";
			paras.add(element);
		}
		
		if(StrUtil.notBlank(errortype)) {
			whereSql += " and error_type like concat('%', ?, '%')";
			paras.add(errortype);
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
			} else if(paras.size() == 5) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4));
			} else if(paras.size() == 6) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4), paras.get(5));
			} else if(paras.size() == 7) {
				result = Db.find(sql, paras.get(0), paras.get(1), paras.get(2), paras.get(3), paras.get(4), paras.get(5), paras.get(6));
			}
		}
		
		return result;
	}
	

	public static List<ErrorEvent> loadErrorEventData() {
		return ErrorEvent.dao.find("select * from tbds_event_error");
	}

	public static List<ValueEvent> loadValueEventData() {
		return ValueEvent.dao.find("select * from tbds_event_value");
	}

}
