package com.tbds.ctrl;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.Permission;
import com.tbds.model.eo.Role;
import com.tbds.service.PermissionService;
import com.tbds.service.RoleService;
import com.tbds.util.StrUtil;

public class PermissionController extends Controller {
	private static final Logger log = Logger.getLogger(PermissionController.class);
	
	public void index() {
		int currentPageIndex = getParaToInt(0, 1);
		
		String permType = getPara("qPermType");
		String keyword = getPara("qKeyword");
		
		if (StrUtil.notBlank(keyword) || StrUtil.notBlank(permType)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					log.error(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("permissionPage", PermissionService.search(currentPageIndex, 10, permType, keyword));
			
			setAttr("qPermType", permType);
			setAttr("qKeyword", keyword);
			
		} else {
			setAttr("permissionPage", PermissionService.paginate(currentPageIndex, 10));
		}
		render("index.html");
	}
	
	public void sync() {
		JSONObject resp = new JSONObject();
		
		JSONObject syncResult = PermissionService.refreshPermissions();		
		if(syncResult != null && syncResult.getBooleanValue("status")) {
        	resp.put("msg", "成功同步了(" + syncResult.getIntValue("counter")  + "条)权限信息！");
			resp.put("code", 1);
        } else {
        	resp.put("msg", "同步权限信息失败，请检查！");
			resp.put("code", 0);
        }
        
        renderJson(resp);
        
	}
	
	public void delete() {
		JSONObject resp = new JSONObject();
		
		Permission permission = getModel(Permission.class, "permission");
		
		if(null == permission) {
			resp.put("msg", "删除权限资源失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		boolean flag = PermissionService.deletePermission(permission);
		
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功删除权限资源!");
		} else {
			resp.put("msg", "删除权限资源失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	public void edit() {
		int permissionId = getParaToInt(0, 1);
		setAttr("permission", PermissionService.findById(permissionId));
		render("edit.html");
	}
	
	public void update() {
		JSONObject resp = new JSONObject();
		
		Permission permission = getModel(Permission.class, "permission");
		
		if(null == permission) {
			resp.put("msg", "更新权限资源失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		permission.set("modified", new java.util.Date());
		
		boolean flag = permission.update();
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功更新权限资源!");
		} else {
			resp.put("msg", "更新权限资源失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	
	
}
