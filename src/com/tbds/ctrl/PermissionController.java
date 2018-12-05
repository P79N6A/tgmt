package com.tbds.ctrl;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.tbds.service.PermissionService;
import com.tbds.util.StrUtil;

public class PermissionController extends Controller {
	private static final Logger log = Logger.getLogger(PermissionController.class);
	
	public void index() {
		int currentPageIndex = getParaToInt(0, 1);
		
		String permType = getPara("qPermType");
		String keyword = getPara("qKeyword");
		
		if (StrUtil.notBlank(keyword) || StrUtil.notBlank(permType)) {
			// 处理keyword传入：解码处理（中文keyword）
			if (StrUtil.notBlank(keyword)) {
				try {
					keyword = URLDecoder.decode(keyword, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					log.error(ex.getMessage());
				}
			}
			// 通过keyword进一步搜索查询
			setAttr("permissionPage", PermissionService.search(currentPageIndex, 10, permType, keyword));
			
			setAttr("qPermType", permType);
			setAttr("qKeyword", keyword);
			
		} else {
			setAttr("permissionPage", PermissionService.paginate(currentPageIndex, 10));
		}
		render("index.html");
	}
	
	public void sync() {
		JSONObject resp = new JSONObject();
		boolean flag = PermissionService.refreshPermissions();
        if(flag) {
        	resp.put("msg", "成功同步了权限信息！");
			resp.put("code", 1);
			
        } else {
        	resp.put("msg", "同步权限信息失败，请检查！");
			resp.put("code", 0);
        }
        
        renderJson(resp);
        
	}
	
	
	
	
}
