/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import java.util.List;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.service.AnalyticsService;
import com.tbds.service.MpsService;

/**
 *
 * @author totan
 */
public class HomeController extends TbdsBaseController {
    public void index() {
    	callme();
    }
    public void home() {
    	callme();
    }
    
    private void callme() {
    	/*统计当天日志已入库的列车数量*/
    	long logTrainCounts = AnalyticsService.todayPersistTrainCount();
    	setAttr("logTrainCounts", logTrainCounts);
    	
    	/*统计当天日志已入库的统计数量*/
    	long logErrCounts = AnalyticsService.todayPersistErrLogCount();
    	setAttr("logErrCounts", logErrCounts);
    	
    	
    	/*
    	 * 	统计当前在线的MPS数量
    	 *	统计当前离线的MPS数量
    	*/
    	Map<String, Integer> mpsStatusMap = new MpsService().statisticMpsStatus();
    	setAttr("mpsStatusMap", mpsStatusMap);
    	
    	/**
    	 * 对每个列车的故障组件进行计数
    	 */
    	setAttr("trainErrCatalog", AnalyticsService.statisticByTrainComponentCatalog());
    	
    	
    	/*统计今天上报的列车故障Top5*/
    	setAttr("todayTrainErrs", AnalyticsService.todayTop5TrainErrs());
    	
    	///*统计本周上报的列车故障Top5*/
    	setAttr("currentWeekTrainErrs", AnalyticsService.currentWeekTop5TrainErrs());
    	
    	///*统计上周上报的列车故障Top5*/
    	setAttr("lastWeekTrainErrs", AnalyticsService.lastWeekTop5TrainErrs());
    	
    	///*统计本月上报的列车故障Top5*/
    	setAttr("currentMonthTrainErrs", AnalyticsService.currentMonthTop5TrainErrs());
    	
    	///*统计上月上报的列车故障Top5*/
    	setAttr("lastMonthTrainErrs", AnalyticsService.lastMonthTop5TrainErrs());
    	
    	/*列车故障概览图*/
    	List<Record> trainErrs = AnalyticsService.statisticErrorByTrainNumber(null, null);
    	setAttr("trainNumErrs", JsonKit.toJson(trainErrs));
    	
    	render("index.html");
    }
    
}
