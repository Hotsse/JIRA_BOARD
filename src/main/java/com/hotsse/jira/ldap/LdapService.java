package com.hotsse.jira.ldap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hotsse.jira.ldap.vo.LdapVO;

@Service
public class LdapService {

	/**
	 * <pre>
	 * 쿠키에서 AD 계정 읽기
	 * </pre>
	 * @methodName	: getLdap
	 */
	public LdapVO getLdap(HttpServletRequest req, HttpServletResponse res) throws Exception {
				
		/* 쿠키에서 AD 계정 로그(프로토타입 - 하드코딩 대체)*/
		String id = "YOUR_ID";
		String pw = "YOUR_PW";
		/* // */
		
		return new LdapVO(id, pw);
	}
}
