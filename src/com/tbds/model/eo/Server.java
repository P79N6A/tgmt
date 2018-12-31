package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class Server extends Model<Server> {
	
	private static final long serialVersionUID = -401572277142511610L;
	
	public static final Server dao = new Server().dao();
	
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}
	
	public void setHost(java.lang.String host) {
		set("host", host);
	}
	
	public java.lang.String getHost() {
		return getStr("host");
	}
	
	public void setPort(java.lang.Integer port) {
		set("port", port);
	}
	
	public java.lang.Integer getPort() {
		return getInt("port");
	}
	
	public void setCatalog(java.lang.String catalog) {
		set("catalog", catalog);
	}
	
	public java.lang.String getCatalog() {
		return getStr("catalog");
	}
	
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	public java.lang.String getDescription() {
		return getStr("description");
	}
	
}
