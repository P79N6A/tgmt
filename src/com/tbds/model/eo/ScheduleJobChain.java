package com.tbds.model.eo;

import java.io.Serializable;

public class ScheduleJobChain implements Serializable {

	private static final long serialVersionUID = 5020761005652616573L;
	
	private String fileName;//job_chain_fs_collect_mpsdata.job_chain.xml
	//<job_chain>
	//	<job_chain_node  state="dc" job="job_fs_collect_mpsdata" next_state="success" error_state="error"/>
	//	<job_chain_node  state="success"/>
	//	<job_chain_node  state="error"/>
	//</job_chain>
	private String job_chain_node;
	
	private String state;
	private String job;//引用ScheduleJob
	private String next_state;
	private String error_state;
	
	
	
}
