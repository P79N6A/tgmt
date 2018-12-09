package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.Permission;
import com.tbds.model.eo.Role;
import com.tbds.model.eo.User;
import com.tbds.service.PermissionService;
import com.tbds.service.RoleService;
import com.tbds.service.UserService;
import com.tbds.util.StrUtil;

public class UserController extends Controller {
	private static final Logger log = Logger.getLogger(UserController.class);

	public void index() {
		int currentPageIndex = getParaToInt(0, 1);

		String keyword = getPara("qKeyword");

		if (StrUtil.notBlank(keyword)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					log.error(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("userPage", UserService.search(currentPageIndex, 5, keyword));

			setAttr("qKeyword", keyword);

		} else {
			setAttr("userPage", UserService.paginate(currentPageIndex, 5));
		}

		render("index.html");
	}

	public void add() {
		render("add.html");
	}

	public void edit() {
		setAttr("user", UserService.findById(getParaToInt()));
		render("edit.html");
	}

	public void update() {
		JSONObject resp = new JSONObject();
		User user = getModel(User.class, "user");
		
		if(null == user) {
			resp.put("msg", "更新用户失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		user.set("modified", new java.util.Date());
		
		boolean flag = user.update();
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功更新用户!");
		} else {
			resp.put("msg", "更新用户失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}

	public void chgpswd() {
		JSONObject resp = new JSONObject();

		// String oldPassword = getPara("oldpassword");//管理员操作不再考虑旧密码
		String newOnePassword = getPara("newonepassword");
		String newDualPassword = getPara("newdualpassword");

		// 新密码为空，或确认密码不正确，均报错！
		if (StrKit.isBlank(newOnePassword) || StrKit.isBlank(newDualPassword)
				|| !newOnePassword.equals(newDualPassword)) {
			resp.put("msg", "新密码与确认密码为空或不匹配！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}

		int uid = getParaToInt("userid");
		User user = UserService.findById(uid);

		if (user != null) {
			String password = user.getStr("password");
			String salt = user.getStr("salt");

			if (StrKit.isBlank(password)) {// 从未设置过密码,忽略掉旧密码的提交
				// HashKit.md5(newDualPassword);
				salt = HashKit.generateSalt(32);
				password = HashKit.sha256(newDualPassword + salt);

				user.set("salt", salt);
				user.set("password", password);
				user.set("modified", new java.util.Date());
				// user.set("status", "1");//默认修改密码不进行激活用户

				boolean flag = user.update();

				if (flag) {// 更改密码成功
					resp.put("msg", "成功添加新密码！");
					resp.put("code", 1);
					renderJson(resp);

				} else {// 更改密码失败
					resp.put("msg", "添加新密码失败，请检查！");
					resp.put("code", 0);
					renderJson(resp);
				}

			} else {
				if(StrKit.isBlank(salt)) {
					salt = HashKit.generateSalt(32);
				}
				
				password = HashKit.sha256(newDualPassword + salt);

				user.set("salt", salt);
				user.set("password", password);
				user.set("modified", new java.util.Date());

				boolean flag = user.update();

				if (flag) {// 更改密码成功
					// renderText("1");//renderText("2");
					resp.put("msg", "成功修改密码！");
					resp.put("code", 1);
					renderJson(resp);

				} else {// 更改密码失败
					// renderText("0");
					resp.put("msg", "修改密码失败，请检查！");
					resp.put("code", 1);
					renderJson(resp);
				}

			}
		} else {
			log.error("用户试图在撞库攻击！请检查~");
			resp.put("msg", "您试图修改的用户不成功，请联系管理员！");
			resp.put("code", -1);
			renderJson(resp);
		}
	}

	/**
	 * 检查用户名是否可用
	 */
	public void checkexistuname() {
		JSONObject jo = new JSONObject();
		String username = getPara("username");
		User existUser = UserService.filterByUserName(username);

		if (existUser != null) {
			jo.put("msg", "用户名已存在！");
			jo.put("code", 0);
		} else {
			jo.put("msg", "新用户名可用！");
			jo.put("code", 1);
		}
		renderJson(jo);

	}

	/*
	 * 需要判断用户名是否已存在
	 */
	public void save() {
		JSONObject resp = new JSONObject();
		User user = getModel(User.class, "user");

		/* 保存之前，判断账号是否已存在 */
		String userName = user.getStr("username");
		String nickName = user.getStr("nickname");
		String email = user.getStr("email");

		if (StrKit.isBlank(userName) || StrKit.isBlank(nickName) || StrKit.isBlank(email)) {
			resp.put("msg", "用户名或昵称或邮箱为空，请检查！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}

		User existUser = UserService.filterByUserName(userName);

		if (existUser != null) {
			resp.put("msg", "用户名已存在，请重新输入新用户名！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}

		// 更新相关时间字段和状态
		user.set("created", new java.util.Date());
		user.set("modified", new java.util.Date());
		user.set("status", "0");// 默认创建用户不激活

		boolean flag = user.save();

		if (flag) {
			int id = user.get("id");
			resp.put("msg", "用户保存成功！");
			resp.put("code", 1);
			resp.put("id", id);
			renderJson(resp);
		} else {
			System.err.println("Save User failed, please check it.");
			resp.put("msg", "用户保存失败！");
			resp.put("code", 0);
			renderJson(resp);
		}

	}

	/**
	 * 激活用户
	 */
	public void activate() {
		User user = getModel(User.class, "user");

		JSONObject jo = new JSONObject();

		if (null != user) {

			user.set("modified", new java.util.Date());
			user.set("activated", new java.util.Date());

			boolean flag = user.update();

			if (flag) {
				String status = user.getStr("status");
				if ("1".equals(status)) {
					jo.put("msg", "成功激活用户！");
				} else {
					jo.put("msg", "成功禁用用户！");
				}

				jo.put("code", 1);
				renderJson(jo);
				return;
			}
		}

		jo.put("msg", "激活用户失败！");
		jo.put("code", 0);
		renderJson(jo);

	}
	
	/**
	 *  用户状态切换（常用于解锁或锁住用户）
	 */
	public void shift() {
		User user = getModel(User.class, "user");

		JSONObject jo = new JSONObject();

		if (null != user) {

			user.set("modified", new java.util.Date());
			
			boolean flag = user.update();

			if (flag) {
				String status = user.getStr("status");
				if ("1".equals(status)) {
					jo.put("msg", "成功激活用户！");
				} else if("2".equals(status)) {
					jo.put("msg", "成功lock用户！");
				} else if("0".equals(status)) {
					jo.put("msg", "成功禁用用户！");
				}
				
				jo.put("code", 1);
				renderJson(jo);
				return;
			}
		}

		jo.put("msg", "激活用户失败！");
		jo.put("code", 0);
		renderJson(jo);

	}
	
	/**
	 * 删除用户
	 * 注意：需要考虑将其对应的角色关联信息也删除！
	 */
	public void delete() {
		JSONObject resp = new JSONObject();
		User user = getModel(User.class, "user");
		
		if(null == user) {
			resp.put("msg", "删除用户失败，请检查!");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		if("s".equalsIgnoreCase(user.getStr("flag"))) {
			resp.put("msg", "删除用户失败，超级管理员不可删除！");
			resp.put("code", 0);
			renderJson(resp);
			return;
		}
		
		boolean flag = UserService.deleteByUser(user);
		int code = 0;
		if (flag) {
			code = 1;
			resp.put("msg", "成功删除用户!");
		} else {
			resp.put("msg", "删除用户失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	public void setrole() {
		int userId = getParaToInt();
		setAttr("user", UserService.findById(userId));
		
		List<Role> roles = RoleService.findAll();
        setAttr("roles", roles);
        
        List<Role> myroles = RoleService.findRoleListByUserId(userId);
        setAttr("myroles", myroles);
		
		render("setrole.html");
	}
	
	public void assignrole() {
		JSONObject resp = new JSONObject();
		Integer assigned = getParaToInt("assigned");
		Integer userId =  getParaToInt("userId");
		Integer roleId =  getParaToInt("roleId");
		
		boolean flag = false;
		
		int code = 0;
		String msg = "";
		if(assigned == 1) {//赋予角色
			flag = RoleService.addUserRole(userId, roleId);
			msg = "赋予角色";
			
		} else if(assigned == 0) {//取消角色
			flag = RoleService.deleteUserRole(userId, roleId);
			msg = "取消角色";
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
	
	public void mypermissions() {
		int userId = getParaToInt();
		
		User user = UserService.findById(userId);
        setAttr("user", user);
        
        Map<String, List<Permission>> groupPermissions = PermissionService.findAllByGroupPermissionNode();
        
        List<Permission> myPermissions = PermissionService.findPermissionListByUserId(userId);
        
        setAttr("groupPermissions", groupPermissions);
        setAttr("myPermissions", myPermissions);
        
        render("userpermission.html");
	}

}
