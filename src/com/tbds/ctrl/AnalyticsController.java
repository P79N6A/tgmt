package com.tbds.ctrl;

import java.util.Date;
import java.util.List;

import com.jfinal.json.JFinalJson;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;

import com.tbds.service.AnalyticsService;
import com.tbds.util.DateUtil;
import com.tbds.util.StrUtil;

public class AnalyticsController extends TbdsBaseController {
	
	String dateLongFormat = "yyyy-MM-dd HH:mm";
	String dateShortFormat = "yyyy-MM-dd";
	
	public void index() {
		render("index.html");
	}
	
	/*
	 * 列车故障概况统计
	 */
	public void faultsummary() {
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		String train = getPara("train");
		String obcu = getPara("obcu");
		
		Date startDate = DateUtil.str2date(sDate, dateLongFormat);
		Date endDate = DateUtil.str2date(eDate, dateLongFormat);
		
		//查询
		//List<Record> trainErrs = AnalyticsService.statisticTrainErrorGroupByTrainNumber(startDate, endDate);
		List<Record> obcuErrs = AnalyticsService.statisticErrorByTrainNumAndOBCU(startDate, endDate, train, obcu);
		
		//回传查询结果到页面
		//setAttr("trainErrs", JsonKit.toJson(trainErrs));
		setAttr("obcuErrs", JsonKit.toJson(obcuErrs));

		//设置页面参数值
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		
		render("fault/index.html");
	}
	
	/*
	 * 分类进行分析统计
	 */
	public void classify() {
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		String train = getPara("train");
		String obcu = getPara("obcu");
		
		String item = getPara("item");
		String element = getPara("element");
		String errortype = getPara("errortype");
		
		Date startDate = DateUtil.str2date(sDate, dateLongFormat);
		Date endDate = DateUtil.str2date(eDate, dateLongFormat);
		
		//查询
		List<Record> obcuErrs = AnalyticsService.statisticErrorByObcu(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> itemErrs = AnalyticsService.statisticErrorByItem(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> elementErrs = AnalyticsService.statisticErrorByElement(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> errTypeErrs = AnalyticsService.statisticErrorByErrType(startDate, endDate, train, obcu, item, element, errortype);
		
		
		//回串查询结果
		setAttr("obcuErrs", JsonKit.toJson(obcuErrs));
		setAttr("itemErrs", JsonKit.toJson(itemErrs));
		setAttr("elementErrs", JsonKit.toJson(elementErrs));
		setAttr("errTypeErrs", JsonKit.toJson(errTypeErrs));
		
		//设置页面参数值
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		setAttr("item", StrUtil.notBlank(item) ? item : null);
		setAttr("element", StrUtil.notBlank(element) ? element : null);
		setAttr("errortype", StrUtil.notBlank(errortype) ? errortype : null);
		
		render("fault/classify.html");
	}
	
	/*
	 * 故障频率分析统计
	 */
	public void frequency() {
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		String train = getPara("train");
		String obcu = getPara("obcu");
		
		String item = getPara("item");
		String element = getPara("element");
		String errortype = getPara("errortype");
		
		Date startDate = DateUtil.str2date(sDate, dateShortFormat);
		Date endDate = DateUtil.str2date(eDate, dateShortFormat);
		
		//查询
		List<Record> errsFrequency = AnalyticsService.statisticErrorByEventDate(startDate, endDate, train, obcu, item, element, errortype);
		
		//设置json字符串的时间格式
		//setTimestampPattern("yyyy-MM-dd")
		//JFinalJson.getJson().setTimestampPattern("yyyy-MM-dd").toJson(errsFrequency);
		String result = JFinalJson.getJson().setDatePattern(dateShortFormat).toJson(errsFrequency);
		
		//回传结果
		setAttr("errsFrequency", result);
		
		//设置页面参数值
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		setAttr("item", StrUtil.notBlank(item) ? item : null);
		setAttr("element", StrUtil.notBlank(element) ? element : null);
		setAttr("errortype", StrUtil.notBlank(errortype) ? errortype : null);
		
		render("fault/frequency.html");
	}
	
	/*
	 * 故障时间表分布统计 
	 * 
	 */
	public void faulttimeline() {
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		String train = getPara("train");
		String obcu = getPara("obcu");
		
		String item = getPara("item");
		String element = getPara("element");
		String errortype = getPara("errortype");
		
		Date startDate = DateUtil.str2date(sDate, dateLongFormat);
		Date endDate = DateUtil.str2date(eDate, dateLongFormat);
		
		//查询
		List<Record> hourErrs = AnalyticsService.statisticErrorByHour(startDate, endDate, train, obcu, item, element, errortype);
		
		//回传结果到页面
		setAttr("hourErrs", JsonKit.toJson(hourErrs));
		
		//设置页面参数值
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		setAttr("item", StrUtil.notBlank(item) ? item : null);
		setAttr("element", StrUtil.notBlank(element) ? element : null);
		setAttr("errortype", StrUtil.notBlank(errortype) ? errortype : null);
		
		render("fault/timeline.html");
	}
	
	/*
	 * 提供用户手工自主进行组合关联查询的功能
	 * 	-- 按计算处理模块cpm
	 *	-- 按列车号train
	 *	-- 按车载单元obcu
	 *	-- 按组件模块item
	 *	-- 按故障元素element
	 *	-- 按故障类型error_type
	 */
	public void faultunion() {
		//日期+时间
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		Date startDate = DateUtil.str2date(sDate, dateLongFormat);
		Date endDate = DateUtil.str2date(eDate, dateLongFormat);
		
		//分组参数
		String byCpm = getPara("cpm");
		String byTrain = getPara("train");
		String byObcu = getPara("obcu");
		String byItem = getPara("item");
		String byElement = getPara("element");
		String byErrorType = getPara("errortype");
		
		System.out.println("byCpm = " + byCpm + ", byTrain = " + byTrain 
				+ ", byObcu = " + byObcu + ", byItem = " + byItem 
				+ ", byElement = " + byElement + ", byErrorType = " + byErrorType);
		
		boolean groupByCpm = false, groupByTrain = false, groupByObcu = false, groupByItem = false, groupByElement = false, groupByErrorType = false;
		if("on".equals(byCpm)) {//cpm
			groupByCpm = true;
		}
		if("on".equals(byTrain)) {//train
			groupByTrain = true;
		}
		if("on".equals(byObcu)) {//obcu
			groupByObcu = true;
		}
		if("on".equals(byItem)) {//item
			groupByItem = true;
		}
		if("on".equals(byElement)) {//element
			groupByElement = true;
		}
		if("on".equals(byErrorType)) {//element
			groupByErrorType = true;
		}
		
		//查询
		List<Record> result = AnalyticsService.statisticErrorByManualChosenGroup(groupByCpm, groupByTrain, groupByObcu, groupByItem, 
														groupByElement, groupByErrorType, startDate, endDate);
		
		//回传结果到页面
		setAttr("errsList", result);
		setAttr("errs", JsonKit.toJson(result));
		
		//设置页面参数值
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);	
				
		setAttr("cpm", byCpm);
		setAttr("train", byTrain);
		setAttr("obcu", byObcu);
		setAttr("item", byItem);
		setAttr("element", byElement);
		setAttr("errortype", byErrorType);
			
		
		render("fault/union.html");
	}
	
	
	
	public void health() {
		render("health/index.html");
	}

	public void wheel() {
		render("wheel/index.html");
	}

	public void stop() {
		render("stop/index.html");
	}
	
	
	

}
