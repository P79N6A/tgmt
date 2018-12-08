package com.tbds.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.tbds.model.eo.Permission;
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
		
		String fromSql = "from tbds_role order by id asc";
		
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
	
	public static boolean hasPermission(int roleId, int permissionId) {

        List<Permission> permissions = PermissionService.findPermissionListByRoleId(roleId);

        if (permissions == null || permissions.isEmpty()) {
            return false;
        }

        for (Permission permission : permissions) {
            if (permission.getInt("id") == permissionId) return true;
        }

        return false;
    }
	
	public static boolean addPermission(int roleId, int permissionId) {
        Record rolePermission = new Record().set("role_id", roleId).set("permission_id", permissionId);
        return Db.save("tbds_role_permission_mapping", rolePermission);
    }
	
	public static boolean delPermission(int roleId, int permissionId) {
        Db.delete("delete from tbds_role_permission_mapping where role_id=? and permission_id=?", roleId, permissionId);
        return true;
    }
	
	
	/**
	 * 删除角色（并把相关的其他信息也需要一并删除，例如：user与role，role与permission相关的记录）
	 * @param role（真实对象）
	 * @return boolean
	 */
	public static boolean deleteRoleById(Role role) {
		
		return Db.tx(() -> {
			int roleId = role.getInt("id");
			Db.delete("delete from tbds_user_role_mapping where role_id = ? ", roleId);
			Db.delete("delete from tbds_role_permission_mapping where role_id = ? ", roleId);
			
			role.delete();
			
			return true;
		});
		
	}
	
	public static List<Role> findRoleListByUserId(int userId) {
        String sql = "select * from tbds_user_role_mapping where user_id = ?";
        List<Record> records = Db.find(sql, userId);
        if (records == null || records.isEmpty()) {
            return null;
        }

        List<Role> roles = new ArrayList<>();
        for (Record record : records) {
            Role role = findRoleById(record.getInt("role_id"));
            if (role != null) roles.add(role);
        }
        return roles;
    }
	
	
}
