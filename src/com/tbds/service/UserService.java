package com.tbds.service;

import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.User;

public class UserService {
	
	public static Page<User> paginate(int pageNumber, int pageSize) {
        return User.dao.paginate(pageNumber, pageSize, "select *", " from tbds_user order by id asc");
    }
	
	public static User findById(int id) {
		return User.dao.findById(id);
	}
	
	public static User findByLoginUserName(String userName) {
		return User.dao.findFirst("select username, password, salt from tbds_user where status='1' and username = ?", userName);
	}
	
}
