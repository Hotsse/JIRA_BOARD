package com.hotsse.jira.jira.vo;

import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.domain.Comment;

public class CommentVO {
	
	private long id;
	private String parentKey;
	private String body;
	private String authorId;
	private String authorNm;
	private DateTime createdDate;
	
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
	public String toString() {
		return "CommentVO [id=" + id + ", parentKey=" + parentKey + ", body=" + body + ", authorId=" + authorId
				+ ", authorNm=" + authorNm + ", createdDate=" + createdDate + "]";
	}
	
	public static CommentVO parse(Comment comment, String parentKey) {
		
		CommentVO result = new CommentVO();
		
		result.setId(comment.getId());
		result.setParentKey(parentKey);
		result.setBody(comment.getBody());
		result.setAuthorId(comment.getAuthor().getName());
		result.setAuthorNm(comment.getAuthor().getDisplayName());
		result.setCreatedDate(comment.getCreationDate());
		
		return result;
	}

}
