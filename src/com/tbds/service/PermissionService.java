package com.tbds.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.Permission;
import com.tbds.util.Constants;


public class PermissionService {
	
	private static List<String> BELONG_PERMISSION_MENU_TYPE_LST = new ArrayList<String>();
	private static List<String> BELONG_PERMISSION_OPER_TYPE_LST = new ArrayList<String>();
	
	static {
		PropKit.use(Constants.CONFIG_FILE);
		
		String menus = PropKit.get(Constants.PERMISSION_MENU_TYPE);
		String opers = PropKit.get(Constants.PERMISSION_OPER_TYPE);
		
		if(null != menus) {
			String[] menuArr = menus.split(",");
			
			for(String menu : menuArr) {
				BELONG_PERMISSION_MENU_TYPE_LST.add(menu);
			}
			
		}
		
		if(null != opers) {
			String[] operArr = opers.split(",");
			
			for(String oper : operArr) {
				BELONG_PERMISSION_OPER_TYPE_LST.add(oper);
			}
		}
	}
	
	public static Page<Permission> paginate(int pageNumber, int pageSize) {
		return Permission.dao.paginate(pageNumber, pageSize, "select *", " from tbds_permission order by id asc");
	}
	
	public static Page<Permission> search(int pageNumber, int pageSize, String permType, String keyword) {
		
		System.out.println("**********permType = " + permType + " & keyword = " + keyword);
		
		String whereSql = "";
		if(StrKit.notBlank(permType) && !"all".equalsIgnoreCase(permType)) {
			whereSql += " type = '" + permType + "'";
		}
		
		String likeSql = "";
		if(StrKit.notBlank(keyword)) {
			likeSql = " action_key like '%" + keyword + "%' or node like '%" + keyword + "%' or text like '%" + keyword + "%'";
		}
		
		if(whereSql.length() > 0 && likeSql.length() > 0) {
			whereSql = whereSql + " and (" + likeSql + ")";
		} else {
			whereSql += likeSql;
		}
		
		String fromSql = " from tbds_permission order by id asc";
		if(StrKit.notBlank(whereSql)) {
			fromSql = " from tbds_permission where " + whereSql + " order by id asc";
		}
		
		System.out.println(fromSql);
		
		
		return Permission.dao.paginate(pageNumber, pageSize, "select *", fromSql);
	}
	
	public static Permission findByCtrlKey(String ctrlKey) {
		return Permission.dao.findFirst("select * from tbds_permission where action_key = ?", ctrlKey);
	}
	
	public static Permission findById(int id) {
		return Permission.dao.findById(id);
	}
	
	public static JSONObject refreshPermissions() {
		JSONObject response = new JSONObject();
		
		boolean done = true;
		
		int counter = 0;
		List<String> allActionKeys = JFinal.me().getAllActionKeys();
		

        String[] urlPara = new String[1];
        for (String actionKey : allActionKeys) {
        	//if (actionKey.startsWith("/admin")) {
            //Action action = JFinal.me().getAction(actionKey, urlPara);
            //action.getMethod().getAnnotation(AdminMenu.class);
        	
        	Action action = JFinal.me().getAction(actionKey, urlPara);
        	
        	//忽略null值的action
        	if(action == null) {
        		continue;
        	}
        	
        	String ctrlKey = action.getControllerKey();
        	
        	/*
        	 *	如下部分不计入权限管理
        	 *	登录部分： /login
        	 *	+ ctrlKey 或 actionKey为空时
        	 * 
        	 */
        	if(ctrlKey.equals("/login") || StrKit.isBlank(ctrlKey) || StrKit.isBlank(actionKey)) {
        		continue;
        	}
        	
        	/*
        	 * 	普通用户均可访问（暂时不做任何权限控制），主要通过Interceptor来默认控制，只要是登录用户均有相应的权限
        	 */
        	//if(ctrlKey.equals("/") || ctrlKey.equals("/profile")) {
        	//	continue;
        	//}
        	
        	
        	String methodName = action.getMethodName();
        	
        	String permissionType = null;
        	if(BELONG_PERMISSION_MENU_TYPE_LST.contains(methodName)) {
        		/*
            	 * 	以下方法归类为“菜单【menu】”权限控制
            	 * 	index, home, search, info, status
            	 */
        		permissionType = "menu";
        		
        	} else if(BELONG_PERMISSION_OPER_TYPE_LST.contains(methodName)) {
        		/*
            	 * 	以下方法归类为“操作【oper】”权限控制
            	 * 	add,edit,delete,del,update,save,chgpswd,settings,download,land,quit,sync
            	 */
        		permissionType = "oper";
        		
        	} else {
        		
        		permissionType = "other";
        		
        	}
        	
        	Permission permission = findByCtrlKey(actionKey);
        	
        	if(null == permission) {
        		permission = new Permission();
        		permission.set("action_key", actionKey);
        		permission.set("node", methodName);
            	permission.set("type", permissionType);
            	permission.set("text", actionKey);
            	permission.set("modified", new java.util.Date());
        		permission.set("created", new java.util.Date());
        		
        		done = done && permission.save();
        		
        	} else {
        		permission.set("node", methodName);
            	permission.set("type", permissionType);
            	//permission.set("text", actionKey);
            	permission.set("modified", new java.util.Date());
            	done = done && permission.update();
        	}
        	
        	counter++;
        	
        }
        
        response.put("counter", new Integer(counter));
        response.put("status", new Boolean(done));
        
        return response;
        
	}
	
	
	
	
}
