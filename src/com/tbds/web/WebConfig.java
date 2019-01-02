/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.web;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.tbds.ctrl.MpsController;
import com.tbds.ctrl.FileTransferHistoryController;
import com.tbds.model.util.DbManager;
import com.tbds.model.util.ModelCacheManager;
import com.tbds.web.sharekit.PermissionKits;
import com.tbds.ctrl.*;
import com.tbds.model.eo.ErrorEvent;
import com.tbds.model.eo.JobTemplate;
import com.tbds.model.eo.Permission;
import com.tbds.model.eo.Role;
import com.tbds.model.eo.Server;
import com.tbds.model.eo.Job;
import com.tbds.model.eo.Transfer;
import com.tbds.model.eo.TransferedFile;
import com.tbds.model.eo.User;
import com.tbds.model.eo.ValueEvent;

/**
 *
 * @author totan
 */
public class WebConfig extends JFinalConfig {

	public static ModelCacheManager modelCache = null;

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		loadPropertyFile(com.tbds.util.Constants.CONFIG_FILE);

		boolean needRefresh = getPropertyToBoolean(com.tbds.util.Constants.MANNUAL_REFRESH_DB, true);

		ModelCacheManager.createInstance(needRefresh);
		modelCache = ModelCacheManager.getInstance();

		// 开发模式
		me.setDevMode(getPropertyToBoolean(com.tbds.util.Constants.DEV_MODE, true));

		// 设置404错误跳转页面
		me.setError404View("/404.html");
		me.setError500View("/500.html");

		// 国际化资源
		// me.setI18nDefaultBaseName("");

		// 设置URL参数分隔符,e.g. 1-3代表id=1&pages=3
		// me.setUrlParaSeparator("-");
		// 下面不再使用FreeMarker，改用Enjoy模板，具体查看下面configEngine方法
		// me.setViewType(ViewType.FREE_MARKER);
		
		// 文件upload根路径
		me.setBaseUploadPath(getProperty(com.tbds.util.Constants.BASE_UPLOAD_FILE_PATH));
		
		// 文件download根路径
		//me.setBaseDownloadPath("/filebrowser/download");
		me.setBaseDownloadPath(getProperty(com.tbds.util.Constants.BASE_DOWNLOAD_FILE_PATH));

	}

	@Override
	public void configRoute(Routes me) {
		// me.add(new TbdsRoutes());//路由
		// ***************自定义的路由配置往这里加***************
		// 为了安全考虑，建议放置到/WEB-INF/pages下面(防止别人读取到源代码)
		me.setBaseViewPath("/WEB-INF/pages");
		// **********************************************

		me.add("/", HomeController.class, "/home");

		me.add("/login", LogonController.class);

		me.add("/mps", MpsController.class);

		me.add("/fthistory", FileTransferHistoryController.class);

		me.add("/filebrowser", FileBrowserController.class);

		me.add("/analytics", AnalyticsController.class);

		/**
		 * 用户账号，角色，权限设置路由
		 */
		me.add("/auth/user", UserController.class);
		me.add("/auth/role", RoleController.class);
		me.add("/auth/permission", PermissionController.class);

		/**
		 * 个人信息修改（包括基本信息以及密码重置）
		 */
		me.add("/profile", UserProfileController.class);
		
		/*
		 * 定时任务管理
		 * */
		me.add("/scheduler", JobController.class);
		
		
		/**
		 * Event Error + Value Controller
		 */
		

	}

	@Override
	public void configPlugin(Plugins plugin) {
		String url = getProperty(com.tbds.util.Constants.DB_URL);
		String uid = getProperty(com.tbds.util.Constants.DB_UID);
		String pwd = getProperty(com.tbds.util.Constants.DB_PWD);

		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = DbManager.initC3p0Plugin(url, uid, pwd);
		plugin.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = DbManager.initActiveRecordPlugin(c3p0Plugin);
		plugin.add(arp);

		arp.addMapping("YADE_FILES", "ID", TransferedFile.class);
		arp.addMapping("YADE_TRANSFERS", "ID", Transfer.class);

		/**
		 * 用户，角色，权限
		 */
		arp.addMapping("tbds_user", "id", User.class);
		arp.addMapping("tbds_role", "id", Role.class);
		arp.addMapping("tbds_permission", "id", Permission.class);
		
		/**
		 * Event Error + Value
		 */
		arp.addMapping("tbds_event_error", "id", ErrorEvent.class);
		arp.addMapping("tbds_event_value", "id", ValueEvent.class);
		
		/**
		 * Job Template
		 */
		arp.addMapping("tbds_job_template", "id", JobTemplate.class);
		
		/**
		 * Server Management
		 */
		arp.addMapping("tbds_server", "id", Server.class);
		
		/**
		 * Server Links Management
		 */
		arp.addMapping("tbds_job", "id", Job.class);
		

		// 构建表与Model的关系，具体请参考Model工程中DbManager类
		DbManager.initModelMapping(arp);

		// 配置缓存的Plugin
		// plugin.add(new EhCachePlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// me.add(new WebInterceptor());//添加全局拦截器，用于判断用户是否已正常登录

		// for session globally
		me.add(new SessionInViewInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		// 添加一个全局的路径handler,用来匹配上下文目录()
		// 如果是用的别的模板引擎(jsp)，则可以添加一个Handler解上下文
		me.add(new ContextPathHandler("CPATH"));
		// me.add(new SessionHandler());
		// 刷新DB
		// modelCache.refreshDB();

	}

	@Override
	public void configEngine(Engine me) {
		me.addSharedFunction("/WEB-INF/pages/common/_header.html");
		me.addSharedFunction("/WEB-INF/pages/common/_layout.html");
		me.addSharedFunction("/WEB-INF/pages/common/_footer.html");
		me.addSharedFunction("/WEB-INF/pages/common/_menu.html");
		me.addSharedFunction("/WEB-INF/pages/common/_paginate.html");
		me.addSharedFunction("/WEB-INF/pages/common/_sidebar.html");

		// 如果是用的jfinal template（也就是html[freemark]），只需要配置一句
		me.addSharedObject("CPATH", JFinal.me().getContextPath());

		// 添加可直接在模板调用的静态方法
		me.addSharedStaticMethod(PermissionKits.class);
	}
}
