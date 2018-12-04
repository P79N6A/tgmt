package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.Role;
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
			setAttr("rolePage", RoleService.search(currentPageIndex, 2, roleType, keyword));
			setAttr("qRoleType", roleType);
			setAttr("qKeyword", keyword);
		} else {
			setAttr("rolePage", RoleService.paginate(currentPageIndex, 2));
		}
		render("index.html");
	}
	
	public void add() {
		render("add.html");
	}
	
	public void edit() {
		int roleId = getParaToInt(0, 1);
		setAttr("role", RoleService.findRoleById(roleId));
		render("edit.html");
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
}
