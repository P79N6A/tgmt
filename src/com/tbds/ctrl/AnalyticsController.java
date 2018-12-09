package com.tbds.ctrl;


public class AnalyticsController extends TbdsBaseController  {

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
