/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.Constants;
import com.tbds.util.StrUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author totan
 */
public class LogonController extends Controller {
    private static final Logger log = Logger.getLogger(LogonController.class);
    
    public void index() {
        render("login.html");
    }
    
    public void quit() {
        setSessionAttr(Constants.LOGINER, null);
        redirect("/");
    }
    
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
        
        if(loginUser == null) {
        	setAttr("LoginFailed", "请输入用户名与密码错误！");
            renderText("0");
            return;
        }
        
        String salt = loginUser.getStr("salt");
        String saltPassword = loginUser.getStr("password");
        
        String inputPasssword = HashKit.sha256(password + salt);
        
        if(saltPassword.equalsIgnoreCase(inputPasssword)) {
        	
            System.out.println("****UserName = " + userName + " login successfully!");
            
        	String showName = loginUser.getStr("nickname");
            if(StrKit.isBlank(showName)) {
            	showName = userName;
            }
        	
            setSessionAttr(Constants.LOGINER, showName);
            renderText("1");
            return;
        } else {
        	System.out.println("****UserName = " + userName + " login failed!");
        	setAttr("LoginFailed", "请输入用户名与密码错误！");
            renderText("0");
            return;
        }
        
    }
    
    
}
