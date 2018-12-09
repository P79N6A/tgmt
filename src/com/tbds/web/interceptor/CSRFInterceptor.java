package com.tbds.web.interceptor;

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
			
			//当用户首次进入系统时，不拦截登录页和首页，以便有入口进入系统
			if(ctrler instanceof LogonController || ctrler instanceof HomeController) {
				
				renderNormal(ai); 
				return;
				
			} else {//若用户访问别的资源时，则直接转入到登录页面
				
				ctrler.redirect("/login");
				return;
			}
			
			
		}
		
		//拦住非登陆和首页相关的AJAX的请求，但需要校验Cookie Token
        if (StrUtil.isBlank(cookieToken)) {
            renderBad(ai);
            return;
        }
        
        //文件处理
        if (RequestUtil.isMultipartRequest(request)) {
        	ctrler.getFile();
        }
        
        /**
         * 是否有必要——比较cookie：cookieToken以及url：cookieToken进行对吧，若相等才可进行登录！
         * TODO: 此处暂时可忽略考虑
         */

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
