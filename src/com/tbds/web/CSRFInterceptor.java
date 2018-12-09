package com.tbds.web;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.render.TextRender;
import com.tbds.ctrl.HomeController;
import com.tbds.ctrl.LogonController;
import com.tbds.util.RequestUtil;
import com.tbds.util.StrUtil;
import javax.servlet.http.HttpServletRequest;

public class CSRFInterceptor implements Interceptor {
	
	private static final String CSRF_KEY = "tbds_token";
	
	@Override
	public void intercept(Invocation ai) {
		Controller ctrler = ai.getController();
		HttpServletRequest request = ctrler.getRequest();
		
		String cookieToken = ctrler.getCookie(CSRF_KEY);
		
		//登录与首页可进入，并触发相关Cookie初始化信息
		if (RequestUtil.isAjaxRequest(request) == false && StrUtil.isBlank(cookieToken)) {
			
			if(ctrler instanceof LogonController || ctrler instanceof HomeController) {
				renderNormal(ai); 
				return;
			}
		}
		
		
        if (StrUtil.isBlank(cookieToken)) {
            renderBad(ai);
            return;
        }
        
        if (RequestUtil.isMultipartRequest(request)) {
        	ctrler.getFile();
        }

        renderNormal(ai);
        
		
	}
	
	private void renderNormal(Invocation ai) {
		Controller ctrler = ai.getController();
		HttpServletRequest request = ctrler.getRequest();
		
		if (RequestUtil.isAjaxRequest(request) == false) {
            String uuid = StrUtil.uuid();
            ctrler.setCookie(CSRF_KEY, uuid, -1);
            ctrler.setAttr(CSRF_KEY, uuid);
        }

        ai.invoke();
    }

	private void renderBad(Invocation ai) {
		if (RequestUtil.isAjaxRequest(ai.getController().getRequest())) {
            ai.getController().renderJson(Ret.fail().set("msg", "bad or mission token!"));
        } else {
            ai.getController().renderError(403, new TextRender("bad or missing token!"));
        }
		
	}
	
	
	
	

}
