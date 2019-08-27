package com.hotsse.jira.ldap.vo;

public class LdapVO {
	
	private String id;
	private String pw;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public LdapVO(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}
	
	public LdapVO() {
		
	}
	
}
