package com.tbds.web;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.tbds.ctrl.HomeController;
import com.tbds.ctrl.UserProfileController;
import com.tbds.model.eo.User;
import com.tbds.service.PermissionService;
import com.tbds.util.Constants;
import com.tbds.util.RequestUtil;

/**
 * 
 * @author totan
 *
 */

public class PermissionInterceptor implements Interceptor {
	
	private static final String NO_PERMISSION_VIEW = "/pages/error/denied.html";
	
	@Override
	public void intercept(Invocation ai) {
		
		Controller ctrler = ai.getController();
		
		User user = (User)ctrler.getSession().getAttribute(Constants.LOGINER_USER);
		
		if (user == null) {
            render(ai);
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
		 * 
		 */
        if (!PermissionService.hasPermission(user.getInt("id"), ai.getActionKey())) {
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
