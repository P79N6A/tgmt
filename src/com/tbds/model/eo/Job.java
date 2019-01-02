package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class Job extends Model<Job> {

	private static final long serialVersionUID = -5738982505315800744L;
	
	public static final Job dao = new Job().dao();
	
	//*********job的主要信息:
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	public void setJobName(java.lang.String jobName) {
		set("job_name", jobName);
	}
	
	public java.lang.String getJobName() {
		return getStr("job_name");
	}
	
	public void setOprationType(java.lang.String oprationType) {
		set("opr_type", oprationType);
	}
	
	public java.lang.String getOprationType() {
		return getStr("opr_type");
	}
	
	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}
	
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	public java.lang.String getDescription() {
		return getStr("description");
	}
	
	public void setIsDependOnTemplate(java.lang.Integer isOnTemplate) {
		set("is_on_template", isOnTemplate);
	}
	
	public java.lang.Integer getIsDependOnTemplate() {
		return getInt("is_on_template");
	}
	//job依赖文件（基于此模板进行生成）
	public void setOnTemplateName(java.lang.String onTemplateName) {
		set("on_template_name", onTemplateName);
	}
	
	public java.lang.String getOnTemplateName() {
		return getStr("on_template_name");
	}
	//job依赖的文件job_file
	public void setJobFile(java.lang.String jobFile) {
		set("job_file", jobFile);
	}
	
	public java.lang.String getJobFile() {
		return getStr("job_file");
	}
	//job依赖的文件job_chain
	public void setJobChainFile(java.lang.String jobChainFile) {
		set("job_chain_file", jobChainFile);
	}
	
	public java.lang.String getJobChainFile() {
		return getStr("job_chain_file");
	}
	//job依赖的文件job_chain_order
	public void setJobChainOrderFile(java.lang.String jobChainOrderFile) {
		set("job_chain_order_file", jobChainOrderFile);
	}
	
	public java.lang.String getJobChainOrderFile() {
		return getStr("job_chain_order_file");
	}
	//job依赖的文件job_chain_config
	public void setJobChainConfigFile(java.lang.String jobChainConfigFile) {
		set("job_chain_config_file", jobChainConfigFile);
	}
	
	public java.lang.String getJobChainConfigFile() {
		return getStr("job_chain_config_file");
	}
	//job依赖的文件1
	public void setJobDenpendFile1(java.lang.String jobDependFile1) {
		set("job_depend_file1", jobDependFile1);
	}
	
	public java.lang.String getJobDenpendFile1() {
		return getStr("job_depend_file1");
	}
	//job依赖的文件2
	public void setJobDenpendFile2(java.lang.String jobDependFile2) {
		set("job_depend_file2", jobDependFile2);
	}
	
	public java.lang.String getJobDenpendFile2() {
		return getStr("job_depend_file2");
	}
	
	
	//如下的信息：主要是为了构建server间的视图关系(表述他们之间的关系)
	public void setSourceId(java.lang.Long sourceId) {
		set("source_id", sourceId);
	}
	
	public java.lang.Long getSourceId() {
		return getLong("source_id");
	}
	
	public void setSourceName(java.lang.String sourceName) {
		set("source_name", sourceName);
	}
	
	public java.lang.String getSourceName() {
		return getStr("source_name");
	}
	
	public void setSourceHost(java.lang.String sourceHost) {
		set("source_host", sourceHost);
	}
	
	public java.lang.String getSourceHost() {
		return getStr("source_host");
	}
	
	public void setSourcePort(java.lang.Integer sourcePort) {
		set("source_port", sourcePort);
	}
	
	public java.lang.Integer getSourcePort() {
		return getInt("source_port");
	}
	
	public void setSourceType(java.lang.String sourceType) {
		set("source_type", sourceType);
	}
	
	public java.lang.String getSourceType() {
		return getStr("source_type");
	}
	
	//target + link relationships
	
	public void setTargetId(java.lang.Long targetId) {
		set("target_id", targetId);
	}
	
	public java.lang.Long getTargetId() {
		return getLong("target_id");
	}
	
	public void setTargetName(java.lang.String targetName) {
		set("target_name", targetName);
	}
	
	public java.lang.String getTargetName() {
		return getStr("target_name");
	}
	
	public void setTargetHost(java.lang.String targetHost) {
		set("target_host", targetHost);
	}
	
	public java.lang.String getTargetHost() {
		return getStr("target_host");
	}
	
	public void setTargetPort(java.lang.Integer targetPort) {
		set("target_port", targetPort);
	}
	
	public java.lang.Integer getTargetPort() {
		return getInt("target_port");
	}
	
	public void setTargetType(java.lang.String targetType) {
		set("target_type", targetType);
	}
	
	public java.lang.String getTargetType() {
		return getStr("target_type");
	}
	
	public void setCatalog(java.lang.String catalog) {
		set("catalog", catalog);
	}
	
	public java.lang.String getCatalog() {
		return getStr("catalog");
	}
	
	

}
