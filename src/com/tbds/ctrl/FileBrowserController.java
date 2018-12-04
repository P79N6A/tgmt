/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import java.io.File;

import com.jfinal.core.Controller;
import com.tbds.service.JobSchedulerService;

/**
 *
 * @author JQIAO
 */
public class FileBrowserController  extends Controller {
    
   public void index()
    {
       String startDate = getPara("startdate");
       String endDate = getPara("enddate");
       String type = getPara("type");
       String trainNo = getPara("trainno");
       setAttr("startDate",startDate);
       setAttr("endDate",endDate);
       setAttr("dataType",type);
       setAttr("trainNo",trainNo);
       setAttr("files", JobSchedulerService.searchFiles(startDate,endDate,type,trainNo,getParaToInt(0, 1), 5));
       render("index.html");
 
    }
        
    public void  search()
    {
        String startDate = getPara("startdate");
        String endDate = getPara("enddate");
        String type = getPara("type");
        String trainNo = getPara("trainno");
        setAttr("startDate",startDate);
        setAttr("endDate",endDate);
        setAttr("dataType",type);
        setAttr("trainNo",trainNo);
        
        setAttr("files", JobSchedulerService.searchFiles(startDate,endDate,type,trainNo,getParaToInt(0, 1), 5));
        render("index.html");
    
    }
    
    public void download()
    {
    	System.out.println("****************come here**************");
    	String fileName = getPara("fn");
    	
    	String date = fileName.substring(9,17);
    	
    	String path = date + "/" + fileName;
    	
    	String basePath = "/home/oracle/data/inventory/";
    	
    	String fullPath = basePath + path;
    	
    	System.out.println("full path is: " + fullPath);
    	
    	renderFile(new File(fullPath));
    	//renderFile(fullPath);
    }
}
 