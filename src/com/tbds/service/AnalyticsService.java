package com.tbds.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.model.eo.ErrorEvent;
import com.tbds.model.eo.ValueEvent;
import com.tbds.util.StrUtil;

public class AnalyticsService {

	private static final Logger log = Logger.getLogger(AnalyticsService.class);
	
	/**
	 * @see 按列车号进行统计近两个月发生故障频率
	 * @return 统计结果Record列表
	 */
	public static List<Record> statisticCurrent2MonthErrorByTrainNumber() {
		List<Record> result = null; 
		
		String selectSql = " SELECT train, count(*) as errtimes FROM tbds_event_error ";
		String whereSql = " where event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE()";
		String groupBySql = " group by train order by train asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, null, null, null, null, null, null, null);
		
		return result;
	}
	
	
	/**
	 * 统计近俩个月（计算处理模块）的故障种类计数以及对应列车
	 */
	public static List<Record> statisticCurrent2MonthCatalogErrorOnCpm() {
		List<Record> result = null; 
		String sql = "select count(*) as errtimes, cpm as catalog, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE() group by train, cpm order by train asc, cpm asc";
		result = Db.find(sql);
		return result;
	}
	
	/**
	 * 统计近俩个月（车载单元）的故障种类计数以及对应列车
	 */
	public static List<Record> statisticCurrent2MonthCatalogErrorOnObcu() {
		List<Record> result = null; 
		String sql = "select count(*) as errtimes, obcu as catalog, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE() group by train, obcu order by train asc, obcu asc";
		result = Db.find(sql);
		return result;
	}
	
	/**
	 * 统计近俩个月（组件）的故障种类计数以及对应列车
	 */
	public static List<Record> statisticCurrent2MonthCatalogErrorOnItem() {
		List<Record> result = null; 
		String sql = "select count(*) as errtimes, item as catalog, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE() group by train, item order by train asc, item asc";
		result = Db.find(sql);
		return result;
	}

	
	/**
	 * 统计近俩个月（元素）的故障种类计数以及对应列车
	 */
	public static List<Record> statisticCurrent2MonthCatalogErrorOnElement() {
		List<Record> result = null; 
		String sql = "select count(*) as errtimes, element as catalog, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE() group by train, element order by train asc, element asc";
		result = Db.find(sql);
		return result;
	}
	
	/**
	 * 统计近俩个月（故障信息分类）的故障种类计数以及对应列车
	 */
	public static List<Record> statisticCurrent2MonthCatalogErrorOnErrorType() {
		List<Record> result = null; 
		String sql = "select count(*) as errtimes, error_type as catalog, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE() group by train, error_type order by train asc, error_type asc";
		result = Db.find(sql);
		return result;
	}
	
	
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
	
	/**
	 * 根据故障发生的所属小时区域内进行统计，便于发现故障发现哪些时间段上
	 * @param startDate
	 * @param endDate
	 * @param train
	 * @param obcu
	 * @param item
	 * @param element
	 * @param errortype
	 * @return
	 */
	public static List<Record> statisticErrorByHour(Date startDate, Date endDate, String train, String obcu, String item, String element, String errortype) {
		List<Record> result = null;
		
		String selectSql = " SELECT count(*) as errtimes, HOUR(event_time) as h FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = " group by h order by h asc ";
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, train, obcu, item, element, errortype);
		
