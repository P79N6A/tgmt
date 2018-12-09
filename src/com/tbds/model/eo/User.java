package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author totan
 *
 */

public class User extends Model<User> {
	
	private static final long serialVersionUID = -1533376620780172252L;
	
	public static final User dao = new User().dao();
	
	public boolean isActive() {
		if("1".equals(this.get("status"))) {
			return true;
		}
		return false;
	}
}
