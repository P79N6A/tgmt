/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import org.apache.log4j.Logger;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.tbds.model.eo.Mps;
import com.tbds.service.MpsService;

/**
 *
 * @author totan
 */
public class MpsController extends Controller {
	
	private static final Logger log = Logger.getLogger(MpsController.class);

    MpsService service = new MpsService();
    

    public void index() {
        setAttr("mpsPage", service.paginate(getParaToInt(0, 1), 10));
        render("index.html");
    }

    public void status() {
        setAttr("mpsPage", service.getMpsListStatus());
        render("status.html");

    }

    public void edit() {
        setAttr("mps", service.findById(getParaToInt()));
        render("edit.html");
    }
    
    public void add() {
        render("add.html");
    }
    
    public void save() {
    	Mps mps = getModel(Mps.class, "mps");
    	
    	Integer id = mps.getInt("id");
        String trainType = mps.get("train_type");
        String trainNum = mps.get("train_num");
        String abMarker = mps.get("ab_marker");
        String hostIP = mps.get("host_ip");
        String hostPort = mps.get("host_port");
    	
    	//TODO: 判断是否存在重复的IP地址与端口
    	
    	mps.set("fullname", trainType + "_" + trainNum);
        mps.set("desc", trainType + "_" + trainNum + abMarker);
        mps.set("status", 1);
        
        PropKit.use(com.tbds.util.Constants.CONFIG_FILE);
        String statusPath = PropKit.get(com.tbds.util.Constants.MPS_STATUS_LOG_PATH);
        mps.set("client_state_file", statusPath);
        mps.set("client_state_log", statusPath);
    	
    	boolean flag = mps.save();
    	if(flag) {
            redirect("/mps");
        } else {
            System.err.println("save failed in MpsController");
            this.renderError(500);
        }
    }
    
    public void update() {
        Mps mps = getModel(Mps.class, "mps");
        
        Integer id = mps.getInt("id");
        String trainType = mps.get("train_type");
        String trainNum = mps.get("train_num");
        String abMarker = mps.get("ab_marker");
        String hostIP = mps.get("host_ip");
        String hostPort = mps.get("host_port");
        
        //TODO: 判断是否存在重复的IP地址与端口，方便排除问题
        mps.set("fullname", trainType + "_" + trainNum);
        mps.set("desc", trainType + "_" + trainNum + abMarker);
        mps.set("status", 1);
        
        boolean flag = mps.update();
        
        if(flag) {
            redirect("/mps");
        } else {
            System.err.println("update failed in MpsController");
            this.renderError(500);
        }
        
    }
    
    public void delete() {
        //直接删除掉
    	Integer id = getParaToInt();
    	System.out.println("id = " + id);
    	boolean flag = service.deleteById(id);
    	
		if(flag) {
			renderText("1");//del success
		} else {
			renderText("0");//del failed
			
		}
    }
    
    public void disable() {
        
    }
    
    public void enable() {
        
    }
    

}
