package com.tbds.web;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.Constants;
import com.tbds.util.EncryptCookieUtils;
import com.tbds.util.StrUtil;

public class LoginerInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		Controller ctrler = ai.getController();
		
		String uid = EncryptCookieUtils.get(ctrler, Constants.COOKIE_UUUID);
        
		if (StrUtil.isBlank(uid)) {
			ctrler.redirect("/login");
            return;
        }

        User user = UserService.findById(Integer.parseInt(uid));
        if (user == null || !user.isActive()) {
        	ctrler.redirect("/login");
            return;
        }
        
        //此处可进行初始化菜单等公共资源
        
        ctrler.setAttr(Constants.LOGINER_USER, user);

        ai.invoke();
        
        
	}
	

}
