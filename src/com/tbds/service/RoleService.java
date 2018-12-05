package com.tbds.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.tbds.model.eo.Role;

public class RoleService {
	public static Page<Role> paginate(int pageNumber, int pageSize) {
		return Role.dao.paginate(pageNumber, pageSize, "select *", " from tbds_role order by id asc");
	}
	
	public static Page<Role> search(int pageNumber, int pageSize,String roleType, String keyword) {
		String whereSql = "";
		if(StrKit.notBlank(roleType) && !"all".equalsIgnoreCase(roleType) && roleType.length() == 1) {
			whereSql += " flag = '" + roleType + "'";
		}
		
		String likeSql = "";
		if(StrKit.notBlank(keyword)) {
			likeSql = " name like '%" + keyword + "%' or description like '%" + keyword + "%'";
		}
		
		if(whereSql.length() > 0 && likeSql.length() > 0) {
			whereSql = whereSql + " and (" + likeSql + ")";
		} else {
			whereSql += likeSql;
		}
		
		String fromSql = "from tbds_role order by by id asc";
		
		if(StrKit.notBlank(whereSql)) {
			fromSql = " from tbds_role where " + whereSql +" order by id asc";
		}
		
		return Role.dao.paginate(pageNumber, pageSize, "select *", fromSql);
	}
	
	public static Role filterByRoleName(String roleName) {
		return Role.dao.findFirst("select * from tbds_role where name = ?", roleName);
	}
	
	public static Role findRoleById(int id) {
		return Role.dao.findById(id);
	}
}
