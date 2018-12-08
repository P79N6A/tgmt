package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author totan
 *
 */

public class Role extends Model<Role> {
	private static final String ADMIN_FLAG = "超级管理员";
	private static final long serialVersionUID = -4703724473077239454L;
	
	public static final Role dao = new Role().dao();
	
	public boolean isSuperAdmin() {
		return ADMIN_FLAG.equals(this.get("name")) && "0".equals(this.get("flag"));
	}

}
