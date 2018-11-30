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
        
        String encryptPasssword = HashKit.md5(password);
        String testPassword = HashKit.md5("123456");
        String testUser = "admin";
        System.out.println(testPassword);
        System.out.println(encryptPasssword);
        if(testPassword.equalsIgnoreCase(encryptPasssword) && testUser.equalsIgnoreCase(userName)) {
            setSessionAttr(Constants.LOGINER, userName);
            renderText("1");
            return;
        } else {
            renderText("0");
            return;
        }
        
    }
    
    
}
