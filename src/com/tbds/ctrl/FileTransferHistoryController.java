/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.core.Controller;
import com.tbds.service.JobSchedulerService;

/**
 *
 * @author jq
 */
public class FileTransferHistoryController extends Controller  {
    
    public void index()
    {
        String startDate = getPara("startdate");
        String endDate = getPara("enddate");
        String type = getPara("type");
        setAttr("startDate",startDate);
        setAttr("endDate",endDate);
        setAttr("dataType",type);
        
        setAttr("files", JobSchedulerService.searchHistory(startDate,endDate,type,getParaToInt(0, 1), 5));
        render("index.html");
    }
        
    public void  search()
    {
        String startDate = getPara("startdate");
        String endDate = getPara("enddate");
        String type = getPara("type");
        setAttr("startDate",startDate);
        setAttr("endDate",endDate);
        setAttr("dataType",type);
        
        setAttr("files", JobSchedulerService.searchHistory(startDate,endDate,type,getParaToInt(0, 1), 5));
        render("index.html");
        
    }
}
