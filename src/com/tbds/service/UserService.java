package com.tbds.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.User;
import com.tbds.util.StrUtil;

public class UserService {
	
	public static Page<User> paginate(int pageNumber, int pageSize) {
        return User.dao.paginate(pageNumber, pageSize, "select *", " from tbds_user order by id asc");
    }
	
	public static User findById(int id) {
		return User.dao.findById(id);
	}
	
	public static User findByLoginUserName(String userName) {
		if(!StrUtil.isValid(userName)) {
				return null;
		}
		return User.dao.findFirst("select id, username, password, salt, logged from tbds_user where status='1' and username = ?", userName);
	}
	
	public static User filterByUserName(String userName) {
		return User.dao.findFirst("select id, username from tbds_user where username = ?", userName);
	}
	
	public static Page<User> search(int pageNumber, int pageSize, String keyword) {
		//username, nickname, email, mobile, company
		String likeSql = " username like '%" + keyword + "%' or nickname like '%" + keyword + "%' or email like '%" + keyword + "%' or mobile like '%" + keyword + "%' or company like '%" + keyword + "%' ";
        return User.dao.paginate(pageNumber, pageSize, "select *", " from tbds_user where " + likeSql + " order by id asc");
    }
	
}
