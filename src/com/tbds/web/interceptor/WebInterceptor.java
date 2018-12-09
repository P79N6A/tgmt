/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.web.interceptor;


import org.apache.log4j.Logger;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.tbds.ctrl.LogonController;
import com.tbds.util.Constants;

/**
 *
 * @author totan
 * @deprecated Replaced by other interceptors
 */
public class WebInterceptor implements Interceptor {

    private final static Logger log = Logger.getLogger(WebInterceptor.class);

    public void intercept(Invocation ai) {
        System.out.println("before GlobalActionInterceptor");
        Controller ctrl = ai.getController();
        
        String actionKey = ai.getActionKey();
        String action = ai.getMethodName();
        
        System.out.println("**************Current ActionKey: " + actionKey);
        
        Object loginer = ctrl.getSession().getAttribute(Constants.LOGINER);
        
        if(null != loginer && !"".equals((String)loginer)) {
            System.out.println(">>>loginer = " + loginer);
            System.out.println("User access login action.");
            
            if(ctrl instanceof LogonController && !"quit".equalsIgnoreCase(action)) {//若用户直接访问登录
                ctrl.redirect("/");//用户不做退出时，直接访问登陆页直接跳转至首页
            } else {
                ai.invoke();
            }
        } else if(ctrl instanceof LogonController) {
            ai.invoke();
        } else {
            System.out.println("Visit " + actionKey + " refused, Need login first!");
            log.info("Visit " + actionKey + " refused, Need login first!");
            ctrl.redirect("/login");//跳转至登录页
        }
        System.out.println("after GlobalActionInterceptor");
    }
}
