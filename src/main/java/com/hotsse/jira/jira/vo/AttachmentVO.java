package com.hotsse.jira.jira.vo;

import com.atlassian.jira.rest.client.api.domain.Attachment;

public class AttachmentVO {
	
	private String parentKey;
	private String uri;
	private String name;
	private int size;
	private String mimeType;
	private String thumbNail;
	
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getThumbNail() {
		return thumbNail;
	}
	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}
	
	@Override
	public String toString() {
		return "AttachmentVO [parentKey=" + parentKey + ", uri=" + uri + ", name=" + name + ", size=" + size
				+ ", mimeType=" + mimeType + ", thumbNail=" + thumbNail + "]";
	}
	
	public static AttachmentVO parse(Attachment attachment, String parentKey) {
		
		AttachmentVO result = new AttachmentVO();
		
		result.setParentKey(parentKey);
		result.setUri(attachment.getContentUri().toString());
		result.setName(attachment.getFilename());
		result.setSize(attachment.getSize());
		result.setMimeType(attachment.getMimeType());
		if(attachment.hasThumbnail()) result.setThumbNail(attachment.getThumbnailUri().toString());
		
		return result;
	}
	
}
