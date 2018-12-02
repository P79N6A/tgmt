package com.tbds.ctrl;

import com.jfinal.core.Controller;

public class UserController extends Controller {
	public void index() {
		
		setAttr("userPage", null);
		
		render("index.html");
	}
}
