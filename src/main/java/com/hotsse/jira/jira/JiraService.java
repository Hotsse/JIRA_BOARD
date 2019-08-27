package com.hotsse.jira.jira;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Lists;
import com.hotsse.jira.common.CommonService;
import com.hotsse.jira.jira.vo.IssueVO;
import com.hotsse.jira.ldap.LdapService;
import com.hotsse.jira.ldap.vo.LdapVO;

@Service
public class JiraService {

	final private static String JIRAURI = "http://works.eduwill.net"; // JIRA URI
	
	@Autowired
	private LdapService ldapService;
	
	@Autowired
	private CommonService commonService;
	
	public List<IssueVO> getJiraIssueList(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		List<IssueVO> issueList = new ArrayList<IssueVO>();
		
		LdapVO ldap = ldapService.getLdap(req, res);
		
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
				
				iv.setKey(issue.getKey());																							// key
				iv.setSummary(issue.getSummary());																			// summary
				iv.setReporter(issue.getReporter().getDisplayName());														// reporter
				iv.setAssignee((issue.getAssignee() != null) ? issue.getAssignee().getDisplayName() : "");		// assignee
				
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
