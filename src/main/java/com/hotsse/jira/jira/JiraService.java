package com.hotsse.jira.jira;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Attachment;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.atlassian.jira.rest.client.api.domain.Worklog;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import com.hotsse.jira.common.CommonService;
import com.hotsse.jira.jira.vo.AttachmentVO;
import com.hotsse.jira.jira.vo.CommentVO;
import com.hotsse.jira.jira.vo.IssueVO;
import com.hotsse.jira.jira.vo.WorklogVO;
import com.hotsse.jira.ldap.LdapService;
import com.hotsse.jira.ldap.vo.LdapVO;

@Service
public class JiraService {

	final private static String JIRAURI = "http://works.eduwill.net"; // JIRA URI
	
	@Autowired
	private LdapService ldapService;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * <pre>
	 * 이슈 리스트 조회
	 * </pre>
	 * @methodName	: getJiraIssueList
	 */
	public List<IssueVO> getJiraIssueList(LdapVO ldap) throws Exception{
		
		List<IssueVO> issueList = new ArrayList<IssueVO>();
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			SearchRestClient src = restClient.getSearchClient();
			
			Set<String> fields = new HashSet<>();
			fields.add("*all");
			
			String jql = ""; // 전체 검색
			SearchResult searchResult = src.searchJql(jql).claim();
			
			List<Issue> issues = Lists.newArrayList(searchResult.getIssues());
			for(Issue issue : issues) {
				
				IssueVO iv = new IssueVO();
				
				iv.setKey(issue.getKey());																								// key
				iv.setSummary(issue.getSummary());																				// summary
				iv.setReporterNm(issue.getReporter().getDisplayName());													// reporterNm
				iv.setAssigneeNm((issue.getAssignee() != null) ? issue.getAssignee().getDisplayName() : "");		// assigneeNm
				iv.setReporterId(issue.getReporter().getName());																// reporterId
				iv.setAssigneId(issue.getAssignee().getName());																// assigneeId
				iv.setCreatedDate(issue.getCreationDate().toDate());															// createdDate
				iv.setDueDate((issue.getDueDate() != null) ? issue.getDueDate().toDate() : null);					// dueDate
								
				issueList.add(iv);
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return issueList;
	}
	
	/**
	 * <pre>
	 * 이슈 조회
	 * </pre>
	 * @methodName	: getJiraIssue
	 */
	public IssueVO getJiraIssue(LdapVO ldap, String key) throws Exception{
		
		IssueVO result = new IssueVO();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			result.setKey(issue.getKey());																							// key
			result.setSummary(issue.getSummary());																				// summary
			result.setReporterNm(issue.getReporter().getDisplayName());													// reporterNm
			result.setAssigneeNm((issue.getAssignee() != null) ? issue.getAssignee().getDisplayName() : "");	// assigneeNm
			result.setReporterId(issue.getReporter().getName());																// reporterId
			result.setAssigneId(issue.getAssignee().getName());																// assigneeId
			result.setCreatedDate(issue.getCreationDate().toDate());														// createdDate
			result.setDueDate((issue.getDueDate() != null) ? issue.getDueDate().toDate() : null);					// dueDate
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * 댓글 리스트 조회
	 * </pre>
	 * @methodName	: getJiraCommentList
	 */
	public List<CommentVO> getJiraCommentList(LdapVO ldap, String key){
		
		List<CommentVO> commentList = new ArrayList<CommentVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Comment> comments = issue.getComments();
			if(comments != null) {
				
				for(Comment c : comments) {
					
					CommentVO comment = new CommentVO();
					
					comment.setId(c.getId());
					comment.setParentKey(key);
					comment.setBody(c.getBody());
					comment.setAuthorId(c.getAuthor().getName());
					comment.setAuthorNm(c.getAuthor().getDisplayName());
					comment.setCreatedDate(c.getCreationDate());
					
					commentList.add(comment);					
				}
				Collections.reverse(commentList); // 댓글 역순 정렬
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return commentList;	
	}
	
	/**
	 * <pre>
	 * 첨부파일 리스트 조회
	 * </pre>
	 * @methodName	: getJiraAttachmentList
	 */
	public List<AttachmentVO> getJiraAttachmentList(LdapVO ldap, String key){
		
		List<AttachmentVO> attachmentList = new ArrayList<AttachmentVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Attachment> attachments = issue.getAttachments();
			if(attachments != null) {
				
				for(Attachment a : attachments) {
					
					AttachmentVO attachment = new AttachmentVO();
					
					attachment.setParentKey(key);
					attachment.setUri(a.getContentUri().toString());
					attachment.setName(a.getFilename());
					attachment.setSize(a.getSize());
					attachment.setMimeType(a.getMimeType());
					if(a.hasThumbnail()) attachment.setThumbNail(a.getThumbnailUri().toString());
					
					attachmentList.add(attachment);
				}
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return attachmentList;		
	}
	
	/**
	 * <pre>
	 * 작업시간 리스트 조회
	 * </pre>
	 * @methodName	: getJiraWorklogList
	 */
	public List<WorklogVO> getJiraWorklogList(LdapVO ldap, String key){
		
		List<WorklogVO> worklogList = new ArrayList<WorklogVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Worklog> worklogs = issue.getWorklogs();
			if(worklogs != null) {
				
				for(Worklog w : worklogs) {
					
					WorklogVO worklog = new WorklogVO();
					
					worklog.setParentKey(key);
					worklog.setAuthorId(w.getAuthor().getName());
					worklog.setAuthorNm(w.getAuthor().getDisplayName());
					worklog.setStartDt(w.getCreationDate());
					worklog.setSpentTime(w.getMinutesSpent());
					
					worklogList.add(worklog);					
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return worklogList;		
	}
	
	/**
	 * <pre>
	 * 부작업 리스트 조회
	 * </pre>
	 * @methodName	: getJiraSubtaskList
	 */
	public List<IssueVO> getJiraSubtaskList(LdapVO ldap, String key){
		
		List<IssueVO> subtaskList = new ArrayList<IssueVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(ldap.getId(), ldap.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Subtask> subtasks = issue.getSubtasks();
			if(subtasks != null) {
				
				for(Subtask s : subtasks) {
					
					IssueVO iv = getJiraIssue(ldap, s.getIssueKey());					
					subtaskList.add(iv);
				}
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			commonService.closeInstant(restClient);
		}
		
		return subtaskList;	
	}
	
	
	/**
	 * <pre>
	 * JRJC 클라이언트 인스턴스 생성
	 * </pre>
	 * @methodName	: getJiraRestClient
	 */
	private JiraRestClient getJiraRestClient(String username, String password) {
		
		JiraRestClient jrc = null;
		try {
			jrc = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(new URI(JIRAURI), username, password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return jrc;
	}
	
	/**
	 * <pre>
	 * 이슈 컨트롤러 인스턴스 생성
	 * </pre>
	 * @methodName	: getIssueClient
	 */
	private IssueRestClient getIssueClient(JiraRestClient restClient) {
		return restClient.getIssueClient();
	}
	
}
