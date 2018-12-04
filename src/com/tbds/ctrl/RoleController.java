package com.tbds.ctrl;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

public class RoleController extends Controller {
	private static final Logger log = Logger.getLogger(RoleController.class);
	public void index() {
		render("index.html");
	}
}
