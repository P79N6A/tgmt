package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author totan
 *
 */
public class Permission extends Model<Permission> {

	private static final long serialVersionUID = -8089926180974771433L;
	
	public static final Permission dao = new Permission().dao();

}