		return result;
	}
	
	
	/**
	 * 选择性地进行group分组统计
	 * @param groupByCpm
	 * @param groupByTrain
	 * @param groupByObcu
	 * @param groupByItem
	 * @param groupByElement
	 * @param groupByErrorType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Record> statisticErrorByManualChosenGroup(boolean groupByCpm, boolean groupByTrain, 
																boolean groupByObcu, boolean groupByItem, 
																boolean groupByElement, boolean groupByErrorType,
																Date startDate, Date endDate) {
		List<Record> result = null;
		List<Object> paras = new  ArrayList<Object>();
		
		String selectSql = "";
		String fromSql = " FROM tbds_event_error ";
		String whereSql = " where 1=1 ";
		String groupBySql = "";
		String orderBySql = "";
		
		if(groupByCpm || groupByTrain || groupByObcu || groupByItem || groupByElement || groupByErrorType) {
			selectSql = "SELECT count(*) as errtimes,";
			groupBySql = " group by ";
			orderBySql = " order by ";
			
			if(groupByCpm) {
				selectSql += "cpm,";
				groupBySql += "cpm,";
				orderBySql += "cpm asc,";
			}
			
			if(groupByTrain) {
				selectSql += "train,";
				groupBySql += "train,";
				orderBySql += "train asc,";
			}
			
			if(groupByObcu) {
				selectSql += "obcu,";
				groupBySql += "obcu,";
				orderBySql += "obcu asc,";
			}
			
			if(groupByItem) {
				selectSql += "item,";
				groupBySql += "item,";
				orderBySql += "item asc,";
			}
			
			if(groupByElement) {
				selectSql += "element,";
				groupBySql += "element,";
				orderBySql += "element asc,";
			}
			
			if(groupByErrorType) {
				selectSql += "error_type,";
				groupBySql += "error_type,";
				orderBySql += "error_type asc,";
			}
			
			if(selectSql.trim().endsWith(",")) {
				selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			}
			
			if(groupBySql.trim().endsWith(",")) {
				groupBySql = groupBySql.substring(0, groupBySql.lastIndexOf(","));
			}
			
			if(orderBySql.trim().endsWith(",")) {
				orderBySql = orderBySql.substring(0, orderBySql.lastIndexOf(","));
			}
			
		} else {//若没有进行分组计算，则返回所有记录统计总数?
			selectSql = " SELECT count(*) as errtimes ";
		}
		
		selectSql = selectSql + fromSql;
		groupBySql = groupBySql + orderBySql;
		
		//System.out.println(selectSql);
		//System.out.println(whereSql);
		//System.out.println(groupBySql);
		
		result = internalQueryActionWithEventDateTime(selectSql, whereSql, groupBySql, startDate, endDate, null, null, null, null, null);
		
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
	
	/**
	 * 统计当天有多少班次列车的日志已入库
	 * 
	 */
	public static long todayPersistTrainCount() {
		long result = 0;
		
		Record resultRecord = Db.findFirst("SELECT count(distinct(train)) as cnts FROM tbds_event_error WHERE event_date = curdate()");
		if(resultRecord != null) {
			result = resultRecord.getLong("cnts");
		}
		return result;
	}
	
	/**
	 * 统计当天有多少日志已入库
	 * 
	 */
	public static long todayPersistErrLogCount() {
		long result = 0;
		
		Record resultRecord = Db.findFirst("SELECT count(*) as errtimes FROM tbds_event_error WHERE event_date = curdate()");
		if(resultRecord != null) {
			result = resultRecord.getLong("errtimes");
		}
		return result;
	}
	
	
	/**
	 * 统计当天发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> todayTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE event_date = curdate() group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计本周发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> currentWeekTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE YEARWEEK(date_format(event_date, '%Y-%m-%d')) = YEARWEEK(now()) group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计上一周发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> lastWeekTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE YEARWEEK(date_format(event_date, '%Y-%m-%d')) = YEARWEEK(now())-1 group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计本月发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> currentMonthTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE date_format(event_date,'%Y-%m')=date_format(now(),'%Y-%m') group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计上一个月发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> lastMonthTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE date_format(event_date,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m') group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计6个月发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> recent6MonthsTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 5 MONTH) and CURDATE() group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 统计近一年发生故障数排名前五的列车
	 * 
	 */
	public static List<Record> recentOneYearTop5TrainErrs() {
		List<Record> result = null;
		
		result = Db.find("SELECT count(*) as errtimes, train FROM tbds_event_error WHERE event_datetime between DATE_SUB(CURDATE(), INTERVAL 11 MONTH) and CURDATE() group by train order by errtimes desc limit 0,5");
		
		return result;
	}
	
	/**
	 * 计算每个列车近两个月有多少个部件发生了故障
	 * 
	 */
	public static List<Record> statisticByTrainComponentCatalog() {
		List<Record> result = null;
		
		//select train, count(distinct(cpm)) as cpm_cnts, count(distinct(obcu)) as obcu_cnts, count(distinct(item)) as item_cnts,count(distinct(element)) as element_cnts, count(distinct(error_type)) as errortype_cnts from tbds_event_error group by train order by train asc
		result = Db.find("select train, count(distinct(cpm)) as cpm_cnts, count(distinct(obcu)) as obcu_cnts, count(distinct(item)) as item_cnts,count(distinct(element)) as element_cnts, count(distinct(error_type)) as errortype_cnts "
				+ " from tbds_event_error "
				//+ " where 1=1 and (date_format(event_date,'%Y-%m')=date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m') or date_format(event_date,'%Y-%m')=date_format(now(),'%Y-%m'))"
				+ " where event_datetime between DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and CURDATE()"
				+ " group by train order by train asc");
		
		return result;
	}
	

	public static List<ErrorEvent> loadErrorEventData() {
		return ErrorEvent.dao.find("select * from tbds_event_error");
	}

	public static List<ValueEvent> loadValueEventData() {
		return ValueEvent.dao.find("select * from tbds_event_value");
	}

}
