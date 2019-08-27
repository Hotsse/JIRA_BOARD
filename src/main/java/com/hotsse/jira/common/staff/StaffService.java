package com.hotsse.jira.common.staff;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hotsse.jira.common.staff.vo.StaffVO;


@Service
public class StaffService {
	
	/**
	 * <pre>
	 * 세션에서 계정 읽기
	 * </pre>
	 * @methodName	: getStaff
	 */
	public StaffVO getStaff(HttpServletRequest req) throws Exception {
				
		String id = req.getSession().getAttribute("id").toString();
		String pw = req.getSession().getAttribute("pw").toString();
		
		return new StaffVO(id, pw);
	}

}
