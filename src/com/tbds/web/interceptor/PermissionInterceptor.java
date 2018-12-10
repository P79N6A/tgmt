package com.tbds.web.interceptor;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.tbds.ctrl.HomeController;
import com.tbds.ctrl.UserProfileController;
import com.tbds.model.eo.User;
import com.tbds.service.PermissionService;
import com.tbds.util.Constants;
import com.tbds.util.EncryptCookieUtil;
import com.tbds.util.RequestUtil;

/**
 * 
 * @author totan
 *
 */

public class PermissionInterceptor implements Interceptor {
	
	private static final Logger log = Logger.getLogger(PermissionInterceptor.class);
	
	private static final String NO_PERMISSION_VIEW = "/WEB-INF/pages/error/denied.html";
	
	@Override
	public void intercept(Invocation ai) {
		
		Controller ctrler = ai.getController();
		
		User user = (User)ctrler.getSession().getAttribute(Constants.LOGINER_USER);
		
		if (user == null) {
			System.err.println(">>>>Current User Is Timeout, You will logout and redirect to login page.");
			
			//Session Timeout时，检测用户已不存在，移除已存在Cookie，并跳转至登录页
			EncryptCookieUtil.remove(ctrler, Constants.COOKIE_UUUID);
			
			//render(ai);
			//跳转至登录页
			ctrler.redirect("/login");
			
            return;
        }
		
		/**
		 * 首页和个人概要信息不设置用户的权限控制（首页+个人信息修改——任何已登录用户都有权限进行访问）
		 */
		if(ctrler instanceof HomeController || ctrler instanceof UserProfileController ) {
			ai.invoke();
			return;
		}
		
		/**
		 * 判断当前用户是否拥有权限访问此资源
		 */
		int userId = user.getInt("id");
		
		log.info("Current User Id is: " + userId);
		System.out.println(">>>>Current User Id is: " + userId);
		
        if (!PermissionService.hasPermission(userId, ai.getActionKey())) {
            render(ai);
            return;
        }

        ai.invoke();

	}
	
	public void render(Invocation ai) {
        if (RequestUtil.isAjaxRequest(ai.getController().getRequest())) {
            ai.getController().renderJson(Ret.fail().set("msg", "您没有权限操作此功能。"));
        } else {
            ai.getController().render(NO_PERMISSION_VIEW);
        }
    }
	
	
	

}
