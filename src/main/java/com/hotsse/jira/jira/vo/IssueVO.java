package com.hotsse.jira.jira.vo;

import java.util.Date;

public class IssueVO {
	
	private String key;
	private String summary;
	private String reporterId;
	private String reporterNm;
	private String assigneId;
	private String assigneeNm;	
	private Date createdDate;
	private Date dueDate;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getReporterNm() {
		return reporterNm;
	}
	public void setReporterNm(String reporterNm) {
		this.reporterNm = reporterNm;
	}
	public String getAssigneeNm() {
		return assigneeNm;
	}
	public void setAssigneeNm(String assigneeNm) {
		this.assigneeNm = assigneeNm;
	}
	public String getReporterId() {
		return reporterId;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	public String getAssigneId() {
		return assigneId;
	}
	public void setAssigneId(String assigneId) {
		this.assigneId = assigneId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}	
	
	@Override
	public String toString() {
		return "IssueVO [key=" + key + ", summary=" + summary + ", reporterNm=" + reporterNm + ", assigneeNm="
				+ assigneeNm + ", reporterId=" + reporterId + ", assigneId=" + assigneId + ", createdDate="
				+ createdDate + ", dueDate=" + dueDate + "]";
	}

}
