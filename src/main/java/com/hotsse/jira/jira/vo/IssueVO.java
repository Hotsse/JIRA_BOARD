package com.hotsse.jira.jira.vo;

import java.util.Date;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class IssueVO {
	
	private String key;
	private String summary;
	private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	public static IssueVO parse(Issue issue) {
		
		IssueVO result = new IssueVO();
		
		result.setKey(issue.getKey());
		result.setSummary(issue.getSummary());
		result.setDescription(issue.getDescription());
		result.setReporterNm(issue.getReporter().getDisplayName());
		result.setAssigneeNm((issue.getAssignee() != null) ? issue.getAssignee().getDisplayName() : "");
		result.setReporterId(issue.getReporter().getName());
		result.setAssigneId(issue.getAssignee().getName());
		result.setCreatedDate(issue.getCreationDate().toDate());
		result.setDueDate((issue.getDueDate() != null) ? issue.getDueDate().toDate() : null);
		
		return result;		
	}

}
