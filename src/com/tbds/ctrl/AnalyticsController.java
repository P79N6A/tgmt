package com.tbds.ctrl;

import com.jfinal.core.Controller;

public class AnalyticsController extends Controller  {

	public void index()
	{
		render("index.html");
	}
	
	public void fault()
	{
		render("fault/index.html");
	}
	
	public void wheel()
	{
		render("wheel/index.html");
	}
	
	public void stop()
	{
		render("stop/index.html");
	}
	
}
