package com.tbds.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.tbds.model.eo.User;
import com.tbds.service.UserService;
import com.tbds.util.StrUtil;

public class UserController extends Controller {
	private static final Logger log = Logger.getLogger(MpsController.class);
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
				//user.set("status", "1");//默认修改密码不进行激活用户
				
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
							renderText("1");//renderText("2");
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
		 * TODO: 保存之前，判断账号是否已存在
		 */
		
		String userName = user.getStr("username");
		String nickName = user.getStr("nickname");
		String email = user.getStr("email");
		
		if(StrKit.isBlank(userName) || StrKit.isBlank(nickName) || StrKit.isBlank(email)) {
			renderText("-2");//提交的信息有误
			return;
		}
		
		User existUser = UserService.filterByUserName(userName);
		if(existUser != null) {
			renderText("-1");//已存在此用户！
			return;
		}
		
		//更新相关时间字段和状态
		user.set("created", new java.util.Date());
		user.set("modified", new java.util.Date());
		user.set("status", "0");//默认创建用户不激活
		
		boolean flag = user.save();
		
		if(flag) {
			int id = user.get("id");
			
			renderText(""+id);//
		} else {
			System.err.println("Save User failed, please check it.");
			renderText("0");//保存新用户存在错误
		}
		
	}
	
	/**
	 * 激活用户
	 */
	public void activate() {
		User user = getModel(User.class, "user");
		
		JSONObject jo = new JSONObject();
		
		if(null != user) {
		
			user.set("modified", new java.util.Date());
			user.set("activated", new java.util.Date());
			
			boolean flag = user.update();
			
			if(flag) {
				String status = user.getStr("status");
				if("1".equals(status)) {
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
	
	
}
