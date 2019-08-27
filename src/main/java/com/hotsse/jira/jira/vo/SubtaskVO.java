package com.hotsse.jira.jira.vo;

import java.util.Date;

public class SubtaskVO {
	
	private long key;
	private String summary;
	private String reporterId;
	private String reporterNm;
	private String assigneeId;
	private String assigneeNm;
	private Date createdDate;
	
	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getReporterId() {
		return reporterId;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	public String getReporterNm() {
		return reporterNm;
	}
	public void setReporterNm(String reporterNm) {
		this.reporterNm = reporterNm;
	}
	public String getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	public String getAssigneeNm() {
		return assigneeNm;
	}
	public void setAssigneeNm(String assigneeNm) {
		this.assigneeNm = assigneeNm;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
