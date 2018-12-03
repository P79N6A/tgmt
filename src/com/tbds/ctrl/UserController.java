package com.tbds.ctrl;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;

public class UserController extends Controller {
	
	public void index() {
		
		setAttr("userPage", UserService.paginate(getParaToInt(0, 1), 5));
		
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
		User user = getModel(User.class, "user");
		boolean flag = user.update();
		String msg = "";
		if(flag) {
			msg = "1";
		} else {
			msg = "0";
		}
		renderText(msg);
	}
	
	
	public void chgpswd() {
		
		String oldPassword = getPara("oldpassword");
		String newOnePassword = getPara("newonepassword");
		String newDualPassword = getPara("newdualpassword");
		
		//新密码为空，或确认密码不正确，均报错！
		if(StrKit.isBlank(newOnePassword) || StrKit.isBlank(newDualPassword) || !newOnePassword.equals(newDualPassword)) {
			renderText("新密码与确认密码为空或不匹配！");
			return;
		}
		
		int uid = getParaToInt("userid");
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
				user.set("status", "1");//一并激活用户
				
				boolean flag = user.update();
				
				if(flag) {//更改密码成功
					renderText("1");
				} else {//更改密码失败
					renderText("0");
				}
				
			} else {//不能忽略旧密码
				if(StrKit.isBlank(oldPassword)) {
					renderText("旧密码为空！");
					return;
				} else {
					String oldEncPassword = HashKit.sha256(oldPassword + salt);	
					
					if(!password.equals(oldEncPassword)) {
						renderText("旧密码不正确！");
						return;
					} else {
						salt = HashKit.generateSalt(32);
						password = HashKit.sha256(newDualPassword + salt);
						
						user.set("salt", salt);
						user.set("password", password);
						user.set("modified", new java.util.Date());
						
						boolean flag = user.update();
						
						if(flag) {//更改密码成功
							renderText("2");
						} else {//更改密码失败
							renderText("0");
						}
					}
				}
				
			}
		}
	}
	
	/*
	 * 需要判断用户名是否已存在
	 */
	public void save() {
		User user = getModel(User.class, "user");
		
		/*
		 * TODO: 保存之前，随机生成密码加密盐;账号是否已存在
		 */
		
		user.set("created", new java.util.Date());
		user.set("modified", new java.util.Date());
		//user.set("activated", new java.util.Date());
		user.set("status", "0");
		
		boolean flag = user.save();
		
		if(flag) {
			int id = user.get("id");
			redirect("/auth/user/edit/" + id);
		} else {
			System.err.println("Save User failed, please check it.");
			this.renderError(500);
		}
		
	}
	
	
}
