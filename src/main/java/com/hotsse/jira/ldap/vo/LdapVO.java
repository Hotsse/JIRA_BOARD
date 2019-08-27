package com.hotsse.jira.ldap.vo;

public class LdapVO {
	
	private String id;
	private String name;
	private String teamNm;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamNm() {
		return teamNm;
	}
	public void setTeamNm(String teamNm) {
		this.teamNm = teamNm;
	}
	
	@Override
	public String toString() {
		return "LdapVO [id=" + id + ", name=" + name + ", teamNm=" + teamNm + "]";
	}
	
}
