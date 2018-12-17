/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import java.util.Map;

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
    	/*统计当前列车的数量*/
    	Map<String, Integer> mpsStatusMap = new MpsService().statisticMpsStatus();
    	setAttr("mpsStatusMap", mpsStatusMap);
    	
    	/*统计当前在线的MPS数量*/
    	
    	/*统计当前离线的MPS数量*/
    	
    	
    	/*统计今天上报的列车故障*/
    	
    	
    	render("index.html");
    }
    
}
