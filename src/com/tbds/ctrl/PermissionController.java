package com.tbds.ctrl;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

public class PermissionController extends Controller {
	private static final Logger log = Logger.getLogger(PermissionController.class);
	public void index() {
		render("index.html");
	}
}
