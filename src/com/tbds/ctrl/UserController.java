package com.tbds.ctrl;

import com.jfinal.core.Controller;
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
		user.set("activated", new java.util.Date());
		user.set("status", "0");
		
		boolean flag = user.save();
		
		if(flag) {
			redirect("/auth/user");
		} else {
			System.err.println("Save User failed, please check it.");
			this.renderError(500);
		}
		
	}
	
	
}
