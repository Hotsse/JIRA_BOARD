package com.hotsse.jira.common;

import java.io.Closeable;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.hotsse.jira.common.staff.vo.StaffVO;


@Service
public class CommonService {
	
	/**
	 * <pre>
	 * HTTP Method(GET) 로 Request 진행
	 * </pre>
	 * @methodName	: executeHttpDelete
	 */
	public Map<String, String> executeHttpGet(String uri, StaffVO staff) throws Exception{

		// 메소드 변수 선언
		HttpGet httpRequest = new HttpGet();

		// HTTP Request 공통 모듈 실행
		Map<String, String> map = executeHttpClient(httpRequest, uri, staff);

		return map;
	}
	
	/**
	 * <pre>
	 * HTTP Method(DELETE) 로 Request 진행
	 * </pre>
	 * @methodName	: executeHttpDelete
	 */
	public Map<String, String> executeHttpDelete(String uri, StaffVO staff) throws Exception{

		// 메소드 변수 선언
		HttpDelete httpRequest = new HttpDelete();

		// HTTP Request 공통 모듈 실행
		Map<String, String> map = executeHttpClient(httpRequest, uri, staff);

		return map;
	}

	/**
	 * <pre>
	 * HTTP Method(POST) 로 Request 진행
	 * </pre>
	 * @methodName	: executeHttpPost
	 */
	public Map<String, String> executeHttpPost(String uri, StaffVO staff, String body) throws Exception{

		// 메소드 변수 선언
		HttpPost httpRequest = new HttpPost();

		// 바디 설정
		if(body != null) {
			HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
			httpRequest.setEntity(entity);
		}

		// HTTP Request 공통 모듈 실행
		Map<String, String> map = executeHttpClient(httpRequest, uri, staff);

		return map;
	}

	/**
	 * <pre>
	 * HTTP Method(PUT) 로 Request 진행
	 * </pre>
	 * @methodName	: executeHttpPut
	 */
	public Map<String, String> executeHttpPut(String uri, StaffVO staff, String body) throws Exception{

		// 메소드 변수 선언
		HttpPut httpRequest = new HttpPut();

		// 바디 설정
		if(body != null) {
			HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
			httpRequest.setEntity(entity);
		}

		// HTTP Request 공통 모듈 실행
		Map<String, String> map = executeHttpClient(httpRequest, uri, staff);

		return map;
	}

	/**
	 * <pre>
	 * HTTP Client Request 공통 실행 함수
	 * </pre>
	 * @methodName	: executeHttpClient
	 */
	private Map<String, String> executeHttpClient(HttpRequestBase httpRequest, String uri, StaffVO staff){
		
		Map<String, String> map = null;

		// 클라이언트 변수 선언
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 대상 URI 맵핑
		httpRequest.setURI(URI.create(uri));

		// Base64 인코딩
		String strStaff = staff.getId() + ":" + staff.getPw();
		String basicAuth = new String(Base64.getEncoder().encode(strStaff.getBytes()));
		
		// 헤더 설정
		httpRequest.setHeader("Authorization", "Basic " + basicAuth);
		httpRequest.setHeader("Content-Type", "application/json");

		CloseableHttpResponse httpResponse = null;
		try {
			
			// HTTP Request 실행
			httpResponse = httpClient.execute(httpRequest);
			
			// 결과 Map 생성
			map = new HashMap<String, String>();

			// statusCode
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			map.put("statusCode", String.valueOf(statusCode));

			// Response 결과 분기
			if((statusCode / 100) == 2) {
				map.put("result", "OK");

				// totalCount 예외처리
				/*
				if(uri.equals(JIRAURI + "/rest/api/2/search") && statusCode == 200) {
					String strEntity = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					try {
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(strEntity);
						String total = jsonObj.get("total").toString();
						map.put("totalCount", total);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				*/
			}
			else {
				map.put("result", "ERROR");
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeInstant(httpResponse);
			closeInstant(httpClient);
		}

		return map;
	}

	/**
	 * <pre>
	 * 인스턴스 close 공통 함수
	 * </pre>
	 * @methodName	: closeInstant
	 */
	public void closeInstant(Closeable... closeables) {
	    for (Closeable c : closeables) {
	        if (c != null) {
	            try {
	                c.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}
