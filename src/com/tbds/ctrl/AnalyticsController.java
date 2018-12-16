package com.tbds.ctrl;

import java.util.Date;
import java.util.List;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.model.eo.ErrorEvent;
import com.tbds.service.AnalyticsService;
import com.tbds.util.DateUtil;
import com.tbds.util.StrUtil;

public class AnalyticsController extends TbdsBaseController {

	public void index() {
		render("index.html");
	}

	public void faultsummary() {
		String format = "yyyy-MM-dd HH:mm";
		
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		Date startDate = DateUtil.str2date(sDate, format);
		Date endDate = DateUtil.str2date(eDate, format);
		
		//List<Record> trainErrs = AnalyticsService.statisticTrainErrorGroupByTrainNumber(startDate, endDate);
		List<Record> obcuErrs = AnalyticsService.statisticTrainErrorGroupByTrainOBCU(startDate, endDate);
		
		//setAttr("trainErrs", JsonKit.toJson(trainErrs));
		setAttr("obcuErrs", JsonKit.toJson(obcuErrs));
		
		setAttr("startdate", StrUtil.notBlank(sDate) ? sDate : null);
		setAttr("enddate", StrUtil.notBlank(eDate) ? eDate : null);
		
		render("fault/index.html");
	}
	
	public void faulttimeline() {
		Date startDate = getParaToDate("startdate");
		Date endDate = getParaToDate("enddate");
		
		List<Record> trainErrs = AnalyticsService.statisticTrainErrorGroupByTrainNumber(startDate, endDate);
		
		
		setAttr("result", JsonKit.toJson(trainErrs));
		
		setAttr("startdate", startDate != null ? DateUtil.date2str(startDate) : null);
		setAttr("enddate", endDate != null ? DateUtil.date2str(endDate) : null);
		
		render("fault/timeline.html");
	}
	
	
	public void classify() {
		String format = "yyyy-MM-dd HH:mm";
		
		String sDate = getPara("startdate");
		String eDate = getPara("enddate");
		
		Date startDate = DateUtil.str2date(sDate, format);
		Date endDate = DateUtil.str2date(eDate, format);
		
		List<Record> trainErrs = AnalyticsService.statisticTrainErrorGroupByTrainNumber(startDate, endDate);
		List<Record> itemErrs = AnalyticsService.statisticTrainErrorGroupByItem(startDate, endDate);
		List<Record> elementErrs = AnalyticsService.statisticTrainErrorGroupByElement(startDate, endDate);
		List<Record> errTypeErrs = AnalyticsService.statisticTrainErrorGroupByErrType(startDate, endDate);
		
		
		setAttr("trainNumErrs", JsonKit.toJson(trainErrs));
		setAttr("itemErrs", JsonKit.toJson(itemErrs));
		setAttr("elementErrs", JsonKit.toJson(elementErrs));
		setAttr("errTypeErrs", JsonKit.toJson(errTypeErrs));
		
		render("fault/classify.html");
	}
	

	public void wheel() {
		render("wheel/index.html");
	}

	public void stop() {
		render("stop/index.html");
	}
	
	public void frequency() {
		List<ErrorEvent> errEvtList = AnalyticsService.loadErrorEventData();
		
		setAttr("errorEventList", errEvtList);
		
		render("frequency/index.html");
	}
	

}
