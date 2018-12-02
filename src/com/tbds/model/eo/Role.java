package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author totan
 *
 */

public class Role extends Model<Role> {
	
	private static final long serialVersionUID = -4703724473077239454L;
	
	public static final Role dao = new Role().dao();

}
