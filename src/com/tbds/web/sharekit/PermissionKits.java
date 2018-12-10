package com.tbds.web.sharekit;

import com.tbds.model.eo.User;
import com.tbds.service.PermissionService;

public class PermissionKits {
	
	public static boolean hasPermission(User user, int permissionId) {
        return PermissionService.hasPermission(user.getInt("id"), permissionId);
    }
	
	public static boolean hasPermission(User user, String actionKey) {
        return PermissionService.hasPermission(user.getInt("id"), actionKey);
    }
	
}
