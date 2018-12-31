package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class ServerLink extends Model<ServerLink> {

	private static final long serialVersionUID = -5738982505315800744L;
	
	public static final ServerLink dao = new ServerLink().dao();
	
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
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
	
	//^^^^^^^^^^^^^^^^^
	
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
	
	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}

}
