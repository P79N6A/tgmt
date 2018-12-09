package com.tbds.ctrl;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.Constants;

public class UserProfileController extends TbdsBaseController {
	private static final Logger log = Logger.getLogger(UserProfileController.class);
	
	public void index() {
		Integer userId = getSessionAttr(Constants.LOGINER_UNI_ID);
		
		if(null == userId) {
			//会话已失效
			redirect("/home");
		}
		
		int uid = userId.intValue();
		User user = UserService.findById(uid);
		
		setAttr("user", user);
		
		render("info.html");
	}
	
	public void info() {
		index();
	}
	
	public void settings() {
		render("settings.html");
	}
	
	public void update() {
		
		Integer userId = getSessionAttr(Constants.LOGINER_UNI_ID);
		
		if(null == userId) {
			//会话已失效
			redirect("/home");
		}
		
		int uid = userId.intValue();
		
		JSONObject resp = new JSONObject();
		
		User user = getModel(User.class, "user");
		
		user.set("id", uid);
		
		boolean flag = user.update();
		int code = 0;
		if(flag) {
			code = 1;
			resp.put("msg", "成功更新资料!");
		} else {
			resp.put("msg", "更新个人资料失败，请检查!");
		}
		resp.put("code", code);
		renderJson(resp);
	}
	
	public void chgpswd() {
		JSONObject resp = new JSONObject();
		
		Integer userId = getSessionAttr(Constants.LOGINER_UNI_ID);
		
		if(userId == null) {
			resp.put("msg", "当前会话已失效，请重新登录！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		String oldPassword = getPara("oldpassword");
		String newOnePassword = getPara("newonepassword");
		String newDualPassword = getPara("newdualpassword");
		
		//新密码为空，或确认密码不正确，均报错！
		if(StrKit.isBlank(oldPassword) || StrKit.isBlank(newOnePassword) || StrKit.isBlank(newDualPassword) || !newOnePassword.equals(newDualPassword)) {
			resp.put("msg", "旧密码为空，或者新密码与确认密码不匹配！");
			resp.put("code", -1);
			renderJson(resp);
			return;
		}
		
		int uid = userId.intValue();
		
		User user = UserService.findById(uid);
		
		if(user != null) {
			String password = user.getStr("password");
			String salt = user.getStr("salt");
			
			if(StrKit.isBlank(password)) {//从未设置过密码,忽略掉旧密码的提交
				//HashKit.md5(newDualPassword);
				salt = HashKit.generateSalt(32);
				password = HashKit.sha256(newDualPassword + salt);
				
				user.set("salt", salt);
				user.set("password", password);
				user.set("modified", new java.util.Date());
				//user.set("status", "1");//默认修改密码不进行激活用户
				
				boolean flag = user.update();
				
				if(flag) {//更改密码成功
					resp.put("msg", "成功添加新密码！");
					resp.put("code", 1);
					renderJson(resp);
					
				} else {//更改密码失败
					resp.put("msg", "添加新密码失败，请检查！");
					resp.put("code", 0);
					renderJson(resp);
				}
				
			} else {//不能忽略旧密码
				if(StrKit.isBlank(oldPassword)) {
					resp.put("msg", "旧密码不能未空！");
					resp.put("code", 0);
					renderJson(resp);
					return;
				} else {
					String oldEncPassword = HashKit.sha256(oldPassword + salt);	
					
					if(!password.equals(oldEncPassword)) {
						resp.put("msg", "旧密码输入不正确！");
						resp.put("code", 0);
						renderJson(resp);
						return;
					} else {
						salt = HashKit.generateSalt(32);
						password = HashKit.sha256(newDualPassword + salt);
						
						user.set("salt", salt);
						user.set("password", password);
						user.set("modified", new java.util.Date());
						
						boolean flag = user.update();
						
						if(flag) {//更改密码成功
							//renderText("1");//renderText("2");
							resp.put("msg", "成功修改密码！");
							resp.put("code", 1);
							renderJson(resp);
							
						} else {//更改密码失败
							//renderText("0");
							resp.put("msg", "修改密码失败，请检查！");
							resp.put("code", 1);
							renderJson(resp);
						}
					}
				}
				
			}
		} else {
			resp.put("msg", "修改密码失败，请检查！");
			resp.put("code", -1);
			renderJson(resp);
		}
	}
	
}
