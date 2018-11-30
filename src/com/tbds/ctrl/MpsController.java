/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.tbds.model.eo.Mps;
import com.tbds.service.MpsService;

/**
 *
 * @author totan
 */
public class MpsController extends Controller {

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
        
    }
    
    public void update() {
        Mps mps = getModel(Mps.class, "mps");
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
    }
    
    public void disable() {
        
    }
    
    public void enable() {
        
    }
    

}
