package com.tbds.model.eo;

import java.io.Serializable;

//import javax.xml.bind.annotation.XmlRootElement;
//
//@XmlRootElement
public class ScheduleJob  implements Serializable {

	private static final long serialVersionUID = 7100972135737895617L;
	
	/*
	<?xml version="1.0" encoding="ISO-8859-1"?>
	<job  title="mps data collection" order="yes" stop_on_error="no" visible="never">
	    <settings >
	        <log_level ><![CDATA[info]]></log_level>
	    </settings>
	    <description >
	        <include  file="jobs/jadeJob.xml"/>
	    </description>
	    <params >
	        <param  name="operation" value="move"/>
	        <param  name="target_user" value="nagios"/>
	        <param  name="file_spec" value="^.*\.gz$"/>
	        <param  name="source_dir" value="/home/tbds/source"/>
	        <param  name="source_host" value="10.72.0.1"/>
	        <param  name="source_password" value="nagios"/>
	        <param  name="source_port" value="22"/>
	        <param  name="source_protocol" value="sftp"/>
	        <param  name="ssh_auth_method" value="password"/>
	        <param  name="target_host" value="10.72.20.240"/>
	        <param  name="target_password" value="nagios"/>
	        <param  name="target_port" value="22"/>
	        <param  name="target_protocol" value="sftp"/>
	        <param  name="source_user" value="tbds"/>
	        <param  name="target_dir" value="/home/nagios/zt/data/receive"/>
	        <param  name="source_ssh_auth_method" value="password"/>
	        <param  name="target_ssh_auth_method" value="password"/>
	        <param  name="atomic_suffix" value="~"/>
	        <param  name="force_files" value="false"/>
	    </params>
	    <script  language="java" java_class="sos.scheduler.jade.JadeJob"/>
	    <run_time />
	</job>
	*/
	//job filename
	private String job__fileName;//job_fs_collect_mpsdata.job.xml
	
	//Attriutes
	//<job  title="mps data collection" order="yes" stop_on_error="no" visible="never">
	private String job__attr_$name;
	private String job__attr_$title;
	private String job__attr_$order; //yes or no
	private String job__attr_$stop_on_error;//yes or no
	private String job__attr_$visible; //never
	private int job__attr_$timeout;//60
	
	//Tags
	private String job__tag_$settings;//<settings ><log_level ><![CDATA[info]]></log_level></settings>
	private String job__tag_settings_tag_$log_level;//![CDATA[info]]
	
	private String job__tag_$description;//<description ><include  file="jobs/jadeJob.xml"/></description>
	private String job__tag_description_tag_$include;
	private String job__tag_description_tag_include_attr_$file;
	
	private String job__tag_$script;//<script  language="java" java_class="sos.scheduler.jade.JadeJob"/>
	private String job__tag_script_attr_$language;//if language=java, then java_class;if language=shell, forget all
	private String job__tag_script_attr_$java_class;
	private String job__tag_$run_time;//<run_time />
	
	/*
	 <monitor  name="configuration_monitor" ordering="0">
        <script  java_class="com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor" language="java"/>
     </monitor>
	 */
	private String job__tag_$monitor;
	private String job__tag_monitor_attr_$monitor_name;
	private String job__tag_monitor_attr_$ordering;
	private String job__tag_monitor_tag_$script;
	private String job__tag_monitor_tag_script_attr_$java_class;
	private String job__tag_monitor_tag_script_attr_$language;
		
	//params
	//<param  name="operation" value="move"/>
	private String job__tag_$params;
	private String job__tag_params_tag_$param;
	private String job__tag_params_tag_param_attr_$name;
	private String job__tag_params_tag_param_attr_$value;
	
	//params: name + value
	private String operation;
	private String file_spec;
	private String atomic_suffix;//	~
	private String force_files;//false
	
	private int poll_interval;
	private int poll_timeout;
	
	private String source_host;
	private String source_port;
	private String source_dir;
	private String source_protocol;
	private String source_ssh_auth_method;
	private String ssh_auth_method;
	private String source_user;
	private String source_password;
	
	private String target_host;//192.168.32.171
	private String target_port;//22
	private String target_dir;///home/oracle/data/receive
	private String target_protocol;//sftp
	private String target_ssh_auth_method;//password
	private String target_user;//oracle
	private String target_password;//********
	
	
	
	
	
	
	
	
}
