package com.tbds.ctrl;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.tbds.service.RoleService;

public class RoleController extends Controller {
	private static final Logger log = Logger.getLogger(RoleController.class);
	public void index() {
		int currentPageIndex = getParaToInt(0, 1);
		setAttr("rolePage", RoleService.paginate(currentPageIndex, 5));
		render("index.html");
	}
}
