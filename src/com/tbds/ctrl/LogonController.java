/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.aop.Clear;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;

import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.Constants;
import com.tbds.util.DateUtil;
import com.tbds.util.EncryptCookieUtil;
import com.tbds.util.StrUtil;
import com.tbds.web.interceptor.PermissionInterceptor;

import org.apache.log4j.Logger;

/**
 *
 * @author totan
 */
public class LogonController extends TbdsBaseController {
    private static final Logger log = Logger.getLogger(LogonController.class);
    
    @Clear
    public void index() {
        render("login.html");
    }
    
    @Clear(PermissionInterceptor.class)
    public void quit() {
    	EncryptCookieUtil.remove(this, Constants.COOKIE_UUUID);
        setSessionAttr(Constants.LOGINER, null);
        setSessionAttr(Constants.LOGINER_UNI_ID, null);
        setSessionAttr(Constants.LOGINER_UNI_NAME, null);
        setSessionAttr(Constants.LOGINER_USER, null);
        redirect("/");
    }
    
    @Clear
    public void forgetPassword() {
        render("forgetPassword.html");
    }
   
    
    @Clear
    public void land() {
        log.info(">>>User Login:");
        String userName = getPara("loginName");
        String password = getPara("loginPswd");
        
        log.info("user = " + userName + ", password = ******* ");
        
        if(StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            setAttr("LoginFailed", "请输入用户名与密码！");
            renderText("0");
            return;
        }
        
        
        User loginUser = UserService.findByLoginUserName(userName.trim());
        
        if(loginUser == null || !loginUser.isActive()) {
        	setAttr("LoginFailed", "请输入用户名与密码错误！");
            renderText("0");
            return;
        }
        
        String salt = loginUser.getStr("salt");
        String saltPassword = loginUser.getStr("password");
        java.util.Date logged = loginUser.getDate("logged");
        int loginUniID = loginUser.getInt("id");
        
        String inputPasssword = HashKit.sha256(password + salt);
        
        if(saltPassword.equals(inputPasssword)) {
        	
            log.info("****UserName = " + userName + " login successfully!");
            
            java.util.Date now = new java.util.Date();
            log.info("last login time: " + DateUtil.date2str(logged));
            log.info("now login time: " + DateUtil.date2str(now));
                        
            long duration = DateUtil.compare2now(logged);
            log.info("Calc duration minutes: " + duration);
            
            
            //若在间隔一个小时多进行登录，则需要记录用户的登录时间
            if(logged == null || duration >= 60) {
            	loginUser.set("logged", now);
            	boolean flag = loginUser.update();
            	if(flag) {
            		log.info("Update login user logged time successfully.");
            	}
            }
            
        	String showName = loginUser.getStr("nickname");
            if(StrKit.isBlank(showName)) {
            	showName = userName;
            }
        	
            setSessionAttr(Constants.LOGINER, showName);//设置显示名
            setSessionAttr(Constants.LOGINER_UNI_ID, loginUniID);//设置唯一ID
            setSessionAttr(Constants.LOGINER_UNI_NAME, userName);//设置唯一用户名
            setSessionAttr(Constants.LOGINER_USER, loginUser);//设置对象到session
            
            EncryptCookieUtil.put(this, Constants.COOKIE_UUUID, loginUniID);
            
            renderText("1");
            return;
        } else {
        	System.out.println("****UserName = " + userName + " login failed!");
        	setAttr("LoginFailed", "请输入用户名与密码！");
            renderText("0");
            return;
        }
        
    }
    
    
}
