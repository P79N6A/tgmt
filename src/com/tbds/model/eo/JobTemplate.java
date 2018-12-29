package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class JobTemplate extends Model<JobTemplate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7472427525520870873L;
	
	
	public static final JobTemplate dao = new JobTemplate().dao();
	
	
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	public void setTemplateName(java.lang.String templateName) {
		set("template_name", templateName);
	}
	
	public java.lang.String getTemplateName() {
		return getStr("template_name");
	}
	
	/**
	 * 模板类型
	 */
	public void setTemplateType(java.lang.String templateType) {
		set("template_type", templateType);
	}
	
	public java.lang.String getTemplateType() {
		return getStr("template_type");
	}
	
	/**
	 * 模板用途
	 */
	public void setTemplateApplication(java.lang.String templateApplication) {
		set("template_application", templateApplication);
	}
	
	public java.lang.String getTemplateApplication() {
		return getStr("template_application");
	}
	
	/**
	 * 模板文件路径
	 */
	public void setFilePath(java.lang.String filePath) {
		set("file_path", filePath);
	}
	
	public java.lang.String getFilePath() {
		return getStr("file_path");
	}
	
	/**
	 * 部分模板文件路径
	 */	
	public java.lang.String getRelativeFilePath() {
		String relativePath = "";
		String filePath = getStr("file_path");
		if(null != filePath && !"".equals(filePath.trim())) {
			relativePath = "..." + filePath.substring(filePath.lastIndexOf("/"));
		}
		return relativePath;
	}
	
	/**
	 * 模板详细描述
	 * 
	 */
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	public java.lang.String getDescription() {
		return getStr("description");
	}
	
	
//	/**
//	 * 用于生成job的名称格式
//	 */
////	public void setJobNameFormat(java.lang.String jobNameFormat) {
////		set("job_name_format", jobNameFormat);
////	}
////	
////	public java.lang.String getJobNameFormat() {
////		return getStr("job_name_format");
////	}
////	
}
