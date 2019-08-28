package com.hotsse.jira.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotsse.jira.common.staff.vo.StaffVO;
import com.hotsse.jira.ldap.vo.LdapVO;

@Service
public class LdapService {
	
	@Value("${edw.ldap.context}")
	private String LDAP_CONTEXT;
	
	@Value("${edw.ldap.url}")
	private String LDAP_URL;
	
	@Value("${edw.ldap.auth}")
	private String LDAP_AUTH;
	
	public List<LdapVO> searchLdapInfo(StaffVO staff, String userNm) throws Exception {
		
	    List<LdapVO> list = new ArrayList<LdapVO>();
	 
	    // ------- LDAP 서비스 실행
	    // LDAP 접속 권한 및 경로 설정
	    Hashtable<String, String> env = getLdapEnvironment(staff);
	 
	    // LDAP 서비스 실행
	    InitialDirContext ctx = null;
	    NamingEnumeration<javax.naming.directory.SearchResult> results = null;
	    try {
	        //filter OU=EDUWILL,DC=hq,DC=eduwill,DC=net
	        ctx = new InitialDirContext(env);
	 
	        SearchControls searcher = new SearchControls();
	        searcher.setSearchScope(SearchControls.SUBTREE_SCOPE);
	 
	        String contextFilter = "ou=EDUWILL,dc=hq,dc=eduwill,dc=net";
	        String attrFilter = "(&(objectClass=*)(cn=*" + userNm + "*))";
	 
	        // 검색 실행
	        results = ctx.search(contextFilter, attrFilter, searcher);
	 
	        // 수신 결과 파싱
	        while(results.hasMore()) {
	 
	            javax.naming.directory.SearchResult res = (javax.naming.directory.SearchResult)results.next();
	 
	            Attributes attrs = res.getAttributes();
	 
	            LdapVO ldap = new LdapVO();
	            ldap.setId(attrs.get("sAMAccountName").get().toString());
	            ldap.setName(attrs.get("displayName").get().toString());
	            ldap.setTeamNm(attrs.get("distinguishedName").get().toString());
	 
	            list.add(ldap);
	        }
	 
	    } catch(AuthenticationException ae) {
	        // AD 계정 불일치
	        ae.printStackTrace();
	 
	    } catch (NamingException ex) {
	        // 명명 규칙 오류
	        ex.printStackTrace();
	    } finally {
	    	
	        // close results
	        try {
	            if(results != null) results.close();
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	 
	        // close ctx
	        try {
	            if(ctx != null) ctx.close();
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	 
	    }
	 
	    return list;
	}
	
	private Hashtable<String, String> getLdapEnvironment(StaffVO staff) throws Exception {
		
	    Hashtable<String, String> env = new Hashtable<String, String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT); // 프로토콜 컨텍스트
	    env.put(Context.PROVIDER_URL, LDAP_URL);
	    env.put(Context.SECURITY_PRINCIPAL, staff.getId());
	    env.put(Context.SECURITY_CREDENTIALS, staff.getPw());
	    env.put(Context.SECURITY_AUTHENTICATION, LDAP_AUTH);
	 
	    return env;
	}
}
