package com.hotsse.jira.ldap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hotsse.jira.ldap.vo.LdapVO;

@Service
public class LdapService {

	/**
	 * <pre>
	 * 세션에서 계정 읽기
	 * </pre>
	 * @methodName	: getLdap
	 */
	public LdapVO getLdap(HttpServletRequest req) throws Exception {
				
		String id = req.getSession().getAttribute("id").toString();
		String pw = req.getSession().getAttribute("pw").toString();
		
		return new LdapVO(id, pw);
	}
}
