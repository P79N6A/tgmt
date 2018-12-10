package com.tbds.ctrl;


import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.kit.Ret;
import com.tbds.web.interceptor.CSRFInterceptor;
import com.tbds.web.interceptor.LoginerInterceptor;
import com.tbds.web.interceptor.PermissionInterceptor;

@Before({
    CSRFInterceptor.class,
    LoginerInterceptor.class,
    PermissionInterceptor.class
})
public abstract class TbdsBaseController extends Controller {
	
	public static final String NO_PERMISSION_DENIED_VIEW = "/WEB-INF/pages/error/denied.html";
	
	
}
