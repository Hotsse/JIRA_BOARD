package com.hotsse.jira.jira.vo;

import org.joda.time.DateTime;

public class WorklogVO {
	
	private long id;	
	private String parentKey;
	private String authorId;
	private String authorNm;
	private DateTime startDt;
	private int spentTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorNm() {
		return authorNm;
	}
	public void setAuthorNm(String authorNm) {
		this.authorNm = authorNm;
	}
	public DateTime getStartDt() {
		return startDt;
	}
	public void setStartDt(DateTime startDt) {
		this.startDt = startDt;
	}
	public int getSpentTime() {
		return spentTime;
	}
	public void setSpentTime(int spentTime) {
		this.spentTime = spentTime;
	}
	
	@Override
	public String toString() {
		return "WorklogVO [id=" + id + ", parentKey=" + parentKey + ", authorId=" + authorId + ", authorNm=" + authorNm
				+ ", startDt=" + startDt + ", spentTime=" + spentTime + "]";
	}
	
}
