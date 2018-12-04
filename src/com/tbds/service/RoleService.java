package com.tbds.service;

import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.Role;

public class RoleService {
	public static Page<Role> paginate(int pageNumber, int pageSize) {
		return Role.dao.paginate(pageNumber, pageSize, "select *", " from tbds_role order by id asc");
	}
}
