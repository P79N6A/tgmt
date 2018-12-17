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

	public void faultsummary() {
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		String train = getPara("train");
		String obcu = getPara("obcu");
		
		Date startDate = DateUtil.str2date(sDate, dateLongFormat);
		Date endDate = DateUtil.str2date(eDate, dateLongFormat);
		
		//List<Record> trainErrs = AnalyticsService.statisticTrainErrorGroupByTrainNumber(startDate, endDate);
		List<Record> obcuErrs = AnalyticsService.statisticErrorByTrainNumAndOBCU(startDate, endDate, train, obcu);
		
		//setAttr("trainErrs", JsonKit.toJson(trainErrs));
		setAttr("obcuErrs", JsonKit.toJson(obcuErrs));

		
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		
		render("fault/index.html");
	}
	
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
		
		List<Record> obcuErrs = AnalyticsService.statisticErrorByObcu(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> itemErrs = AnalyticsService.statisticErrorByItem(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> elementErrs = AnalyticsService.statisticErrorByElement(startDate, endDate, train, obcu, item, element, errortype);
		List<Record> errTypeErrs = AnalyticsService.statisticErrorByErrType(startDate, endDate, train, obcu, item, element, errortype);
		
		
		setAttr("obcuErrs", JsonKit.toJson(obcuErrs));
		setAttr("itemErrs", JsonKit.toJson(itemErrs));
		setAttr("elementErrs", JsonKit.toJson(elementErrs));
		setAttr("errTypeErrs", JsonKit.toJson(errTypeErrs));
		
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		setAttr("item", StrUtil.notBlank(item) ? item : null);
		setAttr("element", StrUtil.notBlank(element) ? element : null);
		setAttr("errortype", StrUtil.notBlank(errortype) ? errortype : null);
		
		render("fault/classify.html");
	}
	
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
		
		List<Record> errsFrequency = AnalyticsService.statisticErrorByEventDate(startDate, endDate, train, obcu, item, element, errortype);
		
		//setTimestampPattern("yyyy-MM-dd")
		//JFinalJson.getJson().setTimestampPattern("yyyy-MM-dd").toJson(errsFrequency);
		String result = JFinalJson.getJson().setDatePattern(dateShortFormat).toJson(errsFrequency);
		
		setAttr("errsFrequency", result);
		
		System.out.println(result);
		
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);		
		
		setAttr("train", StrUtil.notBlank(train) ? train : null);
		setAttr("obcu", StrUtil.notBlank(obcu) ? obcu : null);
		setAttr("item", StrUtil.notBlank(item) ? item : null);
		setAttr("element", StrUtil.notBlank(element) ? element : null);
		setAttr("errortype", StrUtil.notBlank(errortype) ? errortype : null);
		
		render("fault/frequency.html");
	}
	
	public void faulttimeline() {
		Date startDate = getParaToDate("startdate");
		Date endDate = getParaToDate("enddate");
		
		List<Record> trainErrs = AnalyticsService.statisticErrorByTrainNumber(startDate, endDate);
		
		
		setAttr("result", JsonKit.toJson(trainErrs));
		
		setAttr("startdate", startDate != null ? DateUtil.date2str(startDate) : null);
		setAttr("enddate", endDate != null ? DateUtil.date2str(endDate) : null);
		
		render("fault/timeline.html");
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
