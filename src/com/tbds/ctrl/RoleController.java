package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.Permission;
import com.tbds.model.eo.Role;
import com.tbds.service.PermissionService;
import com.tbds.service.RoleService;
import com.tbds.util.StrUtil;

public class RoleController extends Controller {
	private static final Logger log = Logger.getLogger(RoleController.class);
	public void index() {
		int currentPageIndex = getParaToInt(0, 1);
		
		String roleType = getPara("qRoleType");
		String keyword = getPara("qKeyword");
		
		if (StrUtil.notBlank(keyword) || StrUtil.notBlank(roleType)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					log.error(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("rolePage", RoleService.search(currentPageIndex, 5, roleType, keyword));
			setAttr("qRoleType", roleType);
			setAttr("qKeyword", keyword);
		} else {
			setAttr("rolePage", RoleService.paginate(currentPageIndex, 5));
		}
		render("index.html");
	}
	
	public void add() {
		render("add.html");
	}
	
	public void edit() {
		Integer roleId = getParaToInt(0, 1);
		if(roleId == null || roleId == 0) {
			this.renderError(404);
			return;
		}
		setAttr("role", RoleService.findRoleById(roleId));
		render("edit.html");
	}
	
	public void setpermission() {
		Integer roleId = getParaToInt(0, 1);
		if(roleId == null || roleId == 0) {
			this.renderError(404);
			return;
		}
		
		Role role = RoleService.findRoleById(roleId);
		if(role == null) {
			this.renderError(404);
			return;
		}
		
		setAttr("role", role);
		
		Map<String, List<Permission>> permissionGroup = PermissionService.findAllByGroupPermissionNode();
		
		Map<Integer, Boolean> ownPermCheck = new HashMap<Integer, Boolean>();
		Map<String, Boolean> groupCheck = new HashMap<String, Boolean>();
        for (String groupKey : permissionGroup.keySet()) {
            List<Permission> permList = permissionGroup.get(groupKey);
            for (Permission permission : permList) {
            	Integer permId = permission.getInt("id");
                boolean hasPerm = RoleService.hasPermission(roleId, permId);
                if (!hasPerm) {
                    groupCheck.put(groupKey, false);
                    break;
                } else {
                    groupCheck.put(groupKey, true);
                    System.out.println(">>>>>>>>>>>" + roleId + " (own) " + permId);
                    ownPermCheck.put(permId, true);
                }
            }
        }
		
        setAttr("ownPermCheck", ownPermCheck);
        setAttr("groupCheck", groupCheck);
        setAttr("permissionGroup", permissionGroup);
		
		render("setpermission.html");
	}
	
	/**
	 * 授权给角色
	 */
	public void grant() {
		JSONObject resp = new JSONObject();
		Integer assigned = getParaToInt("assigned");
		Integer permissionId =  getParaToInt("permissionId");
		Integer roleId =  getParaToInt("roleId");
		
		boolean flag = false;
		
		int code = 0;
		String msg = "";
		if(assigned == 1) {//赋予权限
			flag = RoleService.addPermission(roleId, permissionId);
			msg = "授予权限";
			
		} else if(assigned == 0) {//取消权限
			flag = RoleService.delPermission(roleId, permissionId);
			msg = "取消权限";
		}
		
		if(flag) {//操作成功！
			code = 1;
			msg += "成功";
		} else {
			msg += "失败";
		}
		
		resp.put("code", code);
		resp.put("msg", msg);
		
		renderJson(resp);
		
	}
	
	public void batchgrant() {
		JSONObject resp = new JSONObject();
		
		String action = getPara("action");
		String nodeName = getPara("node");
		Integer roleId = getParaToInt("roleid");
		
		System.out.println("action = " + action);
		System.out.println("nodeName = " + nodeName);
		System.out.println("roleId = " + roleId);
		
		if(StrKit.isBlank(action) || (!"grant".equals(action) && !"cancel".equals(action))) {
			resp.put("code", -1);
			resp.put("msg", "操作未知，请检查！");
			renderJson(resp);
			return;
		}
		
		if(null == roleId || roleId == 0 || roleId < 0) {
			resp.put("code", -1);
			resp.put("msg", "角色未知，请检查！");
			renderJson(resp);
			return;
		}
		
		if(StrKit.isBlank(nodeName)) {
			resp.put("code", -1);
			resp.put("msg", "模块组信息未知，请检查！");
			renderJson(resp);
			return;
		}
		
		List<Permission> permissionList = PermissionService.findListByNode(nodeName);
		
		boolean x = true;
		
		if("grant".equals(action)) {//批量授权
			for (Permission permission : permissionList) {
				int permissionId = permission.get("id");
				
				if (!RoleService.hasPermission(roleId, permissionId)) {//得先删除原有得角色
					x &= RoleService.addPermission(roleId, permissionId);
	            }
			}
			
			if(x) {
	        	resp.put("code", 1);
	        	resp.put("msg", "批量授权成功！");
	        } else {
	        	resp.put("code", 0);
	        	resp.put("msg", "批量授权失败！");
	        }
			
		} else if("cancel".equals(action)) {//批量取消授权
			
	        for (Permission permission : permissionList) {
	            x &= RoleService.delPermission(roleId, permission.get("id"));
	        }
	        
	        if(x) {
	        	resp.put("code", 1);
	        	resp.put("msg", "批量取消授权成功！");
	        } else {
	        	resp.put("code", 0);
	        	resp.put("msg", "批量取消授权失败！");
	        }
			
		}
		
		resp.put("currentRoleId", roleId);
				
		renderJson(resp);
    }
	
	
	
	public void save() {
		JSONObject resp = new JSONObject();
		Role role = getModel(Role.class, "role");

		/* 保存之前，判断相同的角色名称是否已存在 */
		String roleName = role.getStr("name");

		if (StrKit.isBlank(roleName)) {
			resp.put("msg", "角色名称不能为空！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}

		Role existRole = RoleService.filterByRoleName(roleName.trim());

		if (existRole != null) {
			resp.put("msg", "相同名称的角色已存在，请重新输入角色名称！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		// 更新相关时间字段和状态
		role.set("created", new java.util.Date());
		role.set("modified", new java.util.Date());

		boolean flag = role.save();

		if (flag) {
			int id = role.get("id");
			resp.put("msg", "角色保存成功！");
			resp.put("code", 1);
			resp.put("id", id);
			renderJson(resp);
		} else {
			log.error("Save Role failed, please check it.");
			resp.put("msg", "角色保存失败！");
			resp.put("code", 0);
			renderJson(resp);
		}
	}
	
	public void update() {
		JSONObject resp = new JSONObject();
		
		Role role = getModel(Role.class, "role");
		
		if(null == role) {
			resp.put("msg", "更新角色失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		role.set("modified", new java.util.Date());
		
		boolean flag = role.update();
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功更新角色!");
		} else {
			resp.put("msg", "更新角色失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	public void delete() {
		JSONObject resp = new JSONObject();
		
		Role role = getModel(Role.class, "role");
		
		if(null == role) {
			resp.put("msg", "删除角色失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		if("0".equals(role.getStr("flag")) && "超级管理员".equals(role.get("name"))) {//属于系统默认角色, 且是“超级管理员”，不能删除
			resp.put("msg", "删除角色失败，系统默认“超级管理员”角色不可删除！!");
			resp.put("code", 0);
			renderJson(resp);
			return;
		}
		
		boolean flag = RoleService.deleteRoleById(role);
		
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功删除角色!");
		} else {
			resp.put("msg", "删除角色失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
}
