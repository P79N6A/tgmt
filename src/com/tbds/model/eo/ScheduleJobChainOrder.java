package com.tbds.model.eo;

import java.io.Serializable;

public class ScheduleJobChainOrder implements Serializable {
	
	private static final long serialVersionUID = -5688286858102959729L;
	 
	private String fileName;//job_chain_fs_collect_mpsdata,order_fs_collect_mpsdata.order.xml
	
	/*
	<?xml version="1.0" encoding="ISO-8859-1"?>
	<order>
	    <params/>
	    <run_time>
	      <weekdays>
	         <day day="1 2 3 4 5 6 7">
	            <period absolute_repeat="00:01:01" begin="00:00" end="24:00"/>
	         </day>
	      </weekdays>
	   </run_time>
	</order>
	*/
	private String params;
	private String run_time;
	
	private String weekdays;
	private String day;// day="1 2 3 4 5 6 7"
	
	private String period;//absolute_repeat="00:01:01" begin="00:00" end="24:00"
	
	
	
}
