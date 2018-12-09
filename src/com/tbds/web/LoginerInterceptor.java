package com.tbds.web;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.Constants;
import com.tbds.util.EncryptCookieUtils;
import com.tbds.util.StrUtil;

public class LoginerInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		String uid = EncryptCookieUtils.get(ai.getController(), Constants.COOKIE_UUUID);
        
		if (StrUtil.isBlank(uid)) {
            ai.getController().redirect("/login");
            return;
        }

        User user = UserService.findById(Integer.parseInt(uid));
        if (user == null || !user.isActive()) {
            ai.getController().redirect("/login");
            return;
        }
        
        //此处可进行初始化菜单等公共资源
        
        ai.getController().setAttr(Constants.LOGINER_USER, user);

        ai.invoke();
        
        
	}
	

}
