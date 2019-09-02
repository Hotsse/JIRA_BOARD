package com.hotsse.jira.jira;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Attachment;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import com.atlassian.jira.rest.client.api.domain.Worklog;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.WorklogInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import com.hotsse.jira.common.CommonService;
import com.hotsse.jira.common.json.vo.JsonVO;
import com.hotsse.jira.common.staff.vo.StaffVO;
import com.hotsse.jira.jira.vo.AttachmentVO;
import com.hotsse.jira.jira.vo.CommentVO;
import com.hotsse.jira.jira.vo.IssueVO;
import com.hotsse.jira.jira.vo.WorklogVO;

import ch.qos.logback.classic.Logger;

@Service
public class JiraService {
	
	protected final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

	@Value("${edw.domain.jira}")
	private String JIRAURI;
	
	@Value("${edw.jira.project}")
	private String JIRA_PROJECT_CD;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * <pre>
	 * 이슈 리스트 조회
	 * </pre>
	 * @methodName	: getJiraIssueList
	 */
	public List<IssueVO> getJiraIssueList(StaffVO staff) throws Exception {
		
		List<IssueVO> issueList = new ArrayList<IssueVO>();
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			SearchRestClient src = restClient.getSearchClient();
			
			Set<String> fields = new HashSet<>();
			fields.add("*all");
			
			String jql = ""; // 전체 검색
			SearchResult searchResult = src.searchJql(jql).claim();
			
			List<Issue> issues = Lists.newArrayList(searchResult.getIssues());
			for(Issue issue : issues) {
				
				issueList.add(IssueVO.parse(issue));
				
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
	public IssueVO getJiraIssue(StaffVO staff, String key) throws Exception {
		
		IssueVO result = new IssueVO();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			result = IssueVO.parse(issue);
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
	 * 이슈 생성
	 * </pre>
	 * @methodName	: createJiraIssue
	 */
	public IssueVO createJiraIssue(StaffVO staff, IssueVO issue) throws Exception {
		
		IssueVO result = null;	
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			
			IssueInputBuilder builder = new IssueInputBuilder(JIRA_PROJECT_CD, 10102L, issue.getSummary());
			builder.setDescription(issue.getDescription());
			builder.setAssigneeName(issue.getAssigneId());
			builder.setDueDate(new DateTime(issue.getDueDate()));
			
			IssueInput input = builder.build();
			
			BasicIssue basicIssue = restClient.getIssueClient().createIssue(input).claim();
			// Issue result = restClient.getIssueClient().getIssue(basicIssue.getKey()).claim();
			
			result = getJiraIssue(staff, basicIssue.getKey());
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;		
	}
	
	/**
	 * <pre>
	 * 이슈 수정
	 * </pre>
	 * @methodName	: updateJiraIssue
	 */
	public IssueVO updateJiraIssue(StaffVO staff, IssueVO issue) throws Exception {
		
		IssueVO result = null;
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);
			
			IssueInputBuilder builder = new IssueInputBuilder(JIRA_PROJECT_CD, 10102L, issue.getSummary());
			builder.setDescription(issue.getDescription());
			builder.setAssigneeName(issue.getAssigneId());
			builder.setDueDate(new DateTime(issue.getDueDate()));
			
			IssueInput input = builder.build();
			issueClient.updateIssue(issue.getKey(), input).claim();
			
			result = issue;
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
	 * 이슈 삭제
	 * </pre>
	 * @methodName	: deleteJiraIssue
	 */
	public void deleteJiraIssue(StaffVO staff, String key) throws Exception {
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);
			
			issueClient.deleteIssue(key, true).claim();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <pre>
	 * 댓글 리스트 조회
	 * </pre>
	 * @methodName	: getJiraCommentList
	 */
	public List<CommentVO> getJiraCommentList(StaffVO staff, String key) throws Exception {
		
		List<CommentVO> commentList = new ArrayList<CommentVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Comment> comments = issue.getComments();
			if(comments != null) {
				
				for(Comment comment : comments) {
					
					commentList.add(CommentVO.parse(comment, key));					
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
	 * 댓글 등록
	 * </pre>
	 * @methodName	: createComment
	 */
	public boolean createComment(StaffVO staff, String key, String body) throws Exception {
		
		boolean result = false;
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);
			
			Issue issue = issueClient.getIssue(key).claim();
			
			issueClient.addComment(issue.getCommentsUri(), Comment.valueOf(body)).claim();
						
			result = true;
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
	 * 댓글 수정
	 * </pre>
	 * @methodName	: updateComment
	 */
	public boolean updateComment(StaffVO staff, CommentVO comment) throws Exception {
		
		JsonVO json = new JsonVO();
		json.body.put("body", comment.getBody());
		
		String uri = JIRAURI + "/rest/api/2/issue/" + comment.getParentKey() + "/comment/" + comment.getId(); 
		Map<String, String> result = commonService.executeHttpPut(uri, staff, json.toJSONString());
		
		return "OK".equals(result.get("result")) ? true : false;
	}
	
	/**
	 * <pre>
	 * 댓글 삭제
	 * </pre>
	 * @methodName	: deleteComment
	 */
	public boolean deleteComment(StaffVO staff, CommentVO comment) throws Exception {
		
		String uri = JIRAURI + "/rest/api/2/issue/" + comment.getParentKey() + "/comment/" + comment.getId();
		Map<String, String> result = commonService.executeHttpDelete(uri, staff);
		
		return "OK".equals(result.get("result")) ? true : false;
	}
	
	/**
	 * <pre>
	 * 첨부파일 리스트 조회
	 * </pre>
	 * @methodName	: getJiraAttachmentList
	 */
	public List<AttachmentVO> getJiraAttachmentList(StaffVO staff, String key){
		
		List<AttachmentVO> attachmentList = new ArrayList<AttachmentVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Attachment> attachments = issue.getAttachments();
			if(attachments != null) {
				
				for(Attachment attachment : attachments) {
					
					attachmentList.add(AttachmentVO.parse(attachment, key));
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
	 * 첨부파일 등록
	 * </pre>
	 * @methodName	: createAttachment
	 */
	public boolean createAttachment(StaffVO staff, String key, MultipartFile attachment) throws Exception {
		
		boolean result = false;
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);
			
			Issue issue = issueClient.getIssue(key).claim();
			
			if(attachment == null || attachment.getSize() <= 0) {
				throw new Exception();
			}
			
			String orgFileNm = attachment.getOriginalFilename();				
			issueClient.addAttachment(issue.getAttachmentsUri(), new ByteArrayInputStream(attachment.getBytes()), orgFileNm).claim();
			
			result = true;
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
	 * 첨부파일 삭제
	 * </pre>
	 * @methodName	: deleteAttachment
	 */
	public boolean deleteAttachment(StaffVO staff, long attachmentId) throws Exception {
		
		// (DELETE)${domain}/rest/api/2/attachment/${attachmentId}
		String uri = JIRAURI + "/rest/api/2/attachment/" + attachmentId;
		Map<String, String> result = commonService.executeHttpDelete(uri, staff);
		
		return "OK".equals(result.get("result")) ? true : false;		
	}
	
	/**
	 * <pre>
	 * 작업시간 리스트 조회
	 * </pre>
	 * @methodName	: getJiraWorklogList
	 */
	public List<WorklogVO> getJiraWorklogList(StaffVO staff, String key){
		
		List<WorklogVO> worklogList = new ArrayList<WorklogVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Worklog> worklogs = issue.getWorklogs();
			if(worklogs != null) {
				
				for(Worklog worklog : worklogs) {
					
					worklogList.add(WorklogVO.parse(worklog, key));					
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
	 * 작업시간 등록
	 * </pre>
	 * @methodName	: createWorklog
	 */
	public boolean createWorklog(StaffVO staff, WorklogVO worklog) {
		
		boolean result = false;
		
		JiraRestClient restClient = null;
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);
			
			Issue issue = issueClient.getIssue(worklog.getParentKey()).claim();
			
			WorklogInput worklogInput = new WorklogInput(null, issue.getSelf(), null, null, "", worklog.getStartDt(), worklog.getSpentTime(), null);
			issueClient.addWorklog(issue.getWorklogUri(), worklogInput).claim();
			
			result = true;
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
	 * 작업시간 수정
	 * </pre>
	 * @methodName	: updateWorklog
	 */
	public boolean updateWorklog(StaffVO staff, WorklogVO worklog) throws Exception {
		
		JsonVO json = new JsonVO();
		json.body.put("started", worklog.getStartDt().toString("yyyy-MM-dd") + "T" + worklog.getStartDt().toString("HH:mm") + ":00.000+0900");
		json.body.put("timeSpent", (worklog.getSpentTime()/60) + "h " + (worklog.getSpentTime() % 60) + "m");
		
		// (PUT)http://works.eduwill.net/rest/api/2/issue/{issueKey}/worklog/${worklogId}
		String uri = JIRAURI + "/rest/api/2/issue/" + worklog.getParentKey() + "/worklog/" + worklog.getId();
		logger.debug(uri);
		Map<String, String> result = commonService.executeHttpPut(uri, staff, json.toJSONString());
		
		return "OK".equals(result.get("result")) ? true : false;
	}
	
	/**
	 * <pre>
	 * 작업시간 삭제
	 * </pre>
	 * @methodName	: deleteWorklog
	 */
	public boolean deleteWorklog(StaffVO staff, WorklogVO worklog) throws Exception {
		
		// (DELETE)http://works.eduwill.net/rest/api/2/issue/{issueKey}/worklog/${worklogId}
		String uri = JIRAURI + "/rest/api/2/issue/" + worklog.getParentKey() + "/worklog/" + worklog.getId();
		Map<String, String> result = commonService.executeHttpDelete(uri, staff);
		
		return "OK".equals(result.get("result")) ? true : false;		
	}
	
	/**
	 * <pre>
	 * 부작업 리스트 조회
	 * </pre>
	 * @methodName	: getJiraSubtaskList
	 */
	public List<IssueVO> getJiraSubtaskList(StaffVO staff, String key){
		
		List<IssueVO> subtaskList = new ArrayList<IssueVO>();
		
		JiraRestClient restClient = null;		
		try {
			restClient = getJiraRestClient(staff.getId(), staff.getPw());
			IssueRestClient issueClient = getIssueClient(restClient);			
			Issue issue = issueClient.getIssue(key).claim();
			
			Iterable<Subtask> subtasks = issue.getSubtasks();
			if(subtasks != null) {
				
				for(Subtask s : subtasks) {
					
					IssueVO iv = getJiraIssue(staff, s.getIssueKey());					
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
